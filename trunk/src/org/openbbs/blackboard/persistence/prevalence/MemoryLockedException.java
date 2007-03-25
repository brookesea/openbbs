package org.openbbs.blackboard.persistence.prevalence;

/**
 * Throw when someone tries to read or modify a locked memory. A memory is
 * usually locked only for a limited amount of time, so it's worth to retry
 * the operation later.
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

   private static final long serialVersionUID = -4559949109133317574L;
}
