package org.openbbs.blackboard.test;

import java.io.File;
import java.io.Serializable;

import junit.framework.TestCase;

import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.prevalence.PrevalencePersistenceDelegate;

public class PrevalencePersistenceTest extends TestCase
{
   private static final Zone zone1 = new NamedZone("ZONE_1");
   private static final Zone zone2 = new NamedZone("ZONE_2");
   private File logFile;
   private final Entry entry1 = new Entry(1, "Teapot");
   private final Entry entry2 = new Entry(2, "Coffepot");
   private final Entry entry3 = new Entry("TEA", "Earl Gray");

   protected void setUp() throws Exception {
      this.logFile = File.createTempFile("prevalence", ".log");
      PrevalencePersistenceDelegate delegate = new PrevalencePersistenceDelegate();
      delegate.setLogFile(logFile);
      delegate.storeEntry(null, zone1, this.entry1);
      delegate.storeEntry(null, zone1, this.entry2);
      delegate.storeEntry(null, zone2, this.entry3);
      delegate.terminate();
   }

   protected void tearDown() throws Exception {
   }
   
   public void test_restore_data_from_logfile() {
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
   }
}
