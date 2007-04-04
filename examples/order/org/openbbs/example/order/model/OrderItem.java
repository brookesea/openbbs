package org.openbbs.example.order.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author sks
 */
public class OrderItem implements Serializable
{
   private String id;
   private int quantity;
   private Product product;

   public OrderItem() {
      this(null, -1);
   }

   public OrderItem(Product product, int quantity) {
      this.id = UUID.randomUUID().toString();
      this.product = product;
      this.quantity = quantity;
   }

   public Product getProduct() {
      return product;
   }

   public void setProduct(Product product) {
      this.product = product;
   }

   public int getQuantity() {
      return quantity;
   }

   public void setQuantity(int quantity) {
      this.quantity = quantity;
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public boolean equals(Object o) {
      if (!(o instanceof OrderItem)) {
         return false;
      }
      return this.id.equals(((OrderItem)o).id);
   }
}
