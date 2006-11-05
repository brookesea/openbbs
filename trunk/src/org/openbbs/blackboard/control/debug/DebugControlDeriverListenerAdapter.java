/*
 * DebugControlDeriverListenerAdapter.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.control.debug;

import org.openbbs.blackboard.control.BlackboardControl;

/**
 * @author stefan
 */
public class DebugControlDeriverListenerAdapter implements DebugControlDriverListener
{
   public void willStepNext(DebugControlDriver driver)
   {
   }

   public void didStepNext(DebugControlDriver driver)
   {
   }

   public void willRun(DebugControlDriver driver)
   {
   }

   public void didRun(DebugControlDriver driver)
   {
   }

   public void errorHappened(DebugControlDriver driver, Throwable exc)
   {
   }

   public void controlAttached(DebugControlDriver driver, BlackboardControl control)
   {
   }

   public void controlDetached(DebugControlDriver driver, BlackboardControl control)
   {
   }
}
