/*
 * Zone.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * Identifies a zone on a Blackboard.
 *
 * @author stefan
 */
public interface Zone
{
   public static final Zone DEFAULT = new DefaultZone();

   public String name();
}
