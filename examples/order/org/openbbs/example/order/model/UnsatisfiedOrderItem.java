package org.openbbs.example.order.model;

import java.io.Serializable;

/**
 * @author sks
 */
public class UnsatisfiedOrderItem implements Serializable
{
   private final OrderItem orderItem;
   private final String reason;

   public UnsatisfiedOrderItem(OrderItem orderItem) {
      this(orderItem, null);
   }

   public UnsatisfiedOrderItem(OrderItem orderItem, String reason) {
      this.orderItem = orderItem;
      this.reason = reason;
   }

   public OrderItem getOrderItem() {
      return this.orderItem;
   }
   
   public String getReason() {
      return this.reason;
   }
}
