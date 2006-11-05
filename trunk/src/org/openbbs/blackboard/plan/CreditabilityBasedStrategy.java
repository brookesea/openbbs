/*
 * CreditabilityBasedStrategy.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
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

   public void addKnowledgeSource(PlanStep step, KnowledgeSource ks, int creditability)
   {
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

   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step)
   {
      Validate.notNull(step);

      if (!this.sources.containsKey(step)) {
         return Collections.emptyList();
      }

      return Collections.unmodifiableList(this.sources.get(step));
   }

   private void _setCreditability(PlanStep step, KnowledgeSource ks, int creditability)
   {
      Map<KnowledgeSource, Integer> creditabilitiesForStep = this.creditabilities.get(step);
      if (creditabilitiesForStep == null) {
         creditabilitiesForStep = new HashMap<KnowledgeSource, Integer>();
         this.creditabilities.put(step, creditabilitiesForStep);
      }
      creditabilitiesForStep.put(ks, creditability);
   }

   private Integer getCreditability(PlanStep step, KnowledgeSource ks)
   {
      Map<KnowledgeSource, Integer> creditabilitiesForStep = this.creditabilities.get(step);
      if (creditabilitiesForStep == null) {
         return null;
      }
      return creditabilitiesForStep.get(ks);
   }

   private void sortKSByCreditability(PlanStep step, List<KnowledgeSource> ksList)
   {
      final PlanStep _step = step;
      Collections.sort(ksList, new Comparator<KnowledgeSource>() {
         public int compare(KnowledgeSource ks1, KnowledgeSource ks2)
         {
            return getCreditability(_step, ks2).compareTo(getCreditability(_step, ks1));
         }
      });
   }
}
