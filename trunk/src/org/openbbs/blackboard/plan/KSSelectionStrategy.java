/*
 * KnowledgeSourceSelectionStrategy.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.plan;

import java.util.Collection;

import org.openbbs.blackboard.control.BlackboardControl;
import org.openbbs.blackboard.ks.KnowledgeSource;

/**
 * @author sks
 */
public interface KSSelectionStrategy
{
   /**
    * Get the sources for a given {@link PlanStep}. The order in which
    * the KnowledgeSources are returned matters. A {@link BlackboardControl}
    * will execute the first enabled KnowledgeSource from the list, starting
    * with the first.
    */
   public Collection<KnowledgeSource> getKnowledgeSources(PlanStep step);
}
