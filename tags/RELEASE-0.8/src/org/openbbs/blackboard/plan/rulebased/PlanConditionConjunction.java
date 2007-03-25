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

/**
 * @author stefan
 */
public class PlanConditionConjunction implements PlanStepCondition
{
   private List<PlanStepCondition> conditions = new ArrayList<PlanStepCondition>();

   public PlanConditionConjunction(PlanStepCondition condition) {
      this.and(condition);
   }

   public PlanConditionConjunction and(PlanStepCondition condition) {
      Validate.notNull(condition);
      this.conditions.add(condition);
      return this;
   }

   public boolean evaluate() {
      for (PlanStepCondition condition : this.conditions)
         if (!condition.evaluate()) return false;

      return true;
   }
}
