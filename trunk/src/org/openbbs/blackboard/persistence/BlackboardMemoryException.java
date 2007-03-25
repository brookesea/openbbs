package org.openbbs.blackboard.persistence;

/**
 * May be thrown by implementations of BlackboardMemory in
 * order to indicate problems.
 */
public class BlackboardMemoryException extends RuntimeException
{
   public BlackboardMemoryException() {
      super();
   }

   public BlackboardMemoryException(String message, Throwable cause) {
      super(message, cause);
   }

   public BlackboardMemoryException(String message) {
      super(message);
   }

   public BlackboardMemoryException(Throwable cause) {
      super(cause);
   }

   private static final long serialVersionUID = -6076245270904356897L;
}
