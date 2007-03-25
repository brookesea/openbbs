package org.openbbs.blackboard.plan;

import java.util.List;

/**
 * A control asks a control plan which steps to executed next.
 * 
 * @author stefan
 */
public interface ControlPlan
{
   /**
    * Get a descriptive name for the plan.
    */
   public String getName();

   /**
    * Get the steps which are possible at the moment.
    */
   public List<PlanStep> getPossibleSteps();
}
