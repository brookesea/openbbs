package org.openbbs.blackboard.persistence;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;

/**
 * A transient BlackboardMemory implementation. All entries are
 * kept in main memory.
 */
public class TransientMemory implements BlackboardMemory, Serializable
{
   private Map<Zone, Set<Object>> zones = new HashMap<Zone, Set<Object>>();
   private Map<Object, Zone> entries = new HashMap<Object, Zone>();
   
   public void createZone(Zone zone) {
      Validate.notNull(zone, "zone must not be null");
      
      if (zones.containsKey(zone)) {
         throw new BlackboardMemoryException("zone " + zone + " already exists");
      }
      this.zones.put(zone, new HashSet<Object>());
   }

   public void dropZone(Zone zone) {
      Validate.notNull(zone, "zone must not be null");
      
      if (!zones.containsKey(zone)) {
         throw new BlackboardMemoryException("zone " + zone + " does not exist");
      }

      for (Object entry : this.zones.remove(zone)) {
         this.entries.remove(entry);
      }
   }

   public boolean zoneExists(Zone zone) {
      return this.zones.containsKey(zone);
   }

   public Iterator<Object> getEntries(ZoneSelector zoneSelector, EntryFilter entryFilter) {
      return new EntryIterator(zoneSelector, entryFilter);
   }

   public void storeEntry(Zone zone, Object entry) {
      Validate.notNull(entry, "cannot store null entry");
      Validate.notNull(zone, "cannot store entry in null-zone");
      
      if (this.entries.containsKey(entry)) {
         throw new BlackboardMemoryException("cannot store entry " + entry + " twice inside the same memory");
      }
      
      Set<Object> objectsInZone = this.zones.get(zone);
      Validate.notNull(objectsInZone, "zone " + zone + " is unknown");
      objectsInZone.add(entry);
      entries.put(entry, zone);
   }

   public boolean entryExists(Object entry) {
      Validate.notNull(entry, "cannot check existence of a null-entry");
      return this.entries.containsKey(entry);
   }

   public Zone getZone(Object entry) {
      Validate.notNull(entry, "cannot get the zone of a null-entry");
      return this.entries.get(entry);
   }

   public void removeEntry(Object entry) {
      Validate.notNull(entry, "cannot remove null-entry from memory");
      Zone zone = this.entries.remove(entry);
      Validate.notNull(zone, "removed entry has no zone");
      Validate.isTrue(this.zones.get(zone).remove(entry), "failed to remove entry from zone");
   }
   
   private class EntryIterator implements Iterator<Object>
   {
      private Iterator<Zone> zoneIterator = null;
      private Iterator<Object> entryIterator = null;
      private final EntryFilter entryFilter;
      private Object nextEntry = null;
      
      public EntryIterator(ZoneSelector zoneSelector, EntryFilter entryFilter) {
         Validate.notNull(entryFilter);
         this.zoneIterator = new ZoneIterator(zoneSelector);
         this.entryFilter = entryFilter;
      }
      
      public boolean hasNext() {
         if (this.nextEntry != null) {
            return true;
         }
         return this.updateNextEntry();
      }

      public Object next() {
         if (this.nextEntry == null) {
            if (!this.updateNextEntry()) {
               throw new IllegalStateException("no more entries to iterate over");
            }
         }

         Validate.notNull(this.nextEntry, "nextEntry is not set");
         Object entry = this.nextEntry;
         this.nextEntry = null;
         return entry;
      }

      public void remove() {
         throw new UnsupportedOperationException("remove is not supported by this iterator");
      }
      
      private boolean updateNextEntry() {
         this.nextEntry = null;
         
         if (this.entryIterator == null) {
            this.gotoNextZone();
         }

         while (this.entryIterator != null && this.nextEntry == null) {
            while (this.entryIterator.hasNext()) {
               Object candidate = this.entryIterator.next();
               if (this.entryFilter.selects(candidate)) {
                  this.nextEntry = candidate;
                  break;
               }
            }

            if (this.nextEntry == null) {
               this.gotoNextZone();
            }
         }
         
         return this.nextEntry != null;
      }
      
      private void gotoNextZone() {
         if (!this.zoneIterator.hasNext()) {
            this.entryIterator = null;
            return;
         }
         
         Zone nextZone = this.zoneIterator.next();
         this.entryIterator = zones.get(nextZone).iterator();
      }
   }
   
   private class ZoneIterator implements Iterator<Zone>
   {
      private final ZoneSelector zoneSelector;
      private final Iterator<Zone> allZonesIterator;
      private Zone nextZone = null;
      
      public ZoneIterator(ZoneSelector zoneSelector) {
         Validate.notNull(zoneSelector);
         this.zoneSelector = zoneSelector;
         this.allZonesIterator = zones.keySet().iterator();
      }

      public boolean hasNext() {
         if (this.nextZone != null) {
            return true;
         }
         return this.updateNextZone();
      }

      public Zone next() {
         if (this.nextZone == null) {
            if (!this.updateNextZone()) {
               throw new IllegalStateException("no more zones to iterate over");
            }
         }

         Validate.notNull(this.nextZone, "nextZone is not set");
         Zone zone = this.nextZone;
         this.nextZone = null;
         return zone;
      }
      
      public void remove() {
         throw new UnsupportedOperationException("remove is not supported by this iterator");
      }
      
      private boolean updateNextZone() {
         this.nextZone = null;
         while (this.allZonesIterator.hasNext()) {
            Zone candiate = this.allZonesIterator.next();
            if (this.zoneSelector.selects(candiate)) {
               this.nextZone = candiate;
               break;
            }
         }
         return this.nextZone != null;
      }
   }
}
