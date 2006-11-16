package org.openbbs.blackboard.persistence;

import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.Zone;

/**
 * A PersistenceDelegte is responsible for storing the data on
 * a Blackboard to an external media.
 * 
 * @author stefan
 */
public interface PersistenceDelegate
{
   /**
    * A new entry has been written to the blackboard and now has
    * to be stored by the PersistenceDelegate. The delegate must
    * not modify the entry or the blackboard.
    * 
    * @param blackboard  the blackboard to which the entry has
    *                    been written.
    * @param zone        the zone to which the entry belongs.
    * @param entry       the entry itself.
    * 
    */
   public void storeEntry(Blackboard blackboard, Zone zone, Object entry);
   
   /**
    * An entry has been removed from the blackboard and now has
    * to be removed from the external media. The delegate must
    * not modify the entry or the blackboard.
    * 
    * @param blackboard  the blackboard from which the entry has
    *                    been removed.
    * @param zone        the zone the entry did belong to.
    * @param entry       the entry itself.
    */
   public void deleteEntry(Blackboard blackboard, Zone zone, Object entry);
}
