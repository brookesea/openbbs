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
package org.openbbs.blackboard.control;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardAccess;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZonedBlackboardAccess;
import org.openbbs.blackboard.ks.KSExecutionContext;

/**
 * KSExecutionContext implementation for execution of KnowledgeSources.
 * 
 * @author stefan
 */
class KSExecutionContextImpl implements KSExecutionContext
{
   private final Blackboard blackboard;
   private final BlackboardAccess defaultAccess;

   public KSExecutionContextImpl(Blackboard blackboard, Zone zone) {
      Validate.notNull(blackboard);
      Validate.notNull(zone);
      this.blackboard = blackboard;
      this.defaultAccess = new ZonedBlackboardAccess(blackboard, zone);
   }

   public BlackboardAccess blackboard() {
      return this.defaultAccess;
   }

   public BlackboardAccess blackboard(Zone zone) {
      Validate.notNull(zone);
      // TODO: security check (access to zone allowed?)
      return new ZonedBlackboardAccess(this.blackboard, zone);
   }
}
