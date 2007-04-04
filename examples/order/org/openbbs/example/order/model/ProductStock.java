package org.openbbs.example.order.model;

/**
 * A simple bean that contains number of currently available
 * items for a given Product.
 *
 * @author sks
 */
public class ProductStock
{
   private String productCode = null;
   private int itemsAvailable = -1;

   public ProductStock(String productCode, int initialItems) {
      this.productCode = productCode;
      this.itemsAvailable = initialItems;
   }

   public String getProductCode() {
      return productCode;
   }

   public void setProductCode(String productCode) {
      this.productCode = productCode;
   }

   public int getItemsAvailable() {
      return itemsAvailable;
   }

   public void retain(int numberOfItems) throws ProductsNotAvailableException {
      if (this.itemsAvailable < numberOfItems) {
         throw new ProductsNotAvailableException(this.productCode, numberOfItems);
      }
      this.itemsAvailable -= numberOfItems;
   }
}
