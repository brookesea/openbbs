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

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests
{
   public static Test suite()
   {
      TestSuite suite = new TestSuite("Test for org.openbbs.blackboard.test");
      suite.addTestSuite(ReadEntryFromBlackboardTest.class);
      suite.addTestSuite(ObjectBlackboardZonesTest.class);
      suite.addTestSuite(ControlControlPlanInteractionTest.class);
      suite.addTestSuite(WriteEntryToObjectBlackboardTest.class);
      suite.addTestSuite(ControlKnowledgeSourceInteractionTest.class);
      suite.addTestSuite(CreditabilityBasedStrategyTest.class);
      suite.addTestSuite(CloneByMethodStrategyTest.class);
      suite.addTestSuite(TakeEntryFromBlackboardTest.class);
      suite.addTestSuite(RestorePrevalenceMemoryTest.class);
      suite.addTestSuite(SnapshotterTest.class);
      suite.addTestSuite(ReopenSimpleLogFileTest.class);
      suite.addTestSuite(PrevalenceIntegrationTest.class);
      suite.addTestSuite(AccessLockedPrevalenceMemoryTest.class);
      return suite;
   }
}
