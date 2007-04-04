package org.openbbs.example.order.wf.ks;

import org.openbbs.blackboard.ReadBlackboardException;
import org.openbbs.blackboard.filter.KindOfFilter;
import org.openbbs.blackboard.filter.TemplateFilter;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.example.order.Zones;
import org.openbbs.example.order.model.Order;
import org.openbbs.example.order.model.OrderItem;
import org.openbbs.example.order.model.ProductStock;
import org.openbbs.example.order.model.ProductsNotAvailableException;
import org.openbbs.example.order.model.SatisfiedOrderItem;
import org.openbbs.example.order.model.UnsatisfiedOrderItem;

/**
 * Checks the availability of ordered items in stock.
 *
 * @author sks
 */
public class AllocatedOrderedItemsKS implements KnowledgeSource
{
   public void execute(KSExecutionContext context) {
      Order order = (Order) context.blackboard().read(new KindOfFilter(Order.class));
      for (OrderItem item : order.getItems()) {
         ProductStock quantity = null;
         try {
            quantity = (ProductStock) context.blackboard(Zones.STOCK).take(
                  new TemplateFilter(new ProductStock(item.getProduct().getCode(), -1)));
         }
         catch (ReadBlackboardException exc) {
            context.blackboard().write(new UnsatisfiedOrderItem(item, "unknown product"));
            return;
         }

         try {
            quantity.retain(item.getQuantity());
            context.blackboard().write(new SatisfiedOrderItem(item));
         }
         catch (ProductsNotAvailableException exc) {
            context.blackboard().write(new UnsatisfiedOrderItem(item, "not enough items in stock"));
         }
         finally {
            context.blackboard(Zones.STOCK).write(quantity);
         }
      }
   }

   public boolean isEnabled(KSExecutionContext context) {
      return context.blackboard().exists(new KindOfFilter(Order.class));
   }
}
