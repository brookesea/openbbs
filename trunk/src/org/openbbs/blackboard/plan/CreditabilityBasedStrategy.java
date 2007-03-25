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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.ks.KnowledgeSource;

/**
 * @author sks
 */
public class CreditabilityBasedStrategy implements KSSelectionStrategy
{
   private final Map<PlanStep, Map<KnowledgeSource, Integer>> creditabilities = new HashMap<PlanStep, Map<KnowledgeSource, Integer>>();
   private final Map<PlanStep, List<KnowledgeSource>> sources = new HashMap<PlanStep, List<KnowledgeSource>>();

   public void addKnowledgeSource(PlanStep step, KnowledgeSource ks, int creditability) {
      Validate.notNull(step);
      Validate.notNull(ks);
      Validate.isTrue(!step.equals(NoStep.instance), "cannot add a \"no step \" to this strategy");

      List<KnowledgeSource> ksForStep = sources.get(step);
      if (ksForStep == null) {
         ksForStep = new ArrayList<KnowledgeSource>();
         this.sources.put(step, ksForStep);
      }
      ksForStep.add(ks);

      this._setCreditability(step, ks, creditability);
      this.sortKSByCreditability(step, ksForStep);
   }

   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step) {
      Validate.notNull(step);

      if (!this.sources.containsKey(step)) {
         return Collections.emptyList();
      }

      return Collections.unmodifiableList(this.sources.get(step));
   }

   private void _setCreditability(PlanStep step, KnowledgeSource ks, int creditability) {
      Map<KnowledgeSource, Integer> creditabilitiesForStep = this.creditabilities.get(step);
      if (creditabilitiesForStep == null) {
         creditabilitiesForStep = new HashMap<KnowledgeSource, Integer>();
         this.creditabilities.put(step, creditabilitiesForStep);
      }
      creditabilitiesForStep.put(ks, creditability);
   }

   private Integer getCreditability(PlanStep step, KnowledgeSource ks) {
      Map<KnowledgeSource, Integer> creditabilitiesForStep = this.creditabilities.get(step);
      if (creditabilitiesForStep == null) {
         return null;
      }
      return creditabilitiesForStep.get(ks);
   }

   private void sortKSByCreditability(PlanStep step, List<KnowledgeSource> ksList) {
      final PlanStep _step = step;
      Collections.sort(ksList, new Comparator<KnowledgeSource>() {
         public int compare(KnowledgeSource ks1, KnowledgeSource ks2) {
            return getCreditability(_step, ks2).compareTo(getCreditability(_step, ks1));
         }
      });
   }
}
