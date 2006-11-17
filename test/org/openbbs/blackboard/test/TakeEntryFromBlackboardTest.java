/*
 * TakeEntryFromBlackboardTest.java
 *
 * Copyright (C) Dec 21, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.test;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardObserver;
import org.openbbs.blackboard.CloneStrategy;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.ReadBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.filter.AnyObjectFilter;
import org.openbbs.blackboard.filter.EqualObjectFilter;

/**
 * @author stefan
 */
public class TakeEntryFromBlackboardTest extends MockObjectTestCase
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
      observer.expects(once()).method("blackboardDidRemoveEntry").with(same(bb), eq(Zone.DEFAULT),
               eq(entry));

      this.bb.write(Zone.DEFAULT, entry);
      this.bb.registerInterest(ZoneSelector.ALL_ZONES, (BlackboardObserver)observer.proxy());
      this.bb.take(ZoneSelector.ALL_ZONES, new EqualObjectFilter(entry));

      observer.verify();
   }

   public void test_take_removes_entry_from_blackboard()
   {
      String entry = "Some Data";
      this.bb.write(Zone.DEFAULT, entry);
      this.bb.write(Zone.DEFAULT, "Some more Data");

      Mock filter = mock(EntryFilter.class);
      filter.expects(once()).method("selects").with(eq(entry)).will(returnValue(true));

      Object takenEntry = this.bb.take(ZoneSelector.ALL_ZONES, (EntryFilter)filter.proxy());

      assertEquals(entry, takenEntry);
      assertNull(this.bb.read(ZoneSelector.ALL_ZONES, new EqualObjectFilter(entry)));

      filter.verify();
   }

   public void test_taking_no_entry_raises_exception()
   {
      this.bb.write(Zone.DEFAULT, "Hello World");

      try {
         this.bb.take(ZoneSelector.ALL_ZONES, new EntryFilter() {
            public boolean selects(Object entry)
            {
               return false;
            }
         });
         fail("exception expected when take does not select any entry");
      } catch (ReadBlackboardException _) {
      }
   }

   public void test_taken_entries_are_not_cloned()
   {
      String entry = "Hello World";
      String clonedEntry = new String(entry);

      Mock cloneStrategy = mock(CloneStrategy.class);
      cloneStrategy.expects(once()).method("clone").with(same(entry))
               .will(returnValue(clonedEntry));
      cloneStrategy.expects(never()).method("clone").with(same(clonedEntry));

      this.bb = new ObjectBlackboard((CloneStrategy)cloneStrategy.proxy());
      this.bb.write(Zone.DEFAULT, entry);

      Object takenEntry = this.bb.take(ZoneSelector.ALL_ZONES, new AnyObjectFilter());

      assertSame(takenEntry, clonedEntry);
      cloneStrategy.verify();
   }
}
