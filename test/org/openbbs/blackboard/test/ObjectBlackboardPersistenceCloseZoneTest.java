package org.openbbs.blackboard.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.CloneBySerializationStrategy;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.PersistenceDelegate;

public class ObjectBlackboardPersistenceCloseZoneTest extends MockObjectTestCase
{
   private final Zone zone = new NamedZone("TEST_ZONE");
   private final String entries[] = new String[] {"Entry 1", "Entry 2", "Entry 3", "Entry 4", "Entry 5"};
   private Blackboard bb;
   private Mock pdMock;

   protected void setUp() throws Exception {
      // setup blackboard
      this.bb = new ObjectBlackboard();
      ((ObjectBlackboard)this.bb).setCloneStrategy(new CloneBySerializationStrategy());
      this.bb.openZone(this.zone);
      
      // write entries
      for (String entry : this.entries) {
         this.bb.write(this.zone, entry);
      }
      
      // create PersistenceDelegate mock and attach to blackboard
      this.pdMock = mock(PersistenceDelegate.class);
      ((ObjectBlackboard)this.bb).setPersistenceDelegate((PersistenceDelegate)this.pdMock.proxy());
   }

   public void test_removeEntry_is_called_for_each_entry_when_a_zone_is_closed() {
      for (String entry : this.entries) {
         this.pdMock.expects(once()).method("removeEntry").with(same(this.bb), same(this.zone), eq(entry));
      }
      this.bb.closeZone(this.zone);
      this.pdMock.verify();
   }
}
