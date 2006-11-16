package org.openbbs.blackboard.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardZoneException;
import org.openbbs.blackboard.CloneBySerializationStrategy;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.WriteBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.EqualObjectFilter;
import org.openbbs.blackboard.persistence.PersistenceDelegate;

public class ObjectBlackboardPersistenceWriteEntryTest extends MockObjectTestCase
{
   private final Zone zone = new NamedZone("TEST_ZONE");
   private final String entry = "Blackboard Entry";
   private Blackboard bb;
   private Mock pdMock;

   protected void setUp() throws Exception {
      this.bb = new ObjectBlackboard();
      ((ObjectBlackboard)this.bb).setCloneStrategy(new CloneBySerializationStrategy());
      this.bb.openZone(this.zone);
      this.pdMock = mock(PersistenceDelegate.class);
      ((ObjectBlackboard)this.bb).setPersistenceDelegate((PersistenceDelegate)pdMock.proxy());
   }

   public void test_storeEntry_is_called_when_a_new_entry_is_written_to_blackboard() {
      this.pdMock.expects(once()).method("storeEntry").with(same(this.bb), same(this.zone), eq(this.entry));
      this.bb.write(zone, entry);
      this.pdMock.verify();
   }

   public void test_storeEntry_is_not_called_when_the_zone_is_unknown() {
      this.pdMock.expects(never()).method("storeEntry").withAnyArguments();
      try {
         this.bb.write(new NamedZone("DOES_NOT_EXIST"), entry);
      }
      catch (BlackboardZoneException _) {
         // expected because zone does not exist
      }
      this.pdMock.verify();
   }

   public void test_storeEntry_is_not_called_for_duplicate_entries() {
      this.pdMock.expects(once()).method("storeEntry").with(same(this.bb), same(this.zone), eq(this.entry));
      this.bb.write(this.zone, this.entry);
      try {
         this.bb.write(this.zone, this.entry);
      }
      catch (WriteBlackboardException _) {
         // expected because entry is duplicate
      }
      this.pdMock.verify();
   }

   public void test_storeEntry_is_not_called_if_entry_is_not_serializable() {
      this.pdMock.expects(never()).method("storeEntry").withAnyArguments();
      Object notSerializableEntry = new Object();
      try {
         this.bb.write(this.zone, notSerializableEntry);
      }
      catch (IllegalArgumentException _) {
         // expected because entry is not serializable
      }
      this.pdMock.verify();
   }

   public void test_entry_is_not_written_to_blackboard_if_storeEntry_fails() {
      this.pdMock.expects(once()).method("storeEntry").withAnyArguments().will(
            throwException(new RuntimeException("storeEntry failed")));
      try {
         this.bb.write(this.zone, this.entry);
      }
      catch (RuntimeException _) {
         // thrown by storeEntry
      }
      this.pdMock.verify();
      assertFalse(this.bb.exists(new ExactZoneSelector(this.zone), new EqualObjectFilter(this.entry)));
   }
}
