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
      } catch (WriteBlackboardException exception) {
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
