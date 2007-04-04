package org.openbbs.example.order.wf;

import org.openbbs.blackboard.filter.KindOfFilter;
import org.openbbs.blackboard.filter.TemplateFilter;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.plan.PlanStep;
import org.openbbs.blackboard.plan.rulebased.PlanStepCondition;
import org.openbbs.blackboard.plan.rulebased.RuleBasedControlPlan;
import org.openbbs.blackboard.plan.rulebased.RuleBasedPlanStep;
import org.openbbs.example.order.model.Order;
import org.openbbs.example.order.model.OrderItem;
import org.openbbs.example.order.model.SatisfiedOrderItem;

/**
 * Defines the steps to be carried out when an order
 * is processed.
 *
 * @author sks
 */
public class OrderProcessingPlan extends RuleBasedControlPlan
{
   public final PlanStep PREPROCESS_ORDER_STEP = new RuleBasedPlanStep("PreprocessOrder").always();
   public final PlanStep ALLOCATE_ITEMS_STEP = new RuleBasedPlanStep("AllocateItems").once();
   public final PlanStep SCHEDULE_DELIVERY_STEP = new RuleBasedPlanStep("ProcessOrder").once().when(new OrderSatisfiedRule()).terminates(true);

   public OrderProcessingPlan() {
      this.step(PREPROCESS_ORDER_STEP);
      this.step(ALLOCATE_ITEMS_STEP);
      this.step(SCHEDULE_DELIVERY_STEP);
   }

   private class OrderSatisfiedRule implements PlanStepCondition
   {
      public boolean evaluate(PlanStep step, KSExecutionContext context) {
         Order order = (Order) context.blackboard().read(new KindOfFilter(Order.class));
         for (OrderItem item : order.getItems()) {
            if (!context.blackboard().exists(new TemplateFilter(new SatisfiedOrderItem(item)))) {
               return false;
            }
         }
         return true;
      }
   }
}
