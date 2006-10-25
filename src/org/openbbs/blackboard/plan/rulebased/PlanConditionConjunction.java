/*
 * PlanConditionConjunction.java
 *
 * Copyright (C) Jan 11, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.plan.rulebased;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * @author stefan
 */
public class PlanConditionConjunction implements PlanStepCondition
{
   private List<PlanStepCondition> conditions = new ArrayList<PlanStepCondition>();

   public PlanConditionConjunction(PlanStepCondition condition)
   {
      this.and(condition);
   }

   public PlanConditionConjunction and(PlanStepCondition condition)
   {
      Validate.notNull(condition);
      this.conditions.add(condition);
      return this;
   }

   public boolean evaluate()
   {
      for (PlanStepCondition condition : this.conditions)
         if (!condition.evaluate()) return false;

      return true;
   }
}
