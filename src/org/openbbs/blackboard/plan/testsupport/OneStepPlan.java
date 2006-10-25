/*
 * OneStepPlan.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.plan.testsupport;

import java.util.ArrayList;
import java.util.List;

import org.openbbs.blackboard.plan.ControlPlan;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * Builds a plan that consists of one, universal step that
 * is always possible (it's condition always evaulates to
 * true). Such a plan can be useful for testing purposes.
 * 
 * @autor sks
 */
public final class OneStepPlan implements ControlPlan
{
   public String getName()
   {
      return "OneStepPlan";
   }
   
   public List<PlanStep> getPossibleSteps()
   {
      List<PlanStep> steps = new ArrayList<PlanStep>();
      steps.add(new NamedPlanStep("UNIVERSAL_STEP"));
      return steps;
   }
}
