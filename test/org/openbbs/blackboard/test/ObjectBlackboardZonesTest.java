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
import org.openbbs.blackboard.BlackboardZoneException;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.ReadBlackboardException;
import org.openbbs.blackboard.WriteBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.filter.AnyObjectFilter;
import org.openbbs.blackboard.filter.EqualObjectFilter;

/**
 * @author stefan
 */
public class ObjectBlackboardZonesTest extends MockObjectTestCase
{
   private final Zone zone1 = new NamedZone("ZONE_1");
   private final Zone zone2 = new NamedZone("ZONE_2");
   private final ZoneSelector zone1Selector;
   private final ZoneSelector zone2Selector;
   private Blackboard bb = null;

   public ObjectBlackboardZonesTest()
   {
      this.zone1Selector = new ExactZoneSelector(zone1);
      this.zone2Selector = new ExactZoneSelector(zone2);
   }

   protected void setUp() throws Exception
   {
      this.bb = new ObjectBlackboard(new TestCloneStrategy());
      this.bb.openZone(zone1);
      this.bb.openZone(zone2);
   }

   public void test_must_open_a_zone_before_we_can_write_into_it()
   {
      Zone fooZone = new NamedZone("FooZone");

      try {
         this.bb.write(fooZone, "Hello World");
         fail("should throw an exception if an entry is written to an unknown zone");
      } catch (BlackboardZoneException exception) {
         // expected
      }

      this.bb.openZone(fooZone);
      try {
         this.bb.write(fooZone, "Hello World");
      } catch (BlackboardZoneException exception) {
         fail("zone is unknown but has been opened");
      }

   }

   public void test_cannot_open_a_zone_more_than_once()
   {
      Zone fooZone = new NamedZone("FooZone");
      this.bb.openZone(fooZone);
      try {
         this.bb.openZone(fooZone);
         fail("it should not be possible to open a zone twice");
      } catch (BlackboardZoneException exception) {
         // expected
      }
   }

   public void test_default_zone_is_always_open()
   {
      try {
         this.bb.write(Zone.DEFAULT, "Hello World");
      } catch (BlackboardZoneException exc) {
         fail("it should be possible to write to default zone without opening it");
      }
   }

   public void test_read_calls_zone_selector()
   {
      this.bb.write(zone1, "Hello World");

      Mock selector = mock(ZoneSelector.class);
      selector.expects(atLeastOnce()).method("selects").with(isA(Zone.class)).will(returnValue(true));

      this.bb.read((ZoneSelector)selector.proxy(), new EqualObjectFilter("Hello World"));

      selector.verify();
   }

   public void test_entries_written_to_one_zone_are_not_found_in_another_by_read()
   {
      this.bb.write(zone1, "Hello World!");
      this.bb.write(zone2, "Hello Again!");
      assertTrue(this.bb.exists(zone1Selector, new EqualObjectFilter("Hello World!")));
      assertTrue(this.bb.exists(zone2Selector, new EqualObjectFilter("Hello Again!")));
      assertFalse(this.bb.exists(zone1Selector, new EqualObjectFilter("Hello Again!")));
      assertFalse(this.bb.exists(zone2Selector, new EqualObjectFilter("Hello World!")));
      assertTrue(this.bb.exists(ZoneSelector.ALL_ZONES, new EqualObjectFilter("Hello World!")));
      assertTrue(this.bb.exists(ZoneSelector.ALL_ZONES, new EqualObjectFilter("Hello Again!")));
   }

   public void test_entries_written_to_one_zone_are_not_found_in_another_by_readAll()
   {
      this.bb.write(zone1, "Hello World!");
      this.bb.write(zone2, "Hello World Again");

      EntryFilter filter = new EntryFilter() {
         public boolean selects(Object entry)
         {
            return entry.toString().startsWith("Hello");
         }
      };
      assertEquals(1, this.bb.readAll(zone1Selector, filter).size());
      assertEquals(1, this.bb.readAll(zone2Selector, filter).size());
      assertEquals(2, this.bb.readAll(ZoneSelector.ALL_ZONES, filter).size());
   }

   public void test_knows_zone_of_entries()
   {
      this.bb.write(zone1, "Hello World");
      this.bb.write(zone2, "Hello Again");

      assertEquals(zone1, this.bb.zoneOf("Hello World"));
      assertEquals(zone2, this.bb.zoneOf("Hello Again"));

      try {
         this.bb.zoneOf("Unknown Entry");
         fail("exception expected when getting zone of an entry not on the blackboard");
      } catch (ReadBlackboardException exception) {
         // expected
      }
   }

   public void test_entry_may_not_exist_twice_even_in_different_zones()
   {
      this.bb.write(zone1, "Hello World");
      try {
         this.bb.write(zone2, "Hello World");
         fail("exception expected when an entry is written to the blackboard for the second time");
      } catch (WriteBlackboardException exception) {
         // expected
      }
   }

   public void test_notifications_are_send_with_zones()
   {
      this.bb.write(zone1, "Hello World");

      Mock observer = mock(BlackboardObserver.class);
      observer.expects(once()).method("blackboardDidAddEntry").with(same(this.bb), eq(zone2),
               eq("Hello Again"));

      observer.expects(once()).method("blackboardDidRemoveEntry").with(same(this.bb), eq(zone1),
               eq("Hello World"));

      this.bb.registerInterest(ZoneSelector.ALL_ZONES, (BlackboardObserver)observer.proxy());

      this.bb.write(zone2, "Hello Again");
      this.bb.take(zone1Selector, new EqualObjectFilter("Hello World"));

      observer.verify();
   }

   public void test_notifications_can_be_restricted_to_zones()
   {
      Mock observer1 = mock(BlackboardObserver.class);
      observer1.expects(once()).method("blackboardDidAddEntry").with(same(this.bb), eq(zone1),
               eq("Hello World"));

      Mock observer2 = mock(BlackboardObserver.class);
      observer2.expects(once()).method("blackboardDidAddEntry").with(same(this.bb), eq(zone1),
               eq("Hello World"));

      Mock observer3 = mock(BlackboardObserver.class);
      observer3.expects(never()).method("blackboardDidAddEntry").withAnyArguments();

      this.bb.registerInterest(zone1Selector, (BlackboardObserver)observer1.proxy());
      this.bb.registerInterest(ZoneSelector.ALL_ZONES, (BlackboardObserver)observer2.proxy());
      this.bb.registerInterest(zone2Selector, (BlackboardObserver)observer3.proxy());

      this.bb.write(zone1, "Hello World");

      observer1.verify();
      observer2.verify();
      observer3.verify();
   }

   public void test_closing_a_zone_removes_all_objects_in_this_zone()
   {
      this.bb.write(zone1, "Stefan");
      this.bb.write(zone1, "Doreen");
      this.bb.write(zone2, "Monte");

      this.bb.closeZone(zone1);

      assertEquals(1, this.bb.readAll(ZoneSelector.ALL_ZONES, new AnyObjectFilter()).size());
      assertEquals("Monte", this.bb.read(ZoneSelector.ALL_ZONES, new AnyObjectFilter()));
   }

   public void test_cannot_write_to_a_closed_zone()
   {
      this.bb.closeZone(zone1);

      try {
         this.bb.write(zone1, "Hello World");
         fail("it must not be possible to write to a closed zone");
      } catch (BlackboardZoneException exception) {
         // expected
      }
   }

   public void test_cannot_close_unknown_zone()
   {
      try {
         this.bb.closeZone(new NamedZone("fooZone"));
         fail("it must not be possible to close an unknown zone");
      } catch (BlackboardZoneException exception) {
         // expected
      }
   }
}
