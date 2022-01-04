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

import java.util.Set;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.CloneStrategy;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.WriteBlackboardException;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.AnyObjectFilter;

/**
 * @author sks
 */
public class ReadEntryFromBlackboardTest extends MockObjectTestCase
{
   private Blackboard bb = null;

   public void setUp()
   {
      this.bb = new ObjectBlackboard(new TestCloneStrategy());
   }

   public void test_calls_entry_filter_for_each_object_and_returns_selected_object()
   {
      this.bb.write(Zone.DEFAULT, "Stefan");
      this.bb.write(Zone.DEFAULT, "Doreen");

      Mock filter = mock(EntryFilter.class);
      filter.expects(once()).method("selects").with(eq("Stefan")).will(returnValue(false));
      filter.expects(once()).method("selects").with(eq("Doreen")).will(returnValue(false));

      bb.read(new ExactZoneSelector(Zone.DEFAULT), (EntryFilter)filter.proxy());

      filter.verify();
   }

   public void test_returns_null_if_a_filter_does_not_select_any_object()
   {
      this.bb.write(Zone.DEFAULT, "Stefan");
      this.bb.write(Zone.DEFAULT, "Doreen");

      Object entry = bb.read(new ExactZoneSelector(Zone.DEFAULT), new EntryFilter() {
         public boolean selects(Object entry)
         {
            return false;
         }
      });
      assertNull(entry);
   }

   public void test_read_returns_selected_object()
   {
      this.bb.write(Zone.DEFAULT, "Stefan");
      this.bb.write(Zone.DEFAULT, "Doreen");

      Object entry = bb.read(new ExactZoneSelector(Zone.DEFAULT), new EntryFilter() {
         public boolean selects(Object entry)
         {
            return entry.equals("Doreen");
         }
      });

      assertEquals("Doreen", entry);
   }

   public void test_exists_returns_true_if_at_least_one_entry_is_selected()
   {
      this.bb.write(Zone.DEFAULT, "Stefan Kleine Stegemann");
      this.bb.write(Zone.DEFAULT, "Doreen Kleine Stegemann");

      assertTrue(this.bb.exists(new ExactZoneSelector(Zone.DEFAULT), new EntryFilter() {
         public boolean selects(Object entry)
         {
            return entry.toString().endsWith("Kleine Stegemann");
         }
      }));
   }

   public void test_readAll_returns_all_selected_objects()
   {
      this.bb.write(Zone.DEFAULT, "Stefan Kleine Stegemann");
      this.bb.write(Zone.DEFAULT, "Doreen Kleine Stegemann");
      this.bb.write(Zone.DEFAULT, "Monte Miersch");

      Set<Object> entries = bb.readAll(new ExactZoneSelector(Zone.DEFAULT), new EntryFilter() {
         public boolean selects(Object entry)
         {
            return entry.toString().endsWith("Kleine Stegemann");
         }
      });

      assertEquals(2, entries.size());
      assertTrue(entries.contains("Stefan Kleine Stegemann"));
      assertTrue(entries.contains("Doreen Kleine Stegemann"));
   }

   public void test_entries_returned_by_read_are_cloned()
   {
      String entry = "Stefan";
      String writtenEntry = new String(entry);
      String clonedEntry = new String(entry);

      Mock cloneStrategy = mock(CloneStrategy.class);
      cloneStrategy.expects(once()).method("clone").with(same(entry)).will(
               returnValue(writtenEntry)); // write
      cloneStrategy.expects(once()).method("clone").with(same(writtenEntry)).will(
               returnValue(clonedEntry)); // read

      this.bb = new ObjectBlackboard((CloneStrategy)cloneStrategy.proxy());

      this.bb.write(Zone.DEFAULT, entry);
      Object readEntry = this.bb.read(new ExactZoneSelector(Zone.DEFAULT), new AnyObjectFilter());

      cloneStrategy.verify();

      assertNotSame(entry, readEntry);
      assertEquals(entry, readEntry);
   }

   public void test_cannot_write_back_a_read_entry()
   {
      this.bb.write(Zone.DEFAULT, "Stefan");
      Object entry = bb.read(new ExactZoneSelector(Zone.DEFAULT), new AnyObjectFilter());
      try {
         this.bb.write(Zone.DEFAULT, entry);
         fail("exception expected when writing back an object that was read from the blackboard");
      } catch (WriteBlackboardException exception) {
      }
   }
}
