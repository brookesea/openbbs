package org.openbbs.blackboard.persistence.snapshot;

/**
 * Thrown on errors related to taking or restoring memory snapshots.
 */
public class SnapshotException extends RuntimeException
{
   public SnapshotException() {
      super();
   }

   public SnapshotException(String message, Throwable cause) {
      super(message, cause);
   }

   public SnapshotException(String message) {
      super(message);
   }

   public SnapshotException(Throwable cause) {
      super(cause);
   }
}
