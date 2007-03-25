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
package org.openbbs.blackboard.plan;

import java.util.Collection;

import org.openbbs.blackboard.control.BlackboardControl;
import org.openbbs.blackboard.ks.KnowledgeSource;

/**
 * @author sks
 */
public interface KSSelectionStrategy
{
   /**
    * Get the sources for a given {@link PlanStep}. The order in which
    * the KnowledgeSources are returned matters. A {@link BlackboardControl}
    * will execute the first enabled KnowledgeSource from the list, starting
    * with the first.
    */
   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step);
}
