package org.openbbs.example.order.wf.ks;

import org.openbbs.blackboard.filter.KindOfFilter;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.example.order.model.Order;

/**
 * @author sks
 */
public class ScheduleDeliveryKS implements KnowledgeSource
{
   public void execute(KSExecutionContext context) {
   }

   public boolean isEnabled(KSExecutionContext context) {
      return context.blackboard().exists(new KindOfFilter(Order.class));
   }
}
