/*
 * OldControlPlan.java
 *
 * Copyright (C) Jan 11, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.plan.rulebased;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.plan.ControlPlan;
import org.openbbs.blackboard.plan.NoStep;
import org.openbbs.blackboard.plan.PlanStep;
import org.openbbs.util.ClassnameHelper;

/**
 * A generic ControlPlan implementation which is based on rules.
 *
 * @author stefan
 */
public class RuleBasedControlPlan implements ControlPlan
{
   private final List<RuleBasedPlanStep> steps = new ArrayList<RuleBasedPlanStep>();
   private final Map<String, RuleBasedPlanStep> stepsByName = new HashMap<String, RuleBasedPlanStep>();
   private String name = null;

   public RuleBasedControlPlan()
   {
      this(null);
   }

   public RuleBasedControlPlan(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      if (this.name == null) this.name = ClassnameHelper.nameWithoutPackage(this.getClass());

      return this.name;
   }

   public RuleBasedPlanStep step(String name)
   {
      Validate.notNull(name);

      if (this.stepsByName.containsKey(name)) return this.stepsByName.get(name);

      return this.step(new RuleBasedPlanStep(name));
   }

   public RuleBasedPlanStep step(RuleBasedPlanStep step)
   {
      Validate.notNull(step);

      if (this.stepsByName.containsKey(step.getName())) {
         throw new IllegalArgumentException("a step named " + step.getName()
                  + " already exists in this plan");
      }

      this.steps.add(step);
      this.stepsByName.put(step.getName(), step);

      return step;
   }

   public void removeStep(PlanStep step)
   {
      Validate.notNull(step);
      if (!this.steps.remove(step))
         throw new IllegalArgumentException(step.toString() + " is not part of this plan");
   }

   public List<PlanStep> getPossibleSteps()
   {
      List<PlanStep> possibleSteps = new ArrayList<PlanStep>();

      for (RuleBasedPlanStep candidate : this.steps)
         if (candidate.isActive()) possibleSteps.add(candidate);

      if (possibleSteps.isEmpty()) possibleSteps.add(NoStep.instance);

      return possibleSteps;
   }
}
