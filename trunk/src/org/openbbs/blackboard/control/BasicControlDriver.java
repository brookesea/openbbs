package org.openbbs.blackboard.control;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.Validate;

/**
 * @author stefan
 */
public abstract class BasicControlDriver implements ControlDriver
{
   private List<BlackboardControl> controls = new ArrayList<BlackboardControl>();
   private boolean isRunning = false;

   public void attachControl(BlackboardControl control)
   {
      Validate.notNull(control);

      if (this.controls.contains(control)) return;
      this.controls.add(control);
   }

   public void detachControl(BlackboardControl control)
   {
      Validate.notNull(control);
      this.controls.remove(control);
   }

   public List<BlackboardControl> attachedControls()
   {
      return Collections.unmodifiableList(new ArrayList<BlackboardControl>(this.controls));
   }

   public boolean isRunning()
   {
      return this.isRunning;
   }

   protected void runLoop()
   {
      Validate.isTrue(!this.isRunning, "driver is already in run loop");

      this.isRunning = true;
      try {
         while (this.nextRun())
            ;
      } finally {
         this.isRunning = false;
      }
   }

   protected boolean nextRun()
   {
      boolean continueRun = false;

      // clone control list to allow concurrent attach and detach
      Iterator<BlackboardControl> it = new ArrayList<BlackboardControl>(this.controls).iterator();
      while (it.hasNext()) {
         continueRun = it.next().nextRun() | continueRun; // short circuit operator!!
      }

      return continueRun;
   }
}
