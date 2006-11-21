package org.openbbs.blackboard.persistence.prevalence;

/**
 * This exception is raised to signal problems with a
 * {@link LogFile}.
 */
public class LogFileException extends RuntimeException
{
  public LogFileException() {
      super();
   }

   public LogFileException(String message, Throwable cause) {
      super(message, cause);
   }

   public LogFileException(String message) {
      super(message);
   }

   public LogFileException(Throwable cause) {
      super(cause);
   }
}
