/*
 * SelectFirstSourceStrategy.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.plan.testsupport;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.KnowledgeSource;
import org.openbbs.blackboard.plan.KnowledgeSourceSelectionStrategy;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * For any step and any, non-empty list of KnowledgeSources
 * return always the first source in the list. While useless
 * in production systems this strategy is intended to support
 * testing.
 * 
 * @author sks
 */
public class FirstSourceStrategy implements KnowledgeSourceSelectionStrategy
{
   public KnowledgeSource selectBestSource(PlanStep step, List<KnowledgeSource> sources)
   {
      Validate.notNull(sources.isEmpty());
      return sources.get(0);
   }
}
