package org.openbbs.blackboard.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.CloneBySerializationStrategy;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.ReadBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.EqualObjectFilter;
import org.openbbs.blackboard.persistence.PersistenceDelegate;

public class ObjectBlackboardPersistenceTakeEntryTest extends MockObjectTestCase
{
   private final Zone zone = new NamedZone("TEST_ZONE");
   private final String entry = "Blackboard Entry";
   private Blackboard bb;
   private Mock pdMock;

   protected void setUp() throws Exception {
      // configure blackboard with one entry in TEST_ZONE
      this.bb = new ObjectBlackboard();
      ((ObjectBlackboard)this.bb).setCloneStrategy(new CloneBySerializationStrategy());
      this.bb.openZone(this.zone);
      this.bb.write(this.zone, this.entry);

      // setup mock PersistenceDelegate
      this.pdMock = mock(PersistenceDelegate.class);
      ((ObjectBlackboard)this.bb).setPersistenceDelegate((PersistenceDelegate)this.pdMock.proxy());
   }

   public void test_removeEntry_is_called_when_an_entry_is_taken_from_blackboard() {
      this.pdMock.expects(once()).method("removeEntry").with(same(this.bb), same(this.zone), eq(this.entry));
      this.bb.take(new ExactZoneSelector(this.zone), new EqualObjectFilter(this.entry));
      this.pdMock.verify();
   }

   public void test_removeEntry_is_not_called_when_taken_entry_does_not_exist() {
      this.pdMock.expects(never()).method("removeEntry").withAnyArguments();
      try {
         this.bb.take(new ExactZoneSelector(this.zone), new EqualObjectFilter("Foo"));
      }
      catch (ReadBlackboardException _) {
         // expected because taken entry does not exist
      }
      this.pdMock.verify();
   }

   public void test_entry_is_not_removed_if_removeEntry_fails() {
      this.pdMock.expects(once()).method("removeEntry").withAnyArguments().will(
            throwException(new RuntimeException("removeEntry failed")));
      try {
         this.bb.take(new ExactZoneSelector(this.zone), new EqualObjectFilter(this.entry));
      }
      catch (RuntimeException _) {
         // throw by removeEntry
      }
      this.pdMock.verify();
      assertTrue(this.bb.exists(new ExactZoneSelector(this.zone), new EqualObjectFilter(this.entry)));
      assertSame(this.zone, this.bb.zoneOf(this.entry));
   }
}
