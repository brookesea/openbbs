package org.openbbs.blackboard.plan.testsupport;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.plan.PlanStep;

public class NamedPlanStep implements PlanStep
{
   private final String name;
   private boolean terminates = false;

   public NamedPlanStep(String name) {
      Validate.notNull(name);
      this.name = name;
   }

   public NamedPlanStep terminates(boolean terminates) {
      this.terminates = terminates;
      return this;
   }

   public String getName() {
      return this.name;
   }

   public boolean terminates() {
      return this.terminates;
   }
}
