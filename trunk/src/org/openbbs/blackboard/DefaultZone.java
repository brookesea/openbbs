/*
 * DefaultZone.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * The DefaultZone always exists on a Blackboard.
 *
 * @author stefan
 */
public final class DefaultZone implements Zone
{
   public String name() {
      return "DEFAULT_ZONE";
   }

   public boolean equals(Object obj) {
      return (obj != null && (obj instanceof DefaultZone));
   }

   public int hashCode() {
      return DefaultZone.class.hashCode();
   }

   public String toString() {
      return "the default Zone";
   }

   private static final long serialVersionUID = 8704637445602562456L;
}
