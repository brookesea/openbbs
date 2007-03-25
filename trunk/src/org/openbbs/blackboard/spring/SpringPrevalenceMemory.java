package org.openbbs.blackboard.spring;

import org.openbbs.blackboard.persistence.prevalence.PrevalenceMemory;
import org.springframework.beans.factory.InitializingBean;

/**
 * Integrates PrevalenceMemory with the Spring framework.
 */
public class SpringPrevalenceMemory extends PrevalenceMemory implements InitializingBean
{
   private boolean initialRestore = true;
   private boolean snapshotAfterInitialRestore = false;

   /**
    * If set to true, the initial contents of the memory is restored from
    * its logfile (and from the latest snapshot if available). You most
    * likely want this to happen, so the default value for this property
    * is true.
    */
   public void setInitialRestore(boolean initialRestore) {
      this.initialRestore = initialRestore;
   }

   /**
    * Determines if a snapshot is created upon initial restore (only
    * if initalRestore is set to true). This is useful to reduce the
    * space that needs to be allocated on the hard disc to persist the
    * memory's contents. In addition, restoring a memory from a 
    * snapshot is usually faster than from its logfile. The default
    * value for this property is false.
    */
   public void setSnapshotAfterInitialRestore(boolean snapshotAfterInitialRestore) {
      this.snapshotAfterInitialRestore = snapshotAfterInitialRestore;
   }

   /**
    * @see InitializingBean#afterPropertiesSet()
    */
   public void afterPropertiesSet() throws Exception {
      if (this.initialRestore) {
         this.restore();
         if (this.snapshotAfterInitialRestore) {
            this.snapshot();
         }
      }
   }
}
