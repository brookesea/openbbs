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

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.blackboard.plan.KSSelectionStrategy;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * For any step return always the same sources. While useless
 * in production systems this strategy is intended to support
 * testing.
 * <p />
 * This strategy will raise an error if getSources is called
 * and no KnowledgeSources were added.
 * 
 * @author sks
 */
public class AlwaysSameSourcesStrategy implements KSSelectionStrategy
{
   private List<KnowledgeSource> sources = new ArrayList<KnowledgeSource>();

   public AlwaysSameSourcesStrategy addKnowledgeSource(KnowledgeSource source) {
      Validate.notNull(source);
      this.sources.add(source);
      return this;
   }

   public List<KnowledgeSource> getKnowledgeSources(PlanStep step) {
      Validate.isTrue(!this.sources.isEmpty());
      return this.sources;
   }
}
