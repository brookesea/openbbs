/*
 * AllZonesSelector.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * @author stefan
 */
public class AllZonesSelector implements ZoneSelector
{
   public boolean selects(Zone zone) {
      return true;
   }
}
