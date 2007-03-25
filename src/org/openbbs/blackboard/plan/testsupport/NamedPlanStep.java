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

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.plan.PlanStep;

public class NamedPlanStep implements PlanStep
{
   private final String name;
   private boolean terminates = false;

   public NamedPlanStep(String name) {
      Validate.notNull(name);
      this.name = name;
   }

   public NamedPlanStep terminates(boolean terminates) {
      this.terminates = terminates;
      return this;
   }

   public String getName() {
      return this.name;
   }

   public boolean terminates() {
      return this.terminates;
   }
}
