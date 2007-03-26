package org.openbbs.example.order.wf.ks;

import java.util.Random;

import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.example.order.model.Order;

/**
 * Assigns a unique number to an incoming Order.
 *
 * @author sks
 */
public class AssignOrderNumberKS implements KnowledgeSource
{
   private static int counter = new Random().nextInt(32);
   
   public void execute(KSExecutionContext context) {
      Order order = (Order)context.blackboard().take(new OrderWithoutNumberFilter());
      order.setNumber("ORDER" + order.getCustomer().getNumber() + Integer.toString(counter++));
      context.blackboard().write(order);
   }

   /**
    * The KnowledgeSource is enabled if it finds an order
    * without a number.
    */
   public boolean isEnabled(KSExecutionContext context) {
      return context.blackboard().exists(new OrderWithoutNumberFilter());
   }
   
   private class OrderWithoutNumberFilter implements EntryFilter
   {
      public boolean selects(Object entry) {
         return ((entry instanceof Order) && ((Order)entry).getNumber() == null);
      }
   }
}
