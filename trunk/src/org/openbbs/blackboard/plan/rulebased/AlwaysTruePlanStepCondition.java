/*
 * AlwaysTruePlanCondition.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.plan.rulebased;

/**
 * @autor sks
 */
public class AlwaysTruePlanStepCondition implements PlanStepCondition
{
   public boolean evaluate() {
      return true;
   }
}
