/*
 * BlackboardZoneException.java
 *
 * Copyright (C) Jan 14, 2006 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

/**
 * @author stefan
 */
public class BlackboardZoneException extends BlackboardException
{
   public BlackboardZoneException()
   {
      super();
   }

   public BlackboardZoneException(String message, Throwable cause)
   {
      super(message, cause);
   }

   public BlackboardZoneException(String message)
   {
      super(message);
   }

   public BlackboardZoneException(Throwable cause)
   {
      super(cause);
   }
}
