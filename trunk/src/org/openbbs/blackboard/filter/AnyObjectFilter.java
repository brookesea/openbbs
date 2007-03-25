/*
 * AnyObjectFilter.java
 *
 * Copyright (C) Dec 19, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.filter;

import org.openbbs.blackboard.EntryFilter;

/**
 * @author stefan
 */
public class AnyObjectFilter implements EntryFilter
{
   public boolean selects(Object entry) {
      return true;
   }
}
