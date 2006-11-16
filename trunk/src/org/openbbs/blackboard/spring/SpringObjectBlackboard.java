package org.openbbs.blackboard.spring;

import org.openbbs.blackboard.ObjectBlackboard;
import org.springframework.beans.factory.InitializingBean;

/**
 * Extends the ObjectBlackboard class with Spring capabilities.
 * 
 * @author stefan
 */
public class SpringObjectBlackboard extends ObjectBlackboard implements InitializingBean
{
   private boolean initialRestore;
   
   /**
    * Define whether the blackboard restores the entries from the
    * PersistenceDelegate initially.
    */
   public void setInitialRestore(boolean initialRestore) {
      this.initialRestore = initialRestore;
   }

   /**
    * @see org.springframework.beans.factory.InitializingBean#afterPropertiesSet()
    */
   public void afterPropertiesSet() throws Exception {
      if (this.initialRestore) {
         this.restore();
      }
   }
}
