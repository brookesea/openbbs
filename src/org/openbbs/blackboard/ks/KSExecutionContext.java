package org.openbbs.blackboard.ks;

import org.openbbs.blackboard.BlackboardAccess;
import org.openbbs.blackboard.Zone;

/**
 * Provides the execution context for KnowledgeSources.
 * 
 * @author stefan
 */
public interface KSExecutionContext
{
   /**
    * Get the default access to the blackboard. Access is restricted
    * to the zone in which the KnowledgeSource is meant to work.
    */
   public BlackboardAccess blackboard();

   /**
    * Get access to a particular zone on the blackboard. This method
    * fails if access to the zone is prohibited in this context.
    * 
    * @param zone  a non-null zone.
    */
   public BlackboardAccess blackboard(Zone zone);
}
