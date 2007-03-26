package org.openbbs.blackboard.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.ks.KnowledgeSource;

/**
 * This {@link KSSelectionStrategy} maps a step to a list of 
 * KnowledgeSources. This means that for a given step, this
 * selection strategy would return all {@link KnowledgeSource}s
 * for this step, ordered by their addition to the selection
 * strategy.
 *
 * @author sks
 */
public class MultipleKSSelectionStrategy implements KSSelectionStrategy
{
   private Map<PlanStep, Collection<KnowledgeSource>> mappings = new HashMap<PlanStep, Collection<KnowledgeSource>>();

   public void addKnowledgeSource(PlanStep step, KnowledgeSource ks)
   {
      Validate.notNull(step, "step must not be null");
      Validate.notNull(ks, "ks must not be null");
     
      Collection<KnowledgeSource> ksForStep = this.mappings.get(step);
      if (ksForStep == null) {
         ksForStep = new ArrayList<KnowledgeSource>();
         this.mappings.put(step, ksForStep);
      }
      Validate.isTrue(!ksForStep.contains(ks), ks + " is already mapped to " + step);
      ksForStep.add(ks);
   }
   
   public void removeKnowledgeSource(PlanStep step, KnowledgeSource ks)
   {
      Validate.notNull(step, "step must not be null");
      Validate.notNull(ks, "ks must not be null");
     
      Collection<KnowledgeSource> ksForStep = this.mappings.get(step);
      Validate.isTrue(ksForStep != null && ksForStep.contains(step), ks + " is not mapped to " + step);
      ksForStep.remove(ks);
      if (ksForStep.isEmpty()) {
         this.mappings.remove(step);
      }
   }
   
   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step) {
      Validate.notNull(step, "step must not be null");

      Collection<KnowledgeSource> ksForStep = this.mappings.get(step);
      if (ksForStep == null) {
         return Collections.emptyList();
      }
      return Collections.unmodifiableCollection(ksForStep);
   }
}
