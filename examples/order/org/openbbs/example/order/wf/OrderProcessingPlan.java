package org.openbbs.example.order.wf;

import org.openbbs.blackboard.plan.PlanStep;
import org.openbbs.blackboard.plan.rulebased.RuleBasedControlPlan;
import org.openbbs.blackboard.plan.rulebased.RuleBasedPlanStep;

/**
 * Defines the steps to be carried out when an order
 * is processed.
 *
 * @author sks
 */
public class OrderProcessingPlan extends RuleBasedControlPlan
{
   public final PlanStep PREPROCESS_ORDER_STEP = new RuleBasedPlanStep("PreprocessOrder").always();

   public OrderProcessingPlan() {
      this.step(PREPROCESS_ORDER_STEP);
   }
}
