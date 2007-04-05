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
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.EqualObjectFilter;
import org.openbbs.blackboard.persistence.prevalence.PrevalenceMemory;
import org.openbbs.blackboard.persistence.prevalence.SimpleLogFile;

import junit.framework.TestCase;

public class RestorePrevalenceMemoryTest extends TestCase
{
   private File outputFile;
   private PrevalenceMemory memory;
   private static final Zone zone1 = new NamedZone("ZONE_1");
   private static final Zone zone2 = new NamedZone("ZONE_2");
   private static final Zone emptyZone = new NamedZone("EMTPY_ZONE");
   private static final Zone removedZone = new NamedZone("REMOVED_ZONE");
   private final Entry entry1 = new Entry(1, "Teapot");
   private final Entry entry2 = new Entry(2, "Coffepot");
   private final Entry entry3 = new Entry("TEA", "Earl Gray");
   private final Entry entry3Changed = new Entry("TEA", "Earl Green");
   private final Entry entryInRemovedZone = new Entry("Dull Entry", "");

   protected void setUp() throws Exception {
      this.outputFile = File.createTempFile("prevalence", ".log");

      // fill the memory with data
      PrevalenceMemory filledMemory = new PrevalenceMemory();
      filledMemory.setLogFile(new SimpleLogFile(this.outputFile));

      // create the zones first
      filledMemory.createZone(zone1);
      filledMemory.createZone(zone2);
      filledMemory.createZone(emptyZone);
      
      // store some entries in zone1
      filledMemory.storeEntry(zone1, entry1);
      filledMemory.storeEntry(zone1, entry2);
      
      // move an entry from zone1 to zone2
      filledMemory.removeEntry(entry2);
      filledMemory.storeEntry(zone2, entry2);

      // create a zone and remove it
      filledMemory.createZone(removedZone);
      filledMemory.storeEntry(removedZone, entryInRemovedZone);
      filledMemory.dropZone(removedZone);
      
      // change an entry
      filledMemory.storeEntry(zone2, entry3);
      filledMemory.removeEntry(entry3);
      filledMemory.storeEntry(zone2, entry3Changed);

      this.memory = new PrevalenceMemory();
      this.memory.setLogFile(new SimpleLogFile(this.outputFile));
      this.memory.restore();
   }

   public void test_entries_and_zones_are_restored() {
      assertTrue(this.memory.zoneExists(zone1));
      assertTrue(this.memory.zoneExists(zone2));
      assertTrue(this.memory.zoneExists(emptyZone));
      
      assertTrue(this.memory.entryExists(this.entry1));
      assertEquals(zone1, this.memory.getZone(entry1));
      assertTrue(this.memory.entryExists(this.entry2));
      assertEquals(zone2, this.memory.getZone(entry2));

      assertFalse(this.memory.zoneExists(removedZone));
      assertFalse(this.memory.entryExists(entryInRemovedZone));
      
      assertTrue(this.memory.entryExists(this.entry3));
      assertEquals(zone2, this.memory.getZone(entry3));
      Object entry3FromMemory = this.memory.getEntries(new ExactZoneSelector(zone2), new EqualObjectFilter(this.entry3)).next();
      assertEquals(this.entry3Changed.getValue(), ((Entry)entry3FromMemory).getValue());
   }

   @SuppressWarnings("serial")
   private static class Entry implements Serializable
   {
      private final Object key;
      private Object value;

      public Entry(Object key, Object value) {
         this.key = key;
         this.value = value;
      }

      public Object getValue() {
         return this.value;
      }

      public void setValue(Object value) {
         this.value = value;
      }

      public boolean equals(Object obj) {
         if (!(obj instanceof Entry)) return false;
         return this.key.equals(((Entry)obj).key);
      }

      public int hashCode() {
         return this.key.hashCode();
      }

      public String toString() {
         return new ToStringBuilder(this).append("key", this.key).append("value", this.value).toString();
      }
   }
}
