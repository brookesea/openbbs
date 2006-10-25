/*
 * BlackboardTest.java
 *
 * Copyright by Stefan Kleine Stegemann, 2005
 */
package org.openbbs.blackboard.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardObserver;
import org.openbbs.blackboard.CloneStrategy;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.WriteBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;

/**
 * @author sks
 */
public class WriteEntryToObjectBlackboardTest extends MockObjectTestCase
{
   private Blackboard bb = null;

   public void setUp()
   {
      this.bb = new ObjectBlackboard(new TestCloneStrategy());
   }

   public void test_blackboard_notifies_observers()
   {
      String entry = "Hello Blackboard";

      Mock observer = mock(BlackboardObserver.class);
      observer.expects(once()).method("blackboardDidAddEntry").with(same(bb), eq(Zone.DEFAULT),
               eq(entry));

      this.bb.registerInterest(ZoneSelector.ALL_ZONES, (BlackboardObserver)observer.proxy());
      this.bb.write(Zone.DEFAULT, entry);

      observer.verify();
   }

   public void test_cannot_write_the_same_entry_twice_without_take()
   {
      Object entry = "Stefan";

      this.bb.write(Zone.DEFAULT, entry);
      try {
         this.bb.write(Zone.DEFAULT, entry);
         fail("exception expected when writing an entry again");
      } catch (WriteBlackboardException _) {
      }
   }

   public void test_clones_entries_before_writing_them()
   {
      String entry = "Stefan";

      Mock cloneStrategy = mock(CloneStrategy.class);
      cloneStrategy.expects(once()).method("clone").with(same(entry)).will(
               returnValue(new String(entry)));

      this.bb = new ObjectBlackboard((CloneStrategy)cloneStrategy.proxy());

      this.bb.write(Zone.DEFAULT, entry);

      cloneStrategy.verify();
   }
}
