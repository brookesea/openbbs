package org.openbbs.blackboard.persistence.prevalence;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.persistence.BlackboardMemory;
import org.openbbs.blackboard.persistence.TransientMemory;

/**
 * A Blackboard memory implementation that is based on object prevalence.
 * Entries will be kept in memory but all changes to the memory will be
 * logged to a file. When the memory is restored, it will replay the logged
 * changes and thereby restore the contents of the memory.
 */
public class PrevalenceMemory implements BlackboardMemory
{
   private final TransientMemory memory = new TransientMemory();
   private ObjectOutputStream logStream = null;
   private File logFile = null;
   
   /**
    * The logfile is the place where changes to the memory are logged. The logfile
    * will be openend for writing as soon as the first change happens. It will remain
    * open as long as the system runs, the closeLog method is called or or until
    * a new logfile is set. If the logfile does not exist, it will be created.
    */
   public void setLogFile(File logFile) {
      Validate.notNull(logFile, "logFile must be specified");

      this.closeLog();
      this.logFile = logFile;
   }

   /**
    * Test if the logfile is currently open.
    */
   public boolean isLogOpen() {
      return this.logStream != null;
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
    * Restore the contents of this memory from the logfile. The
    * entire memory is deleted first. If this method fails, the
    * state of the memory is undefined.
    */
   public void restore() {
      Validate.notNull(this.logFile, "logFile is not set");

      if (this.isLogOpen()) {
         this.closeLog();
      }
      
      PlaybackDelegate playbackDelegate = new RestorePlaybackDelegate();
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

   public void createZone(Zone zone) {
      this.memory.createZone(zone);
   }

   public void dropZone(Zone zone) {
      this.memory.dropZone(zone);
   }

   public boolean entryExists(Object entry) {
      return this.memory.entryExists(entry);
   }

   public Iterator<Object> getEntries(ZoneSelector zoneSelector, EntryFilter entryFilter) {
      return this.memory.getEntries(zoneSelector, entryFilter);
   }

   public Zone getZone(Object entry) {
      return this.memory.getZone(entry);
   }

   public void removeEntry(Object entry) {
      this.memory.removeEntry(entry);
      this.writeCommand(new RemoveEntryCommand(entry));
   }

   public void storeEntry(Zone zone, Object entry) {
      this.memory.storeEntry(zone, entry);
      this.writeCommand(new StoreEntryCommand(zone, entry));
   }

   public boolean zoneExists(Zone zone) {
      return this.memory.zoneExists(zone);
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
   
   private class RestorePlaybackDelegate implements PlaybackDelegate
   {
      public void storeEntry(Zone zone, Object entry) {
         memory.storeEntry(zone, entry);
      }

      public void removeEntry(Object entry) {
         memory.removeEntry(entry);
      }
   }
}
