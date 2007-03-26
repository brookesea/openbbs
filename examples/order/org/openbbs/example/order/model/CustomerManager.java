package org.openbbs.example.order.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author sks
 */
public class CustomerManager
{
   private Map<String, Customer> customers = new HashMap<String, Customer>();
   
   public CustomerManager()
   {
      this.loadDefaultCustomers();
   }
   
   public void addCustomer(Customer customer)
   {
      this.customers.put(customer.getNumber(), customer);
   }
   
   public Customer getCustomerByNumber(String number)
   {
      return this.customers.get(number);
   }
   
   private void loadDefaultCustomers()
   {
      this.addCustomer(new Customer("CU01", "Ford Prefect"));
      this.addCustomer(new Customer("CU02", "Arthur Dent"));
      this.addCustomer(new Customer("CU03", "Zaphod Beeblebrox"));
      this.addCustomer(new Customer("CU04", "Tricia McMillian"));
   }
}
