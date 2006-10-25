/*
 * KnowledgeSource.java
 *
 * Copyright (C) Dec 21, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * A KnowledgeSource is an expert for a particular aspect of
 * a problem solving strategy. It reads entries from a Blackboard
 * and contributes new or updates existing entries.
 *
 * @author stefan
 */
public interface KnowledgeSource
{
   public void attach(Blackboard blackboard, ZoneSelector zoneSelector);

   public boolean canContribute();

   public void contribute();
}
