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
import org.openbbs.blackboard.CloneByMethodStrategy;

/**
 * @author stefan
 */
public class CloneByMethodStrategyTest extends MockObjectTestCase
{
   public void test_sends_clone_message_to_object()
   {
      Mock object = mock(CloneableObject.class);
      String clone = "Cloned Object";
      object.expects(once()).method("cloneMe").withNoArguments().will(returnValue(clone));

      Object clonedObject = new CloneByMethodStrategy("cloneMe").clone(object.proxy());

      object.verify();
      assertSame(clone, clonedObject);
   }

   public void test_returns_null_for_null_objects()
   {
      assertNull(new CloneByMethodStrategy("cloneMe").clone(null));
   }

   public void test_throws_exception_if_object_does_not_respond_to_clone_message()
   {
      try {
         new CloneByMethodStrategy("cloneMe").clone("Stefan");
         fail("exception expected if cloned object doesn't respond to clone message");
      } catch (UnsupportedOperationException _) {
      }
   }

   private static interface CloneableObject
   {
      public Object cloneMe();
   }
}
