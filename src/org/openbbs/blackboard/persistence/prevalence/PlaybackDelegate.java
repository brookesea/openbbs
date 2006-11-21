package org.openbbs.blackboard.persistence.prevalence;

import org.openbbs.blackboard.Zone;

public interface PlaybackDelegate
{
   public void createZone(Zone zone);
   public void dropZone(Zone zone);
   public void storeEntry(Zone zone, Object entry);
   public void removeEntry(Object entry);
}
