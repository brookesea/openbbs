/*
 * ZoneSelector.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * A ZoneSelector selects one or more Zones on a Blackboard.
 *
 * @author stefan
 */
public interface ZoneSelector
{
   public static final ZoneSelector ALL_ZONES = new AllZonesSelector();

   public boolean selects(Zone zone);
}
