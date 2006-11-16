/*
 * Zone.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import java.io.Serializable;

/**
 * Identifies a zone on a Blackboard.
 *
 * @author stefan
 */
public interface Zone extends Serializable
{
   public static final Zone DEFAULT = new DefaultZone();

   public String name();
}
