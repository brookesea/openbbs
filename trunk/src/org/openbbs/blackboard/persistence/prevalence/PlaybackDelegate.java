package org.openbbs.blackboard.persistence.prevalence;

import org.openbbs.blackboard.Zone;

/**
 * A PlaybackDelegate is used when a PrevalencePersistenceDelegate
 * restores the data from an external media.
 */
public interface PlaybackDelegate
{
   public void storeEntry(Zone zone, Object entry);
   public void removeEntry(Zone zone, Object entry);
}
