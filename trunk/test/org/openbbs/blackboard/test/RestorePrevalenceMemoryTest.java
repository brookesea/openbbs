package org.openbbs.blackboard.test;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.prevalence.PrevalenceMemory;

import junit.framework.TestCase;

public class RestorePrevalenceMemoryTest extends TestCase
{
   private File logFile;
   private PrevalenceMemory memory;
   private static final Zone zone1 = new NamedZone("ZONE_1");
   private static final Zone zone2 = new NamedZone("ZONE_2");
   private static final Zone emtpyZone = new NamedZone("EMTPY_ZONE");
   private final Entry entry1 = new Entry(1, "Teapot");
   private final Entry entry2 = new Entry(2, "Coffepot");
   private final Entry entry3 = new Entry("TEA", "Earl Gray");

   protected void setUp() throws Exception {
      this.logFile = File.createTempFile("prevalence", ".log");

      // fill the memory with data
      PrevalenceMemory filledMemory = new PrevalenceMemory();
      filledMemory.setLogFile(this.logFile);
      filledMemory.createZone(zone1);
      filledMemory.createZone(zone2);
      filledMemory.storeEntry(zone1, entry1);
      filledMemory.storeEntry(zone1, entry2);
      filledMemory.storeEntry(zone2, entry3);

      this.memory = new PrevalenceMemory();
      this.memory.setLogFile(this.logFile);
   }

   public void test_entries_and_zones_are_restored() {
      assertTrue(this.memory.entryExists(this.entry1));
      assertTrue(this.memory.entryExists(this.entry2));
      assertTrue(this.memory.entryExists(this.entry3));
   }

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
