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
   
   public void createMapping(PlanStep step, KnowledgeSource knowledgeSource)
   {
      Validate.notNull(step);
      Validate.notNull(knowledgeSource);
      mappings.put(step.getName(), knowledgeSource);
   }
   
   public void setMappings(Map<String, KnowledgeSource> mappings)
   {
      Validate.notNull(mappings);
      this.mappings = mappings;
   }
   
   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step)
   {
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
