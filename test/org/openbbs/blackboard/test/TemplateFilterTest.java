package org.openbbs.blackboard.test;

import java.beans.BeanDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.SimpleBeanInfo;
import java.util.Collection;

import junit.framework.TestCase;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.DefaultZoneSelector;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.TemplateFilter;

/**
 * @author sks
 */
public class TemplateFilterTest extends TestCase
{
   private Blackboard bb;
   private Order order1;
   private Order order2;
   private Order order3;

   protected void setUp() throws Exception {
      this.bb = new ObjectBlackboard();
      this.order1 = new Order("otst01", 10, "Cat");
      this.order2 = new Order("otst02", 5, "Dog");
      this.order3 = new Order("otst03", 10, "Dog");
      this.bb.write(Zone.DEFAULT, this.order1);
      this.bb.write(Zone.DEFAULT, this.order2);
      this.bb.write(Zone.DEFAULT, this.order3);
   }

   public void testFilterOrdersByNumber() throws Exception {
      Order template = new Order();
      template.setNumber(this.order1.getNumber());
      Collection<Object> result = this.bb.readAll(new DefaultZoneSelector(), new TemplateFilter(template));
      assertEquals(1, result.size());
      assertEquals(this.order1, result.iterator().next());
   }

   public void testFilterOrdersByProduct() throws Exception {
      Order template = new Order();
      template.setProduct("Dog");
      Collection<Object> result = this.bb.readAll(new DefaultZoneSelector(), new TemplateFilter(template));
      System.err.println(result.iterator().next());
      assertEquals(2, result.size());
      assertTrue(result.contains(this.order2));
      assertTrue(result.contains(this.order3));
   }

   public void testFilterOrdersByQuantity() throws Exception {
      Order template = new Order();
      template.setQuantity(10);
      Collection<Object> result = this.bb.readAll(new DefaultZoneSelector(), new TemplateFilter(template));
      assertEquals(2, result.size());
      assertTrue(result.contains(this.order1));
      assertTrue(result.contains(this.order3));
   }

   public void testFilterOrderByQuantityProduct() throws Exception {
      Order template = new Order();
      template.setProduct("Cat");
      template.setQuantity(10);
      Collection<Object> result = this.bb.readAll(new DefaultZoneSelector(), new TemplateFilter(template));
      assertEquals(1, result.size());
      assertTrue(result.contains(this.order1));
   }

   public void testFilterWithoutMatches() throws Exception {
      Order template = new Order();
      template.setProduct("Foo");
      Collection<Object> result = this.bb.readAll(new DefaultZoneSelector(), new TemplateFilter(template));
      assertEquals(0, result.size());
   }

   public static class Order implements Cloneable
   {
      private String number;
      private String product;
      private int quantity = -1;

      public Order() {
         return;
      }

      public Order(String number, int quantity, String product) {
         this.number = number;
         this.product = product;
         this.quantity = quantity;
      }

      public String getNumber() {
         return number;
      }

      public void setNumber(String number) {
         this.number = number;
      }

      public String getProduct() {
         return product;
      }

      public void setProduct(String product) {
         this.product = product;
      }

      public int getQuantity() {
         return quantity;
      }

      public void setQuantity(int quantity) {
         this.quantity = quantity;
      }

      public Object clone() {
         return new Order(this.number, this.quantity, this.product);
      }

      public boolean equals(Object o) {
         if (!(o instanceof Order)) {
            return false;
         }

         Order _o = (Order) o;
         return new EqualsBuilder().append(this.number, _o.number).append(this.product, _o.product).append(
               this.quantity, _o.quantity).isEquals();
      }

      public int hashCode() {
         return new HashCodeBuilder().append(this.number).append(this.product).append(this.quantity).toHashCode();
      }

      public String toString() {
         return ToStringBuilder.reflectionToString(this).toString();
      }
   }

   public static class OrderBeanInfo extends SimpleBeanInfo
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
            PropertyDescriptor number = new PropertyDescriptor("number", Order.class);
            PropertyDescriptor quantity = new PropertyDescriptor("quantity", Order.class);
            quantity.setValue("defaultValue", -1);
            PropertyDescriptor product = new PropertyDescriptor("product", Order.class);
            return new PropertyDescriptor[] { number, quantity, product };
         }
         catch (Exception exc) {
            throw new RuntimeException("failed to create property descriptors for Order class", exc);
         }
      }
   }
}
