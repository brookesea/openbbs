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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.snapshot.SnapshottableMemory;
import org.openbbs.blackboard.persistence.snapshot.Snapshotter;

public class SnapshotterTest extends MockObjectTestCase
{
   private List<Zone> zones;
   private Map<Zone, List> entries;
   private Snapshotter snapshotter;

   protected void setUp() throws Exception {
      this.zones = new ArrayList<Zone>();
      this.zones.add(new NamedZone("Zone_1"));
      this.zones.add(new NamedZone("Zone_2"));
      this.zones.add(new NamedZone("Zone_3"));

      this.entries = new HashMap<Zone, List>();
      this.entries.put(zones.get(0), Arrays.asList(new String[] { "Entry 1", "Entry 2" }));
      this.entries.put(zones.get(1), Collections.emptyList());
      this.entries.put(zones.get(2), Arrays.asList(new String[] { "Entry 3", "Entry 4" }));

      this.snapshotter = new Snapshotter();
      snapshotter.setSnapshotFile(File.createTempFile("memory", ".snapshot"));
   }

   public void testTakeAndRestoreSnapshot() throws Exception {
      Mock memoryMock = mock(SnapshottableMemory.class);
      
      memoryMock.expects(once()).method("getZonesIterator").withNoArguments().will(
            returnValue(this.zones.iterator()));

      for (Zone zone : this.zones) {
         // on takeSnapshot
         memoryMock.expects(once()).method("getEntriesInZone").with(eq(zone)).will(
               returnValue(this.entries.get(zone).iterator()));
         
         // on restoreFromSnapshot
         memoryMock.expects(once()).method("restoreZone").with(eq(zone));
         for (Object entry : this.entries.get(zone)) {
            memoryMock.expects(once()).method("restoreEntry").with(eq(entry), eq(zone));
         }
      }
      
      SnapshottableMemory memory = (SnapshottableMemory)memoryMock.proxy();
      this.snapshotter.takeSnapshot(memory);
      this.snapshotter.restoreFromSnapshot(memory);
      
      memoryMock.verify();
   }
}
