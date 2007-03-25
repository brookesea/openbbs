/*
 * PlanStep.java
 *
 * Copyright (C) Jan 11, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.plan;

/**
 * @author stefan
 */
public interface PlanStep
{
   /**
    * Returns the name of this step.
    */
   public String getName();

   /**
    * Returns true if the control should terminate when
    * this step is executed.
    */
   public boolean terminates();
}
