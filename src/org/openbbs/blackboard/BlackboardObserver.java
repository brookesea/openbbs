/*
 * BlackboardObserver.java
 *
 * Copyright (C) Dec 15, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * @author stefan
 */
public interface BlackboardObserver
{
   public void blackboardDidAddEntry(Blackboard blackboard, Zone zone, Object entry);

   public void blackboardDidRemoveEntry(Blackboard blackboard, Zone zone, Object entry);
}
