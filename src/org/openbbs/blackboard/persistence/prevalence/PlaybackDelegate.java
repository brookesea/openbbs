package org.openbbs.blackboard.persistence.prevalence;

import org.openbbs.blackboard.Zone;

/**
 * A PlaybackDelegate is used as a consumer for entries which are
 * restored by a PersistenceDelegate.
 */
public interface PlaybackDelegate
{
   public void storeEntry(Zone zone, Object entry);
   public void removeEntry(Object entry);
}
