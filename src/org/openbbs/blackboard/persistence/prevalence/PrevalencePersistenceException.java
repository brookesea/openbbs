package org.openbbs.blackboard.persistence.prevalence;

/**
 * Throw my a PrevalenceMemory when errors happen.
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

   private static final long serialVersionUID = 2475719910146355085L;
}
