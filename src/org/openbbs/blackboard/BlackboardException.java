/*
 * BlackboardException.java
 *
 * Copyright by Stefan Kleine Stegemann, 2005
 */
package org.openbbs.blackboard;

/**
 * @author sks
 */
public class BlackboardException extends RuntimeException
{
   public BlackboardException()
   {
      super();
   }

   public BlackboardException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public BlackboardException(String message)
   {
      super(message);
   }

   public BlackboardException(Throwable cause)
   {
      super(cause);
   }
}
