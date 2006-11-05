/*
 * ObjectBlackboard.java
 *
 * Copyright by Stefan Kleine Stegemann, 2005
 */
package org.openbbs.blackboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * An ObjectBlackboard holds arbritrary objects.
 * 
 * @author sks
 */
public class ObjectBlackboard implements Blackboard
{
   private final Map<BlackboardObserver, ZoneSelector> observers = new HashMap<BlackboardObserver, ZoneSelector>();
   private CloneStrategy cloneStrategy;
   private final Set<Zone> knownZones = new HashSet<Zone>();
   private final Map<Object, Zone> entries = new HashMap<Object, Zone>();

   public ObjectBlackboard()
   {
      this(new CloneByMethodStrategy("clone"));
   }

   public ObjectBlackboard(CloneStrategy cloneStrategy)
   {
      this.setCloneStrategy(cloneStrategy);
      this.openZone(Zone.DEFAULT);
   }

   public void setCloneStrategy(CloneStrategy cloneStrategy)
   {
      Validate.notNull(cloneStrategy);
      this.cloneStrategy = cloneStrategy;
   }

   public synchronized void openZone(Zone zone)
   {
      Validate.notNull(zone);

      if (this.knowsZone(zone))
         throw new BlackboardZoneException("zone " + zone.name() + " is already open");

      this.knownZones.add(zone);
   }

   public synchronized void closeZone(Zone zone)
   {
      Validate.notNull(zone);

      if (!this.knowsZone(zone))
         throw new BlackboardZoneException("zone " + zone.name() + " is unknown");

      Set<Object> entriesToRemove = new HashSet<Object>();
      for (Object entry : this.entries.keySet()) {
         if (this.entries.get(entry).equals(zone)) entriesToRemove.add(entry);
      }

      for (Object entryToRemove : entriesToRemove)
         this.remove(entryToRemove);

      this.knownZones.remove(zone);
   }

   public synchronized void write(Zone zone, Object entry)
   {
      Validate.notNull(entry);

      if (!this.knowsZone(zone))
         throw new BlackboardZoneException("zone " + zone.name() + " is unknown");

      if (this.entries.containsKey(entry)) {
         throw new WriteBlackboardException("entry " + entry.toString()
                  + " is already present on this blackboard");
      }

      Object clonedEntry = this.cloneStrategy.clone(entry);
      this.entries.put(clonedEntry, zone);
      this.notifyEntryAdded(zone, clonedEntry);
   }

   public Zone zoneOf(Object entry)
   {
      Zone zone = this.entries.get(entry);
      if (zone == null) throw new ReadBlackboardException("unknown entry " + entry.toString());

      return zone;
   }

   public synchronized Object read(ZoneSelector zoneSelector, EntryFilter filter)
   {
      Validate.notNull(zoneSelector);
      Validate.notNull(filter);

      for (Object entry : this.entries.keySet()) {
         if (!zoneSelector.selects(this.zoneOf(entry))) continue;

         if (filter.selects(entry)) return this.cloneStrategy.clone(entry);
      }

      return null;
   }

   public Set<Object> readAll(ZoneSelector zoneSelector, EntryFilter filter)
   {
      Validate.notNull(zoneSelector);
      Validate.notNull(filter);

      Set<Object> entries = new HashSet<Object>();
      for (Object entry : this.entries.keySet()) {
         if (!zoneSelector.selects(this.zoneOf(entry))) continue;

         if (filter.selects(entry)) entries.add(this.cloneStrategy.clone(entry));
      }

      return entries;
   }

   public boolean exists(ZoneSelector zoneSelector, EntryFilter filter)
   {
      return this.read(zoneSelector, filter) != null;
   }

   public synchronized Object take(ZoneSelector zoneSelector, EntryFilter filter)
   {
      Validate.notNull(filter);

      Object takenEntry = null;

      for (Object entry : this.entries.keySet()) {
         if (filter.selects(entry)) {
            takenEntry = entry;
            break;
         }
      }

      if (takenEntry == null) throw new ReadBlackboardException("no entry selected");

      this.remove(takenEntry);
      return takenEntry;
   }

   public void registerInterest(ZoneSelector zoneSelector, BlackboardObserver observer)
   {
      Validate.notNull(zoneSelector);
      Validate.notNull(observer);
      this.observers.put(observer, zoneSelector);
   }

   private void remove(Object entry)
   {
      Validate.notNull(entry);
      Zone zone = this.entries.remove(entry);
      this.notifyEntryRemoved(zone, entry);
   }

   private boolean knowsZone(Zone zone)
   {
      Validate.notNull(zone);
      return this.knownZones.contains(zone);
   }

   private void notifyEntryAdded(Zone zone, Object entry)
   {
      for (BlackboardObserver observer : this.observers.keySet()) {
         if (this.isObserverInterstedInZone(observer, zone))
            observer.blackboardDidAddEntry(this, zone, entry);
      }
   }

   private void notifyEntryRemoved(Zone zone, Object entry)
   {
      for (BlackboardObserver observer : this.observers.keySet()) {
         if (this.isObserverInterstedInZone(observer, zone))
            observer.blackboardDidRemoveEntry(this, zone, entry);
      }
   }

   private boolean isObserverInterstedInZone(BlackboardObserver observer, Zone zone)
   {
      Validate.notNull(observer);
      Validate.notNull(zone);

      ZoneSelector zoneSelectorForObserver = this.observers.get(observer);
      Validate.notNull(zoneSelectorForObserver, "observer " + observer + " has no ZoneSelector");
      return zoneSelectorForObserver.selects(zone);
   }
}
