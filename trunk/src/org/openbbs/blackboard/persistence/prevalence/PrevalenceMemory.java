package org.openbbs.blackboard.persistence.prevalence;

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
   private LogFile logFile;

   /**
    * The logfile is the place where changes to the memory are logged.
    */
   public void setLogFile(LogFile logFile) {
      Validate.notNull(logFile, "logFile must be specified");

      if (this.logFile != null) {
         this.logFile.closeLog();
      }

      this.logFile = logFile;
   }

   /**
    * Restore the contents of this memory from the logfile. The
    * entire memory is deleted first. If this method fails, the
    * state of the memory is undefined.
    */
   public void restore() {
      Validate.notNull(this.logFile, "logFile is not set");

      this.logFile.playback(new PlaybackDelegate() {
         public void createZone(Zone zone) {
            memory.createZone(zone);
         }

         public void dropZone(Zone zone) {
            memory.dropZone(zone);
         }

         public void storeEntry(Zone zone, Object entry) {
            memory.storeEntry(zone, entry);
         }

         public void removeEntry(Object entry) {
            memory.removeEntry(entry);
         }
      });
   }

   public void createZone(Zone zone) {
      this.memory.createZone(zone);
      this.logFile.writeCommand(new CreateZoneCommand(zone));
   }

   public void dropZone(Zone zone) {
      this.memory.dropZone(zone);
      this.logFile.writeCommand(new DropZoneCommand(zone));
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
      this.logFile.writeCommand(new RemoveEntryCommand(entry));
   }

   public void storeEntry(Zone zone, Object entry) {
      this.memory.storeEntry(zone, entry);
      this.logFile.writeCommand(new StoreEntryCommand(zone, entry));
   }

   public boolean zoneExists(Zone zone) {
      return this.memory.zoneExists(zone);
   }
}
