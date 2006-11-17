package org.openbbs.blackboard.test;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.builder.NameMatchBuilder;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.prevalence.PlaybackDelegate;
import org.openbbs.blackboard.persistence.prevalence.PrevalencePersistenceDelegate;

public class PrevalencePersistenceTest extends MockObjectTestCase
{
   private static final Zone zone1 = new NamedZone("ZONE_1");
   private static final Zone zone2 = new NamedZone("ZONE_2");
   private final Entry entry1 = new Entry(1, "Teapot");
   private final Entry entry2 = new Entry(2, "Coffepot");
   private final Entry entry3 = new Entry("TEA", "Earl Gray");
   private File logFile;
   private PrevalencePersistenceDelegate persistenceDelegate;

   protected void setUp() throws Exception {
      this.logFile = File.createTempFile("prevalence", ".log");

      // fill the logfile with data
      PrevalencePersistenceDelegate fillDelegate = new PrevalencePersistenceDelegate();
      fillDelegate.setLogFile(logFile);
      fillDelegate.storeEntry(null, zone1, this.entry1);
      fillDelegate.storeEntry(null, zone1, this.entry2);
      fillDelegate.storeEntry(null, zone2, this.entry3);
      fillDelegate.removeEntry(null, zone1, entry2);
      fillDelegate.removeEntry(null, zone2, entry3);
      fillDelegate.storeEntry(null, zone2, entry3);
      fillDelegate.closeLog();

      this.persistenceDelegate = new PrevalencePersistenceDelegate();
      this.persistenceDelegate.setLogFile(logFile);
   }

   protected void tearDown() throws Exception {
      this.persistenceDelegate.closeLog();
   }

   public void test_restore_data_from_logfile() {
      Mock playbackMock = mock(PlaybackDelegate.class);
      playbackMock.expects(once()).method("storeEntry").with(eq(zone1), eq(entry1)).id("1");
      ((NameMatchBuilder)playbackMock.expects(once()).method("storeEntry").with(eq(zone1), eq(entry2))).after("1").id("2");
      ((NameMatchBuilder)playbackMock.expects(once()).method("storeEntry").with(eq(zone2), eq(entry3))).after("2").id("3");
      ((NameMatchBuilder)playbackMock.expects(once()).method("removeEntry").with(eq(zone1), eq(entry2))).after("3").id("4");
      ((NameMatchBuilder)playbackMock.expects(once()).method("removeEntry").with(eq(zone2), eq(entry3))).after("4").id("5");
      ((NameMatchBuilder)playbackMock.expects(once()).method("storeEntry").with(eq(zone2), eq(entry3))).after("5");

      this.persistenceDelegate.restoreEntries((PlaybackDelegate)playbackMock.proxy());
      playbackMock.verify();
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
