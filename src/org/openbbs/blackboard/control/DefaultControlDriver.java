/*
 * DefaultControlDriver.java
 *
 * Copyright (C) Jan 4, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.control;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardObserver;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;

/**
 * Observes a Blackboard and runs the registered controls as
 * long as at least one control responds to nextRun with true.
 *
 * @author stefan
 */
public class DefaultControlDriver extends BasicControlDriver implements BlackboardObserver
{
   public DefaultControlDriver() {
      return;
   }

   public DefaultControlDriver(Blackboard blackboard) {
      this.setBlackboard(blackboard);
   }

   public void setBlackboard(Blackboard blackboard) {
      Validate.notNull(blackboard);
      blackboard.registerInterest(ZoneSelector.ALL_ZONES, this);
   }

   public void blackboardDidAddEntry(Blackboard blackboard, Zone zone, Object entry) {
      if (!this.isRunning()) this.runLoop();
   }

   public void blackboardDidRemoveEntry(Blackboard blackboard, Zone zone, Object entry) {
      if (!this.isRunning()) this.runLoop();
   }
}
