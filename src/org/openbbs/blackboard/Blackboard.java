/*
 * Blackboard.java
 *
 * Copyright (C) Dec 15, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import java.util.Set;

/**
 * @author stefan
 */
public interface Blackboard
{
   /**
    * Open a new zone. Before a zone is not opened, it is not possible
    * to write entries to it.
    */
   public void openZone(Zone zone);

   /**
    * Close an open zone and remove all entries in this zone from the
    * receiver.
    */
   public void closeZone(Zone zone);

   /**
    * Write an non-null entry into a zone on the blackboard.
    */
   public void write(Zone zone, Object entry);

   /**
    * Determine the zone of a particular entry.
    */
   public Zone zoneOf(Object entry);

   /**
    * Return the first entry which lives in a selected zone and
    * is selected by the specified filter.
    */
   public Object read(ZoneSelector zoneSelector, EntryFilter filter);

   /**
    * Return all entries which are living in a selected zone and
    * which are selected by the specified filter.
    */
   public Set<Object> readAll(ZoneSelector zoneSelector, EntryFilter filter);

   /**
    * Check whether the receiver contains at least one entry which
    * lives in a selected zone and is selected by the specified filter.
    */
   public boolean exists(ZoneSelector zoneSelector, EntryFilter filter);

   /**
    * Remove and return the first object which lives in a selected
    * zone and is selected by the specified filter from the receiver.
    */
   public Object take(ZoneSelector zoneSelector, EntryFilter filter);

   /**
    * Register an observer which get's notified when the receiver's
    * state changes inside a selected zone.
    */
   public void registerInterest(ZoneSelector zoneSelector, BlackboardObserver observer);
}
