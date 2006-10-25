/*
 * ControlDriver.java
 *
 * Copyright (C) Jan 4, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * @author stefan
 */
public interface ControlDriver
{
   public void attachControl(BlackboardControl control);

   public void detachControl(BlackboardControl control);
}
