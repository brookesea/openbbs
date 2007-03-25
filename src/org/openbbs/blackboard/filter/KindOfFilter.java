/*
 * KindOfFilter.java
 *
 * Copyright (C) Jan 10, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.filter;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;

/**
 * @author stefan
 */
public class KindOfFilter implements EntryFilter
{
   private Class<? extends Object> clazz = null;

   public KindOfFilter(Class<? extends Object> clazz) {
      Validate.notNull(clazz);
      this.clazz = clazz;
   }

   public boolean selects(Object entry) {
      return this.clazz.isAssignableFrom(entry.getClass());
   }
}
