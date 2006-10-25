/*
 * BlackboardControlListener.java
 *
 * Copyright (C) Jan 10, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import java.util.List;

import org.openbbs.blackboard.plan.PlanStep;

/**
 * A BlackboardControlListener can register itself with a BlackboardControl
 * in order to monitor it's state.
 * @author stefan
 */
public interface BlackboardControlListener
{
   public void foundApplicableSources(BlackboardControl control, List<KnowledgeSource> sources);

   public void didSelectPossibleSteps(BlackboardControl control, List<PlanStep> steps);

   public void didSelectNextSource(BlackboardControl control, KnowledgeSource source);
}
