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
package org.openbbs.blackboard.plan.testsupport;

import java.util.ArrayList;
import java.util.List;

import org.openbbs.blackboard.ks.KSExecutionContext;
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
   public String getName() {
      return "OneStepPlan";
   }

   public List<PlanStep> getPossibleSteps(KSExecutionContext context) {
      List<PlanStep> steps = new ArrayList<PlanStep>();
      steps.add(new NamedPlanStep("UNIVERSAL_STEP"));
      return steps;
   }
}
