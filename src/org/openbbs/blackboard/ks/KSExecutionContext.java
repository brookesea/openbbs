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
package org.openbbs.blackboard.ks;

import org.openbbs.blackboard.BlackboardAccess;
import org.openbbs.blackboard.Zone;

/**
 * Provides the context for selecting and executing a KnowledgeSource.
 * 
 * @author stefan
 */
public interface KSExecutionContext
{
   /**
    * Get the default access to the blackboard. Access is restricted
    * to the zone in which the KnowledgeSource is meant to work.
    */
   public BlackboardAccess blackboard();

   /**
    * Get access to a particular zone on the blackboard. This method
    * fails if access to the zone is prohibited in this context.
    * 
    * @param zone  a non-null zone.
    */
   public BlackboardAccess blackboard(Zone zone);
}
