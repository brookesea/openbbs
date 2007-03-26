package org.openbbs.example.order.model;

import java.io.Serializable;

/**
 * @author sks
 */
public class OrderItem implements Serializable
{
   private int quantity;
   private Product product;

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
}
