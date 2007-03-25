/*
 * NoStep.java
 *
 * Copyright (C) Jan 11, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.plan;

/**
 * @author stefan
 */
public final class NoStep implements PlanStep
{
   public final static NoStep instance = new NoStep();

   private NoStep() {
   }

   public String getName() {
      return "NO_STEP";
   }

   public boolean terminates() {
      return false;
   }

   public boolean equals(Object obj) {
      return obj != null && (obj instanceof NoStep);
   }

   public int hashCode() {
      return NoStep.class.hashCode();
   }
}
