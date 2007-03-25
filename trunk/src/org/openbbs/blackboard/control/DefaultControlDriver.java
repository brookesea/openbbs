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
import org.openbbs.blackboard.BlackboardObserver;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;

/**
 * Observes a Blackboard and runs the registered controls as
 * long as at least one control responds to nextRun with true.
 *
 * @author stefan
 */
public class DefaultControlDriver extends BasicControlDriver implements BlackboardObserver
{
   public DefaultControlDriver() {
      return;
   }

   public DefaultControlDriver(Blackboard blackboard) {
      this.setBlackboard(blackboard);
   }

   public void setBlackboard(Blackboard blackboard) {
      Validate.notNull(blackboard);
      blackboard.registerInterest(ZoneSelector.ALL_ZONES, this);
   }

   public void blackboardDidAddEntry(Blackboard blackboard, Zone zone, Object entry) {
      if (!this.isRunning()) this.runLoop();
   }

   public void blackboardDidRemoveEntry(Blackboard blackboard, Zone zone, Object entry) {
      if (!this.isRunning()) this.runLoop();
   }
}
