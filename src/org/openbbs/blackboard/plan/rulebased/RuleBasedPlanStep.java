/*
 *  Copyright [2006-2007] [Stefan Kleine Stegemann]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbbs.blackboard.plan.rulebased;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * 
 * @author stefan
 */
public class RuleBasedPlanStep implements PlanStep
{
   private final List<PlanStepCondition> conditions = new ArrayList<PlanStepCondition>();
   private final String name;
   private boolean isTerminationStep = false;

   public RuleBasedPlanStep(String name) {
      Validate.notNull(name);
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public RuleBasedPlanStep when(PlanStepCondition condition) {
      Validate.notNull(condition);
      this.conditions.add(condition);
      return this;
   }

   public RuleBasedPlanStep always() {
      return this.when(new AlwaysTruePlanStepCondition());
   }
   
   public RuleBasedPlanStep once() {
      return this.when(new OncePlanStepCondition());
   }

   public RuleBasedPlanStep or(PlanStepCondition condition) {
      return this.when(condition);
   }

   public RuleBasedPlanStep and(PlanStepCondition condition1, PlanStepCondition condition2) {
      return this.when(new PlanConditionConjunction(condition1).and(condition2));
   }

   public RuleBasedPlanStep terminates(boolean value) {
      this.isTerminationStep = value;
      return this;
   }

   public boolean terminates() {
      return this.isTerminationStep;
   }

   public boolean isActiveInContext(KSExecutionContext context) {
      for (PlanStepCondition condition : this.conditions)
         if (condition.evaluate(this, context)) return true;

      return false;
   }

   public boolean equals(Object obj) {
      if (obj != null || !(obj instanceof PlanStep)) return false;
      return this.getName().equals(((PlanStep)obj).getName());
   }

   public int hashCode() {
      return this.getName().hashCode();
   }

   public String toString() {
      return "a RuleBasedPlanStep \"" + this.name + "\"";
   }
}
