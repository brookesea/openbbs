package org.openbbs.blackboard.persistence;

import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.Zone;

/**
 * A funny PersistenceDelegate implementation that does not
 * persist entries. It can be used when a transient Blackboard
 * is needed.
 * 
 * @author stefan
 */
public class TransientPersistenceDelegate implements PersistenceDelegate
{
   public void storeEntry(Blackboard blackboard, Zone zone, Object entry) {
   }

   public void removeEntry(Blackboard blackboard, Zone zone, Object entry) {
   }

   public void restoreEntries(PlaybackDelegate playbackDelegate) {
   }
}
