/*
 * KnowledgeSource.java
 *
 * Copyright (C) Dec 21, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.ks;

/**
 * A KnowledgeSource is an expert for a particular aspect of
 * a problem solving strategy. It reads entries from a Blackboard
 * and contributes new or updates existing entries.
 *
 * @author stefan
 */
public interface KnowledgeSource
{
   public boolean isEnabled(KSExecutionContext context);
   public void execute(KSExecutionContext context);
}
