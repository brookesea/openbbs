package org.openbbs.blackboard.plan.rulebased;

import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * @author sks
 */
public class OncePlanStepCondition implements PlanStepCondition
{
   public boolean evaluate(PlanStep step, KSExecutionContext context) {
      return false;
   }
}
