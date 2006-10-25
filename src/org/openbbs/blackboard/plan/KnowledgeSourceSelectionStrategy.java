/*
 * KnowledgeSourceSelectionStrategy.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.plan;

import java.util.List;

import org.openbbs.blackboard.KnowledgeSource;

/**
 * @author sks
 */
public interface KnowledgeSourceSelectionStrategy
{
   public KnowledgeSource selectBestSource(PlanStep step, List<KnowledgeSource> sources);
}
