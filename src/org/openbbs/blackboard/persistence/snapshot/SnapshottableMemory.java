package org.openbbs.blackboard.persistence.snapshot;

import java.util.Iterator;

import org.openbbs.blackboard.Zone;

/**
 * The contents of a SnapshottableMemory can be saved to a
 * snasphot file and restored from such a file. Use a Snapshotter
 * to create such a snapshot file.
 */
public interface SnapshottableMemory
{
   /**
    * Returns a non-null Iterator over all zones in the memory (including
    * the emtpy ones.
    */
   public Iterator<Zone> getZonesIterator();
   
   /**
    * Returns an iterator over all entries stored in a particular
    * zone inside the memory. If the zone is empty, it is both ok,
    * to return null or an Iterator which returns no object.
    * 
    * @param zone  a non-null Zone.
    */
   public Iterator<Object> getEntriesInZone(Zone zone);
   
   /**
    * Restore a Zone in the memory. This method is called when the
    * contents of a memory are restored from a snapshot file. The
    * entries will follow next, if any.
    * 
    * @param zone  the non-null Zone to be restored.
    */
   public void restoreZone(Zone zone);
   
   /**
    * Restore an entry in a Zone. It is guaranteed that the zone has
    * been restored before.
    * 
    * @param entry  a non-null entry to be restored.
    * @param zone   the non-null zone in which the restored entry resides.
    */
   public void restoreEntry(Object entry, Zone zone);
}
