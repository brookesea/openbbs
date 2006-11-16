package org.openbbs.blackboard.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardZoneException;
import org.openbbs.blackboard.CloneBySerializationStrategy;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.WriteBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.PersistenceDelegate;

public class BlackboardPersistenceDelegateInteractionTest extends MockObjectTestCase
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

   protected void tearDown() throws Exception {
      this.bb.closeZone(this.zone);
   }

   public void test_storeEntry_is_called_when_a_new_entry_is_written_to_blackboard() {
      this.pdMock.expects(once()).method("storeEntry").with(same(this.bb), same(this.zone), eq(this.entry));
      this.bb.write(zone, entry);
      this.pdMock.verify();
   }

   public void test_storeEntry_is_NOT_called_when_the_zone_is_unknown() {
      this.pdMock.expects(never()).method("storeEntry").withAnyArguments();
      try {
         this.bb.write(new NamedZone("DOES_NOT_EXIST"), entry);
      }
      catch (BlackboardZoneException _) {
         // expected because zone does not exist
      }
      this.pdMock.verify();
   }
   
   public void test_storeEntry_is_NOT_called_for_duplicate_entries() {
      this.pdMock.expects(once()).method("storeEntry").with(same(this.bb), same(this.zone), eq(this.entry));
      this.bb.write(this.zone, this.entry);
      try {
         this.bb.write(this.zone, this.entry);
      }
      catch (WriteBlackboardException _) {
         // expected because entry is duplicate
      }
   }
}
