/*
 * ExactZoneSelector.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import org.apache.commons.lang.Validate;

/**
 * Selects exactly one zone.
 *
 * @author stefan
 */
public class ExactZoneSelector implements ZoneSelector
{
   private Zone zone = null;

   public ExactZoneSelector(Zone zone)
   {
      Validate.notNull(zone);
      this.zone = zone;
   }

   public boolean selects(Zone zone)
   {
      return this.zone.equals(zone);
   }
}
