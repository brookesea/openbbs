/*
 * EqualObjectFilter.java
 *
 * Copyright (C) Jan 1, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.filter;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;

/**
 * @author stefan
 */
public class EqualObjectFilter implements EntryFilter
{
   private Object object = null;

   public EqualObjectFilter(Object object) {
      Validate.notNull(object);
      this.object = object;
   }

   public boolean selects(Object entry) {
      return entry.equals(this.object);
   }
}
