package org.openbbs.blackboard.persistence.snapshot;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Zone;

/**
 * A Snapshotter takes a Snapshottable BlackboardMemory and dumps
 * its contents to a file. 
 */
public class Snapshotter
{
   private File snapshotFile;

   /**
    * Create a new Snapshotter.
    */
   public Snapshotter() {
      return;
   }

   /**
    * Set the snapshot file. Each time a new snapshot is taken, the
    * contents of the file are replaced with the contents of the
    * snapshotted memory.
    */
   public void setSnapshotFile(File snapshotFile) {
      Validate.notNull(snapshotFile);
      this.snapshotFile = snapshotFile;
   }

   /**
    * Save the contents of a {@link SnapshottableMemory} to the snapshot
    * file. The memory must not be modified while taking the snapshot.
    * 
    * @param memory  a non-null SnapshottableMemory
    */
   public void takeSnapshot(SnapshottableMemory memory) {
      try {
         this.basicTakeSnapshot(memory);
      }
      catch (IOException exc) {
         throw new SnapshotException("failed to create snapshot (file=" + this.snapshotFile + ")", exc);
      }
   }

   /**
    * Restore the contents of {@link SnapshottleMemory} from a snapshot
    * file. It is up to the memory to clear its contents before or to
    * handle conflicts otherwise. The memory must not be modified while
    * its contents are restored.
    * 
    * @param memory  a non-null SnapshottableMemory
    */
   public void restoreFromSnapshot(SnapshottableMemory memory) {
      try {
         this.basicRestoreFromSnapshot(memory);
      }
      catch (Exception exc) {
         throw new SnapshotException("failed to restore contents of snapshot file " + this.snapshotFile, exc);
      }
   }

   private void basicTakeSnapshot(SnapshottableMemory memory) throws IOException {
      Validate.notNull(memory, "memory is null");
      Validate.notNull(this.snapshotFile, "snapshotFile is not set");

      ObjectOutputStream ostream = new ObjectOutputStream(new FileOutputStream(this.snapshotFile));
      try {
         for (Iterator<Zone> zoneIt = memory.getZonesIterator(); zoneIt.hasNext();) {
            ostream.writeObject(SnapshotMarker.NEW_ZONE);
            Zone zone = zoneIt.next();
            ostream.writeObject(zone);

            Iterator<Object> entryIt = memory.getEntriesInZone(zone);
            if (entryIt != null) {
               while (entryIt.hasNext()) {
                  ostream.writeObject(entryIt.next());
               }
            }
         }
      }
      finally {
         ostream.close();
      }
   }

   private void basicRestoreFromSnapshot(SnapshottableMemory memory) throws Exception {
      Validate.notNull(memory, "memory is null");
      Validate.notNull(this.snapshotFile, "snapshotFile is not set");

      ObjectInputStream istream = new ObjectInputStream(new FileInputStream(this.snapshotFile));
      try {
         boolean eof = false;
         Zone currentlyRestoredZone = null;
         while (!eof) {
            try {
               Object nextObject = istream.readObject();
               if (SnapshotMarker.NEW_ZONE.equals(nextObject)) {
                  Object newZone = istream.readObject();
                  Validate.isTrue(newZone instanceof Zone, newZone + " is not a Zone but should be");
                  memory.restoreZone((Zone)newZone);
                  currentlyRestoredZone = (Zone)newZone;
               }
               else {
                  Validate.notNull(currentlyRestoredZone, "entry " + nextObject + " found outside a zone");
                  memory.restoreEntry(nextObject, currentlyRestoredZone);
               }
            }
            catch (EOFException _) {
               eof = true;
            }
         }
      }
      finally {
         istream.close();
      }
   }
}
