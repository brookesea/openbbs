package org.openbbs.blackboard.persistence.prevalence;

import java.io.File;
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

      //this.closeLog();
      this.logFile = logFile;
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
   }

   public void storeEntry(Zone zone, Object entry) {
      this.memory.storeEntry(zone, entry);
   }

   public boolean zoneExists(Zone zone) {
      return this.memory.zoneExists(zone);
   }
}
