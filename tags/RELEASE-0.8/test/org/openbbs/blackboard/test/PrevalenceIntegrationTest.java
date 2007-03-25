/*
 *  Copyright [2006-2007] [Stefan Kleine Stegemann]
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.openbbs.blackboard.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.openbbs.blackboard.AllZonesSelector;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.CloneBySerializationStrategy;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.EqualObjectFilter;
import org.openbbs.blackboard.persistence.prevalence.PrevalenceMemory;
import org.openbbs.blackboard.persistence.prevalence.SimpleLogFile;
import org.openbbs.blackboard.persistence.snapshot.Snapshotter;

/**
 * This is an !integration! test for the prevalence memory.
 * It tests if the contents of a memory can be restored from
 * a logfile and a snapshot file.
 */
public class PrevalenceIntegrationTest extends TestCase
{
   private File logFile;
   private File snapshotFile;
   private List<Zone> zones;
   private Map<Object, Zone> entries;
   
   private void loadZonesAndEntries() {
      this.zones = new ArrayList<Zone>();
      this.zones.add(new NamedZone("Shakespeare"));
      this.zones.add(new NamedZone("Beckett"));
      this.zones.add(new NamedZone("Others"));
      
      this.entries = new HashMap<Object, Zone>();
      this.entries.put("We Like It", this.zones.get(0));
      this.entries.put("Hutt does Lear Lite at Stratford", this.zones.get(0));
      this.entries.put("A dog came in the kitchen and stole a crust of bread", this.zones.get(1));
      this.entries.put("Nothing happens, nobody comes, nobody goes, it's awful!", this.zones.get(1));
   }
   
   protected void setUp() throws Exception {
      this.logFile = File.createTempFile("prevalence", ".log");
      this.snapshotFile = File.createTempFile("prevalence", ".snapshot");

      PrevalenceMemory memory = new PrevalenceMemory();
      memory.setLogFile(new SimpleLogFile(logFile));
      memory.setSnapshotter(new Snapshotter(this.snapshotFile));
      ObjectBlackboard bb = new ObjectBlackboard();
      bb.setMemory(memory);
      bb.setCloneStrategy(new CloneBySerializationStrategy());
      
      this.loadZonesAndEntries();
      for (Zone zone : this.zones) {
         bb.openZone(zone);
      }

      // first half of entries, then snapshot, then the rest
      List<Object> remainingEntries = new ArrayList<Object>(this.entries.keySet());
      for (int i = 0; i < remainingEntries.size() / 2; i++) {
         Object entry = remainingEntries.get(0);
         bb.write(this.entries.get(entry), entry);
         remainingEntries.remove(0);
      }
      memory.snapshot();
      for (Object entry : remainingEntries) {
         bb.write(this.entries.get(entry), entry);
      }
   }
   
   public void testRestoreBlackboardFromLogFileAndSnapshot() {
      Blackboard bb = this.createAndRestoreBlackboard();
      for (Object entry : this.entries.keySet()) {
         Object entryFromBB = bb.read(new AllZonesSelector(), new EqualObjectFilter(entry));
         assertNotNull(entryFromBB);
         assertEquals(entry, entryFromBB);
         assertEquals(this.entries.get(entry), bb.zoneOf(entryFromBB));
      }
   }
   
   private Blackboard createAndRestoreBlackboard() {
      PrevalenceMemory memory = new PrevalenceMemory();
      memory.setLogFile(new SimpleLogFile(this.logFile));
      memory.setSnapshotter(new Snapshotter(this.snapshotFile));
      ObjectBlackboard bb = new ObjectBlackboard();
      bb.setMemory(memory);
      bb.setCloneStrategy(new CloneBySerializationStrategy());
      memory.restore();
      return bb;
   }
}
