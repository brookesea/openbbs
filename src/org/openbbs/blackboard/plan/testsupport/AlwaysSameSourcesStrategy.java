/*
 * SelectFirstSourceStrategy.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
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
   
   public AlwaysSameSourcesStrategy addKnowledgeSource(KnowledgeSource source)
   {
      Validate.notNull(source);
      this.sources.add(source);
      return this;
   }
   
   public List<KnowledgeSource> getKnowledgeSources(PlanStep step)
   {
      Validate.isTrue(!this.sources.isEmpty());
      return this.sources;
   }
}
