package org.openbbs.blackboard.persistence.prevalence;

/**
 * Thrown by a PrevalencePersistenceDelegate when errors happen.
 */
public class PrevalencePersistenceException extends RuntimeException
{
   public PrevalencePersistenceException() {
      super();
   }

   public PrevalencePersistenceException(String message, Throwable cause) {
      super(message, cause);
   }

   public PrevalencePersistenceException(String message) {
      super(message);
   }

   public PrevalencePersistenceException(Throwable cause) {
      super(cause);
   }
}
