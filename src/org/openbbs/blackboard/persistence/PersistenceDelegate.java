package org.openbbs.blackboard.persistence;

import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.prevalence.PlaybackDelegate;

/**
 * A PersistenceDelegte is responsible for storing data on
 * a Blackboard on an external media.
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
   public void removeEntry(Blackboard blackboard, Zone zone, Object entry);

   /**
    * Restore entries from the external media and feed them into
    * the specified PlaybackDelegate. Note that, dependeding on
    * the PersistenceDelegate implementation, an entry may be stored
    * and removed several times during the restore phase. This means
    * that the playbackDelegate may receive subsequent messages for
    * the same entry.
    * 
    * @param playbackDelegate  an object that will get the restored
    *                          entries; not null.
    */
   public void restoreEntries(PlaybackDelegate playbackDelegate);
}
