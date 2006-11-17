package org.openbbs.blackboard.persistence.prevalence;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
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
   private ObjectOutputStream logStream = null;
   private File logFile = null;

   /**
    * The logfile is the place where commands are stored. A new command
    * is appeneded to the logfile. The logfile will be openend for writing
    * as soon as the first entry is stored or removed. It will remain open
    * as long as the system runs, the closeLog method is called or or until
    * a new logfile is set. If the logfile does not exist, it will be created.
    */
   public void setLogFile(File logFile) {
      Validate.notNull(logFile, "logFile must be specified");

      this.closeLog();
      this.logFile = logFile;
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
      this.writeCommand(new RemoveEntryCommand(entry));
   }

   /**
    * @see PrevalencePersistenceDelegate#restore(PlaybackDelegate)
    * 
    * Basically reads the commands from the logfile and executes them.
    * Each command will invoke a particular method on the playbackDelegate.
    * If the logfile is currently open, it will be closed first. However,
    * the logfile is re-opened as soon as the next entry is stored or
    * removed.
    */
   public void restoreEntries(PlaybackDelegate playbackDelegate) {
      Validate.notNull(playbackDelegate);

      if (this.isLogOpen()) {
         this.closeLog();
      }

      try {
         ObjectInputStream oin = new ObjectInputStream(new FileInputStream(this.logFile));
         boolean eof = false;
         while (!eof) {
            try {
               Object object = oin.readObject();
               ((PrevalenceCommand)object).playback(playbackDelegate);
            }
            catch (EOFException _) {
               eof = true;
            }
         }
         oin.close();
      }
      catch (Exception exc) {
         throw new PrevalencePersistenceException("failed to restore entries from logfile " + this.logFile, exc);
      }
   }

   /**
    * Close the logfile. The logfile will be re-opened when the next
    * entry is stored or removed. Does nothing if the logfile is not
    * open.
    */
   public void closeLog() {
      if (!this.isLogOpen()) return; // not open

      try {
         this.logStream.flush();
         this.logStream.close();
      }
      catch (IOException exc) {
         throw new PrevalencePersistenceException("failed to close logFile " + this.logFile, exc);
      }
   }

   /**
    * Test if the logfile is currently open.
    */
   public boolean isLogOpen() {
      return this.logStream != null;
   }

   private void writeCommand(PrevalenceCommand command) {
      Validate.notNull(command);

      if (!this.isLogOpen()) {
         this.openLog();
      }

      try {
         this.logStream.writeObject(command);
      }
      catch (IOException exc) {
         throw new PrevalencePersistenceException("failed to write command " + command + " to " + this.logFile, exc);
      }
   }

   private void openLog() {
      Validate.isTrue(!this.isLogOpen(), "command stream is already open");
      Validate.notNull(this.logFile, "logFile is not set");

      try {
         this.logStream = new ObjectOutputStream(new FileOutputStream(this.logFile, true));
      }
      catch (IOException exc) {
         throw new PrevalencePersistenceException("unable to open logFile " + this.logFile, exc);
      }
   }
}
