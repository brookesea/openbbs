package org.openbbs.blackboard.persistence.prevalence;

/**
 * Throw when someone tries to read or modify a
 * locked memory.
 */
public class MemoryLockedException extends PrevalencePersistenceException
{
   public MemoryLockedException() {
      super();
   }

   public MemoryLockedException(String message, Throwable cause) {
      super(message, cause);
   }

   public MemoryLockedException(String message) {
      super(message);
   }

   public MemoryLockedException(Throwable cause) {
      super(cause);
   }
}
