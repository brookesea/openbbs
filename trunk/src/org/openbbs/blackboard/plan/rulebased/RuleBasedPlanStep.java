package org.openbbs.blackboard.plan.rulebased;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * 
 * @author stefan
 */
public class RuleBasedPlanStep implements PlanStep
{
   private final List<PlanStepCondition> conditions = new ArrayList<PlanStepCondition>();
   private final String name;
   private boolean isTerminationStep = false;

   public RuleBasedPlanStep(String name) {
      Validate.notNull(name);
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public RuleBasedPlanStep when(PlanStepCondition condition) {
      Validate.notNull(condition);
      this.conditions.add(condition);
      return this;
   }

   public RuleBasedPlanStep always() {
      return this.when(new AlwaysTruePlanStepCondition());
   }

   public RuleBasedPlanStep or(PlanStepCondition condition) {
      return this.when(condition);
   }

   public RuleBasedPlanStep and(PlanStepCondition condition1, PlanStepCondition condition2) {
      return this.when(new PlanConditionConjunction(condition1).and(condition2));
   }

   public RuleBasedPlanStep terminates(boolean value) {
      this.isTerminationStep = value;
      return this;
   }

   public boolean terminates() {
      return this.isTerminationStep;
   }

   public boolean isActive() {
      for (PlanStepCondition condition : this.conditions)
         if (condition.evaluate()) return true;

      return false;
   }

   public boolean equals(Object obj) {
      if (obj != null || !(obj instanceof PlanStep)) return false;
      return this.getName().equals(((PlanStep)obj).getName());
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public String toString() {
      return "a RuleBasedPlanStep \"" + this.name + "\"";
   }
}
