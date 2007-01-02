package org.openbbs.blackboard.persistence.prevalence;

import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.persistence.BlackboardMemory;
import org.openbbs.blackboard.persistence.TransientMemory;
import org.openbbs.blackboard.persistence.snapshot.Snapshotter;

/**
 * A Blackboard memory implementation that is based on object prevalence.
 * Entries will be kept in memory but all changes to the memory will be
 * logged to a file. When the memory is restored, it will replay the logged
 * changes and thereby restore the contents of the memory.
 */
public class PrevalenceMemory implements BlackboardMemory
{
   private TransientMemory memory = new TransientMemory();
   private Snapshotter snapshotter = null;
   private LogFile logFile;
   
   /**
    * Create a new PrevalenceMemory. You have to set at least a
    * logFile before entries can be stored in a memory. If you
    * want support for memory snapshots, you have set a Snapshotter
    * too.
    */
   public PrevalenceMemory() {
      return;
   }

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
    * Set the Snapshotter to be used to create snapshots of this
    * memory.
    */
   public void setSnapshotter(Snapshotter snapshotter) {
      this.snapshotter = snapshotter;
   }

   /**
    * Restore the contents of this memory from the logfile. If a snapshotter
    * is set, the memory is restored from the latest snapshot before the logfile
    * is replayed. The entire memory is deleted first. If this method fails, the
    * state of the memory is undefined.
    */
   public void restore() {
      Validate.notNull(this.logFile, "logFile is not set");
      
      this.memory.clear();
      
      if (this.snapshotter != null) {
         this.snapshotter.restoreFromSnapshot(this.memory);
      }

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
   
   /**
    * Store a snapshot of the memory's current state and reset the
    * logfile. The Snapshotter property must be set in order for
    * this method to work.
    */
   public void snapshot() {
      if (this.snapshotter == null) {
         throw new IllegalStateException("no Snapshotter defined for this memory");
      }
      
      this.snapshotter.takeSnapshot(this.memory);
      this.logFile.reset();
   }

   /**
    * @see BlackboardMemory#createZone(Zone)
    */
   public void createZone(Zone zone) {
      this.memory.createZone(zone);
      this.logFile.writeCommand(new CreateZoneCommand(zone));
   }

   /**
    * @see BlackboardMemory#dropZone(Zone)
    */
   public void dropZone(Zone zone) {
      this.memory.dropZone(zone);
      this.logFile.writeCommand(new DropZoneCommand(zone));
   }

   /**
    * @see BlackboardMemory#entryExists(Object)
    */
   public boolean entryExists(Object entry) {
      return this.memory.entryExists(entry);
   }

   /**
    * @see BlackboardMemory#getEntries(ZoneSelector, EntryFilter)
    */
   public Iterator<Object> getEntries(ZoneSelector zoneSelector, EntryFilter entryFilter) {
      return this.memory.getEntries(zoneSelector, entryFilter);
   }

   /**
    * @see BlackboardMemory#getZone(Object)
    */
   public Zone getZone(Object entry) {
      return this.memory.getZone(entry);
   }

   /**
    * @see BlackboardMemory#removeEntry(Object)
    */
   public void removeEntry(Object entry) {
      this.memory.removeEntry(entry);
      this.logFile.writeCommand(new RemoveEntryCommand(entry));
   }

   /**
    * @see BlackboardMemory#storeEntry(Zone, Object)
    */
   public void storeEntry(Zone zone, Object entry) {
      this.memory.storeEntry(zone, entry);
      this.logFile.writeCommand(new StoreEntryCommand(zone, entry));
   }

   /**
    * @see BlackboardMemory#zoneExists(Zone)
    */
   public boolean zoneExists(Zone zone) {
      return this.memory.zoneExists(zone);
   }
}
