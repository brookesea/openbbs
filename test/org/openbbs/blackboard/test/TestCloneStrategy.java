/*
 * TestCloneStrategy.java
 *
 * Copyright (C) Dec 20, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.test;

import org.openbbs.blackboard.CloneStrategy;

/**
 * Clone strategy for testing only. When it receives the
 * clone method with an object, it simply returns the object.
 *
 * @author stefan
 */
public class TestCloneStrategy implements CloneStrategy
{
   public Object clone(Object obj)
   {
      return obj;
   }
}
