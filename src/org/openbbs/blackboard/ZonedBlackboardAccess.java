package org.openbbs.blackboard;

import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * A BlackboardAcccess implementation which restricts access
 * to a particular zone on a blackboard.
 * 
 * @author stefan
 */
public class ZonedBlackboardAccess implements BlackboardAccess
{
   private final Blackboard blackboard;
   private final Zone zone;
   private final ZoneSelector zoneSelector;

   public ZonedBlackboardAccess(Blackboard blackboard, Zone zone)
   {
      Validate.notNull(blackboard);
      Validate.notNull(zone);
      this.blackboard = blackboard;
      this.zone = zone;
      this.zoneSelector = new ExactZoneSelector(zone);
   }

   public boolean exists(EntryFilter filter)
   {
      return this.blackboard.exists(this.zoneSelector, filter);
   }

   public Object read(EntryFilter filter)
   {
      return this.blackboard.read(this.zoneSelector, filter);
   }

   public Set<Object> readAll(EntryFilter filter)
   {
      return this.blackboard.readAll(this.zoneSelector, filter);
   }

   public Object take(EntryFilter filter)
   {
      return this.blackboard.take(this.zoneSelector, filter);
   }

   public void write(Object entry)
   {
      this.blackboard.write(this.zone, entry);
   }
}
