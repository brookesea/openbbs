/*
 * NamedZone.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * @author stefan
 */
public class NamedZone implements Zone
{
   private String name = null;

   public NamedZone(String name) {
      Validate.notNull(name);
      this.name = name;
   }

   public String name() {
      return this.name;
   }

   public boolean equals(Object obj) {
      if (obj == null || !(obj instanceof NamedZone)) return false;

      return this.name().equals(((NamedZone)obj).name());
   }

   public int hashCode() {
      return new HashCodeBuilder().append(this.name).append(NamedZone.class).toHashCode();
   }

   public String toString() {
      return "a NamedZone \"" + this.name() + "\"";
   }

   private static final long serialVersionUID = 6286217725870494454L;
}
