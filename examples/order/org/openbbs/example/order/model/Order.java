package org.openbbs.example.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author sks
 */
public class Order implements Serializable
{
   private Customer customer;
   private String number;
   private List<OrderItem> items = new ArrayList<OrderItem>();

   public Customer getCustomer() {
      return customer;
   }

   public void setCustomer(Customer customer) {
      this.customer = customer;
   }

   public String getNumber() {
      return this.number;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   public List<OrderItem> getItems() {
      return Collections.unmodifiableList(this.items);
   }

   public void addItem(OrderItem item) {
      this.items.add(item);
   }
   
   public String toString() {
      return ToStringBuilder.reflectionToString(this);
   }
}
