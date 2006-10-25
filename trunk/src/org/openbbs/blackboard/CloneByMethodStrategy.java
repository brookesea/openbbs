/*
 * CloneByMethodStrategy.java
 *
 * Copyright (C) Dec 20, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.apache.commons.lang.Validate;

/**
 * An object is cloned by sending it a particular message.
 *
 * @author stefan
 */
public class CloneByMethodStrategy implements CloneStrategy
{
   private String cloneMessage = null;

   public CloneByMethodStrategy(String cloneMessage)
   {
      Validate.notNull(cloneMessage);
      this.cloneMessage = cloneMessage;
   }

   public Object clone(Object obj)
   {
      if (obj == null) return null;

      try {
         Method m = obj.getClass().getMethod(this.cloneMessage);
         return m.invoke(obj);
      } catch (NoSuchMethodException exc) {
         throw new UnsupportedOperationException("object " + obj + " does not respond to "
                  + this.cloneMessage + " message", exc);
      } catch (InvocationTargetException exc) {
         throw new RuntimeException("error while sending " + this.cloneMessage + " message to "
                  + obj, exc.getTargetException());
      } catch (IllegalAccessException exc) {
         throw new UnsupportedOperationException("not allowed to send message " + this.cloneMessage
                  + " to object " + obj, exc);
      }
   }
}
