package org.openbbs.example.order.model;

import java.io.Serializable;

/**
 * @author sks
 */
public class Product implements Serializable
{
   private String code;
   private String description;
   
   public Product() {
      return;
   }
   
   public Product(String code, String description) {
      this.code = code;
      this.description = description;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public String getCode() {
      return code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   private static final long serialVersionUID = -246443902310663029L;
}
