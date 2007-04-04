package org.openbbs.example.order.model;

import java.io.Serializable;

/**
 * @author sks
 */
public class SatisfiedOrderItem implements Serializable
{
   private final OrderItem orderItem;
   
   public SatisfiedOrderItem(OrderItem orderItem) {
      this.orderItem = orderItem;
   }
   
   public OrderItem getOrderItem() {
      return this.orderItem;
   }
}
