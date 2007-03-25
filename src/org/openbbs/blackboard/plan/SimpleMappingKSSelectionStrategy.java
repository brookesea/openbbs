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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.ks.KnowledgeSource;

/**
 * A very simple KSSelectionStrategy which is based on a mapping
 * table. A KnowledgeSource is associated directly to a PlanStep.
 * 
 * @author stefan
 */
public class SimpleMappingKSSelectionStrategy implements KSSelectionStrategy
{
   private Map<String, KnowledgeSource> mappings = new HashMap<String, KnowledgeSource>();

   public void createMapping(PlanStep step, KnowledgeSource knowledgeSource) {
      Validate.notNull(step);
      Validate.notNull(knowledgeSource);
      mappings.put(step.getName(), knowledgeSource);
   }

   public void setMappings(Map<String, KnowledgeSource> mappings) {
      Validate.notNull(mappings);
      this.mappings = mappings;
   }

   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step) {
      Validate.notNull(step);

      KnowledgeSource ks = this.mappings.get(step.getName());
      if (ks == null) {
         return Collections.emptyList();
      }

      List<KnowledgeSource> ksList = new ArrayList<KnowledgeSource>();
      ksList.add(ks);
      return ksList;
   }
}
