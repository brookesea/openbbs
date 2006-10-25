/*
 * DebugControlDriver.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.debug;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.BasicControlDriver;
import org.openbbs.blackboard.BlackboardControl;

/**
 * A ControlDriver that allows "manual" stepping.
 *
 * @author sks
 */
public class DebugControlDriver extends BasicControlDriver
{
   private Set<DebugControlDriverListener> listeners = new HashSet<DebugControlDriverListener>();

   public void attachControl(BlackboardControl control)
   {
      super.attachControl(control);

      final BlackboardControl _control = control;
      this.eachListener(new ListenerBlock() {
         public void yield(DebugControlDriverListener listener)
         {
            listener.controlAttached(DebugControlDriver.this, _control);
         }
      });
   }

   public void detachControl(BlackboardControl control)
   {
      super.detachControl(control);

      final BlackboardControl _control = control;
      this.eachListener(new ListenerBlock() {
         public void yield(DebugControlDriverListener listener)
         {
            listener.controlDetached(DebugControlDriver.this, _control);
         }
      });
   }

   public void step()
   {
      this.eachListener(new ListenerBlock() {
         public void yield(DebugControlDriverListener listener)
         {
            listener.willStepNext(DebugControlDriver.this);
         }
      });

      try {
         this.nextRun();
      } catch (final Throwable exc) {
         this.eachListener(new ListenerBlock() {
            public void yield(DebugControlDriverListener listener)
            {
               listener.errorHappened(DebugControlDriver.this, exc);
            }
         });
      } finally {
         this.eachListener(new ListenerBlock() {
            public void yield(DebugControlDriverListener listener)
            {
               listener.didStepNext(DebugControlDriver.this);
            }
         });
      }
   }

   public void run()
   {
      this.eachListener(new ListenerBlock() {
         public void yield(DebugControlDriverListener listener)
         {
            listener.willRun(DebugControlDriver.this);
         }
      });

      try {
         this.runLoop();
      } catch (final Throwable exc) {
         this.eachListener(new ListenerBlock() {
            public void yield(DebugControlDriverListener listener)
            {
               listener.errorHappened(DebugControlDriver.this, exc);
            }
         });
      } finally {
         this.eachListener(new ListenerBlock() {
            public void yield(DebugControlDriverListener listener)
            {
               listener.didRun(DebugControlDriver.this);
            }
         });
      }
   }

   public void registerListener(DebugControlDriverListener listener)
   {
      Validate.notNull(listener);
      this.listeners.add(listener);
   }

   public void removeListener(DebugControlDriverListener listener)
   {
      Validate.notNull(listener);
      this.listeners.remove(listener);
   }

   private void eachListener(ListenerBlock block)
   {
      Validate.notNull(block);

      // duplicate listener set to allow concurrent registerListener/removeListener
      // messages
      Set<DebugControlDriverListener> _listeners = new HashSet<DebugControlDriverListener>(
               this.listeners);
      for (DebugControlDriverListener listener : _listeners)
         block.yield(listener);
   }

   private interface ListenerBlock
   {
      public void yield(DebugControlDriverListener listener);
   }
}
