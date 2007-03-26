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
import org.openbbs.example.order.wf.OrderProcessingPlan;
import org.openbbs.example.order.wf.ks.AssignOrderNumberKS;

/**
 * @author sks
 */
public class OrderProcessing
{
   private final Blackboard blackboard;

   public OrderProcessing() {
      this.blackboard = new ObjectBlackboard();
      ((ObjectBlackboard) blackboard).setCloneStrategy(new CloneBySerializationStrategy());

      OrderProcessingPlan plan = new OrderProcessingPlan();

      MultipleKSSelectionStrategy ksSelection = new MultipleKSSelectionStrategy();
      ksSelection.addKnowledgeSource(plan.PREPROCESS_ORDER_STEP, new AssignOrderNumberKS());

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
      CustomerManager customerManager = new CustomerManager();

      OrderProcessing orderProcessing = new OrderProcessing();

      Order order = new Order();
      order.setCustomer(customerManager.getCustomerByNumber("CU02"));
      orderProcessing.processOrder(order);
      orderProcessing.inspect();
   }
}
