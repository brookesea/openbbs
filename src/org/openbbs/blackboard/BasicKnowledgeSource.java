/*
 * BasicKnowledgeSource.java
 *
 * Copyright (C) Dec 21, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import org.apache.commons.lang.Validate;

/**
 * An abstract class which can be used as a base for knowledge
 * source implementations.
 *
 * @author stefan
 */
public abstract class BasicKnowledgeSource extends ZonedBlackboardReader implements KnowledgeSource
{
   public final void attach(Blackboard blackboard, ZoneSelector zoneSelector)
   {
      Validate.notNull(blackboard);
      Validate.notNull(zoneSelector);
      this.setBlackboardAndZone(blackboard, zoneSelector);
   }
}
