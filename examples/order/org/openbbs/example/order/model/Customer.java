package org.openbbs.example.order.model;

import java.io.Serializable;

/**
 * @author sks
 */
public class Customer implements Serializable
{
   private String number;
   private String name;
      
   public Customer()
   {
      return;
   }
   
   public Customer(String number, String name)
   {
      this.setNumber(number);
      this.setName(name);
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getNumber() {
      return number;
   }

   public void setNumber(String number) {
      this.number = number;
   }

   private static final long serialVersionUID = 3766184698523038777L;
}
