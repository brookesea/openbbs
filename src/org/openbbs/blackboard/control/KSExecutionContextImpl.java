package org.openbbs.blackboard.control;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardAccess;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZonedBlackboardAccess;
import org.openbbs.blackboard.ks.KSExecutionContext;

/**
 * KSExecutionContext implementation for execution of KnowledgeSources.
 * 
 * @author stefan
 */
class KSExecutionContextImpl implements KSExecutionContext
{
   private final Blackboard blackboard;
   private final BlackboardAccess defaultAccess;
   
   public KSExecutionContextImpl(Blackboard blackboard, Zone zone)
   {
      Validate.notNull(blackboard);
      Validate.notNull(zone);
      this.blackboard = blackboard;
      this.defaultAccess = new ZonedBlackboardAccess(blackboard, zone);
   }

   public BlackboardAccess blackboard()
   {
      return this.defaultAccess;
   }

   public BlackboardAccess blackboard(Zone zone)
   {
      Validate.notNull(zone);
      // TODO: security check (access to zone allowed?)
      return new ZonedBlackboardAccess(this.blackboard, zone);
   }
}
