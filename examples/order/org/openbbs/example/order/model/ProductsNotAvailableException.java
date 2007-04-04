package org.openbbs.example.order.model;

/**
 * Signals that ordered products are currently not available.
 *
 * @author sks
 */
public class ProductsNotAvailableException extends Exception
{
   private final String productCode;
   private final int quantity;

   public ProductsNotAvailableException(String code, int quantity) {
      this.productCode = code;
      this.quantity = quantity;
   }

   public String getProductCode() {
      return productCode;
   }

   public int getQuantity() {
      return quantity;
   }
}
