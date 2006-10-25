/*
 * DebugControlDriverListener.java
 *
 * Copyright (C) Jan 10, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.debug;

import org.openbbs.blackboard.BlackboardControl;

/**
 * @author stefan
 */
public interface DebugControlDriverListener
{
   public void willStepNext(DebugControlDriver driver);

   public void didStepNext(DebugControlDriver driver);

   public void willRun(DebugControlDriver driver);

   public void didRun(DebugControlDriver driver);

   public void errorHappened(DebugControlDriver driver, Throwable exc);

   public void controlAttached(DebugControlDriver driver, BlackboardControl control);

   public void controlDetached(DebugControlDriver driver, BlackboardControl control);
}
