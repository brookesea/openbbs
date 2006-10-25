package org.openbbs.blackboard.plan.testsupport;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.plan.PlanStep;

public class NamedPlanStep implements PlanStep
{
   private final String name;
   
   public NamedPlanStep(String name)
   {
      Validate.notNull(name);
      this.name = name;
   }

   public String getName()
   {
      return this.name;
   }

   public boolean terminates()
   {
      return false;
   }
}
