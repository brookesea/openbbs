package org.openbbs.blackboard.persistence.prevalence;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.PersistenceDelegate;

/**
 * A simple PersistenceDelegate implementation that is based on
 * the concept of object prevalence. It does not require a database
 * or something similar. Instead, for each change event (store or remove),
 * a command is created and serialized to an external media (typically
 * the harddisc). A command contains all information that is neccessary
 * to redo the event. When the entries have to be restored from the
 * external media, the commands are simply applied in the order they
 * have been written.
 * <p />
 * An PrevalencePersistenceDelegate instance is exclusive to a particular
 * blackboard. It cannot be used to store the data of multiple blackboards.
 * <p />
 * A PrevalencePersistenceDelegate imposes certain requirements on the
 * entries which are stored on a blackboard and in turn have to be handled
 * by the delegate. As simple object serialization is used to write objects
 * to the external media, all entries have to be serializable. In order
 * to identify entries, the equals and hashCode methods must be implemented
 * in such a way that they return the same result for two instances which
 * have been restored from the serialized representation of an object. The
 * same requirement applies to Zones.
 */
public class PrevalencePersistenceDelegate implements PersistenceDelegate
{
   private ObjectOutputStream cmdstream = null;
   private File logFile = null;

   /**
    * The logfile is the place where commands are stored. A new command
    * is appeneded to the logfile. The logfile will be open as long as the
    * system runs or until a new logfile is set. If the logfile does not
    * exist, it will be created.
    */
   public void setLogFile(File logFile) {
      Validate.notNull(logFile, "logFile must be specified");

      this.closeCommandOutputStream();
      this.logFile = logFile;
   }
   
   /**
    * 
    */
   public void restore(PlaybackDelegate playbackDelegate) {
      Validate.notNull(playbackDelegate);
   }
   
   /**
    * @see PrevalencePersistenceDelegate#storeEntry(Blackboard, Zone, Object)
    * 
    * Create a new command for storing the specified entry and append
    * it to the command log.
    */
   public void storeEntry(Blackboard blackboard, Zone zone, Object entry) {
      this.writeCommand(new StoreEntryCommand(zone, entry));
   }

   /**
    * @see PrevalencePersistenceDelegate#removeEntry(Blackboard, Zone, Object)
    * 
    * Create a new command for removing the specified entry and append
    * it to the command log.
    */
   public void removeEntry(Blackboard blackboard, Zone zone, Object entry) {
      this.writeCommand(new RemoveEntryCommand(zone, entry));
   }
   
   /**
    * Gracefully terminate the delegate. Frees all allocated resources and
    * makes sure that open files are closed.
    */
   public void terminate() {
      this.closeCommandOutputStream();
   }
   
   private void writeCommand(PrevalenceCommand command) {
      Validate.notNull(command);

      if (!this.isCommandStreamOpen()) {
         this.openCommandOutputStream();
      }
      
      try {
         this.cmdstream.writeObject(command);
      }
      catch (IOException exc) {
         throw new PrevalencePersistenceException("failed to write command " + command + " to " + this.logFile, exc);
      }
   }
   
   private void openCommandOutputStream() {
      Validate.isTrue(!this.isCommandStreamOpen(), "command stream is already open");
      Validate.notNull(this.logFile, "logFile is not set");

      try {
         this.cmdstream = new ObjectOutputStream(new FileOutputStream(this.logFile, true));
      }
      catch (IOException exc) {
         throw new PrevalencePersistenceException("unable to open logFile " + this.logFile, exc);
      }
   }
   
   private void closeCommandOutputStream() {
      if (!this.isCommandStreamOpen()) return; // not open

      try {
         this.cmdstream.flush();
         this.cmdstream.close();
      }
      catch (IOException exc) {
         throw new PrevalencePersistenceException("failed to close logFile " + this.logFile, exc);
      }
   }
   
   private boolean isCommandStreamOpen() {
      return this.cmdstream != null;
   }
}
