/*
 * ReadBlackboardException.java
 *
 * Copyright (C) Dec 19, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * @author stefan
 */
public class ReadBlackboardException extends BlackboardException
{
   public ReadBlackboardException()
   {
      super();
   }

   public ReadBlackboardException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public ReadBlackboardException(String message)
   {
      super(message);
   }

   public ReadBlackboardException(Throwable cause)
   {
      super(cause);
   }
}
