/*
 * WriteBlackboardException.java
 *
 * Copyright by Stefan Kleine Stegemann, 2005
 */
package org.openbbs.blackboard;

/**
 * @autor sks
 */
public class WriteBlackboardException extends BlackboardException
{
   public WriteBlackboardException()
   {
      super();
   }

   public WriteBlackboardException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public WriteBlackboardException(String message)
   {
      super(message);
   }

   public WriteBlackboardException(Throwable cause)
   {
      super(cause);
   }
}
