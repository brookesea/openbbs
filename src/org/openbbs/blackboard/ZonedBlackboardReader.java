/*
 * ZonedBlackboardReader.java
 *
 * Copyright (C) Jan 17, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * @author stefan
 */
public class ZonedBlackboardReader
{
   private Blackboard blackboard = null;
   private ZoneSelector zoneSelector = null;

   protected void setBlackboardAndZone(Blackboard blackboard, ZoneSelector zoneSelector)
   {
      this.blackboard = blackboard;
      this.zoneSelector = zoneSelector;
   }

   protected final Blackboard blackboard()
   {
      Validate.notNull(this.blackboard, "no blackboad has beens set");
      return this.blackboard;
   }

   protected final ZoneSelector zoneSelector()
   {
      Validate.notNull(this.zoneSelector, "no zone has beens set");
      return this.zoneSelector;
   }

   protected final Object readBB(EntryFilter filter)
   {
      return this.blackboard().read(this.zoneSelector(), filter);
   }

   protected final boolean existsOnBB(EntryFilter filter)
   {
      return this.blackboard().exists(this.zoneSelector(), filter);
   }

   protected final Set<Object> readAllFromBB(EntryFilter filter)
   {
      return this.blackboard().readAll(this.zoneSelector(), filter);
   }

   protected final Object takeFromBB(EntryFilter filter)
   {
      return this.blackboard().take(this.zoneSelector(), filter);
   }
}
