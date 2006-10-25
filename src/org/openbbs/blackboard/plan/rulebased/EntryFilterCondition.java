/*
 * EntryFilterCondition.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.plan.rulebased;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.ZonedBlackboardReader;

/**
 * Evaluates to true if at least one object is selected from a Blackboard.
 *
 * @author stefan
 */
public abstract class EntryFilterCondition extends ZonedBlackboardReader implements
         PlanStepCondition, EntryFilter
{
   protected EntryFilterCondition(Blackboard blackboard, ZoneSelector zoneSelector)
   {
      Validate.notNull(blackboard);
      Validate.notNull(zoneSelector);
      this.setBlackboardAndZone(blackboard, zoneSelector);
   }

   public boolean evaluate()
   {
      return this.existsOnBB(this);
   }
}
