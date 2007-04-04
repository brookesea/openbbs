package org.openbbs.example.order.model;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;

/**
 * @author sks
 */
public class ProductStockBeanInfo extends SimpleBeanInfo
{
   private final BeanDescriptor beanDescriptor = new BeanDescriptor(Order.class);
   private PropertyDescriptor propertyDescriptors[] = null;

   public BeanDescriptor getBeanDescriptor() {
      return this.beanDescriptor;
   }

   public PropertyDescriptor[] getPropertyDescriptors() {
      if (this.propertyDescriptors == null) {
         this.propertyDescriptors = this.buildPropertyDescriptors();
      }
      return this.propertyDescriptors;
   }

   private PropertyDescriptor[] buildPropertyDescriptors() {
      try {
         PropertyDescriptor productCode = new PropertyDescriptor("productCode", ProductStock.class);
         PropertyDescriptor itemsAvailable = new PropertyDescriptor("itemsAvailable", ProductStock.class, "getItemsAvailable", null);
         itemsAvailable.setValue("defaultValue", new Integer(-1));
         return new PropertyDescriptor[] { productCode, itemsAvailable };
      }
      catch (Exception exc) {
         throw new RuntimeException("failed to create property descriptors for ProductStock class", exc);
      }
   }
}
