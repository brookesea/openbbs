package org.openbbs.example.order;

import org.openbbs.blackboard.AllZonesSelector;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.CloneBySerializationStrategy;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.control.BlackboardControl;
import org.openbbs.blackboard.control.DefaultControlDriver;
import org.openbbs.blackboard.filter.AnyObjectFilter;
import org.openbbs.blackboard.plan.MultipleKSSelectionStrategy;
import org.openbbs.example.order.model.CustomerManager;
import org.openbbs.example.order.model.Order;
import org.openbbs.example.order.model.OrderItem;
import org.openbbs.example.order.model.Product;
import org.openbbs.example.order.wf.OrderProcessingPlan;
import org.openbbs.example.order.wf.ks.AllocatedOrderedItemsKS;
import org.openbbs.example.order.wf.ks.AssignOrderNumberKS;
import org.openbbs.example.order.wf.ks.ScheduleDeliveryKS;

/**
 * @author sks
 */
public class OrderProcessing
{
   private final Blackboard blackboard;

   public OrderProcessing(Blackboard blackboard) {
      this.blackboard = blackboard;
      OrderProcessingPlan plan = new OrderProcessingPlan();

      MultipleKSSelectionStrategy ksSelection = new MultipleKSSelectionStrategy();
      ksSelection.addKnowledgeSource(plan.PREPROCESS_ORDER_STEP, new AssignOrderNumberKS());
      ksSelection.addKnowledgeSource(plan.ALLOCATE_ITEMS_STEP, new AllocatedOrderedItemsKS());
      ksSelection.addKnowledgeSource(plan.SCHEDULE_DELIVERY_STEP, new ScheduleDeliveryKS());

      BlackboardControl control = new BlackboardControl();
      control.setZone(Zone.DEFAULT);
      control.setBlackboard(this.blackboard);
      control.setPlan(plan);
      control.setKSSelectStrategy(ksSelection);
      control.setDriver(new DefaultControlDriver(this.blackboard));
   }

   public void processOrder(Order order) {
      this.blackboard.write(Zone.DEFAULT, order);
   }

   public void inspect() {
      for (Object entry : this.blackboard.readAll(new AllZonesSelector(), new AnyObjectFilter())) {
         System.out.println(entry);
      }
   }

   public static void main(String args[]) {
      ObjectBlackboard blackboard = new ObjectBlackboard();
      blackboard.setCloneStrategy(new CloneBySerializationStrategy());
      blackboard.openZone(Zones.STOCK);
      
      Product pr01 = new Product("PR01", "White Cat");
      Product pr02 = new Product("PR02", "Black Cat");
      Product pr03 = new Product("PR03", "Small Dog");
      Product pr04 = new Product("PR04", "Big Dog");
      
      CustomerManager customerManager = new CustomerManager();
      OrderProcessing orderProcessing = new OrderProcessing(blackboard);
      Order order = new Order();
      order.setCustomer(customerManager.getCustomerByNumber("CU02"));
      order.addItem(new OrderItem(pr01, 5));
      order.addItem(new OrderItem(pr02, 6));
      order.addItem(new OrderItem(pr03, 1));
      order.addItem(new OrderItem(pr04, 2));
      orderProcessing.processOrder(order);
      orderProcessing.inspect();
   }
}
