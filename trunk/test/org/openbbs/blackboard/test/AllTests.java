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
