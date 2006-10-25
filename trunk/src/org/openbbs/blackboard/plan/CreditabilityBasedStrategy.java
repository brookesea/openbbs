/*
 * CreditabilityBasedStrategy.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.KnowledgeSource;

/**
 * A KnowledgeSource strategy which assigns a creditability for
 * a KnowledgeSource and a PlanStep. When the best source has to
 * be selected from a list of sources for a given PlanStep, the
 * source with the highest creditability for this step is selected.
 * <p />
 * Note that KnowledgeSources with a creditability lower or equal
 * than 0 or no creditability for a step are never considered when
 * selecting a source.
 *
 * @author sks
 */
public class CreditabilityBasedStrategy implements KnowledgeSourceSelectionStrategy
{
   private final Map<PlanStep, Map<KnowledgeSource, Integer>> creditabilities = new HashMap<PlanStep, Map<KnowledgeSource, Integer>>();

   public void setCreditability(PlanStep step, KnowledgeSource source, int creditability)
   {
      Validate.notNull(step);
      Validate.notNull(source);

      if (step.equals(NoStep.instance))
         throw new IllegalArgumentException("cannot assign a source a creditability for " + step);

      Map<KnowledgeSource, Integer> stepCreditabilities = this.creditabilities.get(step);
      if (stepCreditabilities == null) {
         stepCreditabilities = new HashMap<KnowledgeSource, Integer>();
         this.creditabilities.put(step, stepCreditabilities);
      }

      stepCreditabilities.put(source, creditability);
   }

   public KnowledgeSource selectBestSource(PlanStep step, List<KnowledgeSource> sources)
   {
      Validate.notNull(step);
      Validate.notNull(sources);

      KnowledgeSource selectedSource = null;
      int maxCreditability = 0;
      for (KnowledgeSource source : sources) {
         int sourceCreditability = this.creditabilityForSource(step, source);
         if (sourceCreditability > 0 && sourceCreditability > maxCreditability) {
            selectedSource = source;
            maxCreditability = sourceCreditability;
         }
      }

      return selectedSource;
   }

   private int creditabilityForSource(PlanStep step, KnowledgeSource source)
   {
      Validate.notNull(step);
      Validate.notNull(source);

      Map<KnowledgeSource, Integer> stepCreditabilities = this.creditabilities.get(step);
      if (stepCreditabilities == null) return -1;

      Integer creditability = stepCreditabilities.get(source);
      return (creditability != null ? creditability : -1);
   }
}
