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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.blackboard.plan.CreditabilityBasedStrategy;
import org.openbbs.blackboard.plan.NoStep;
import org.openbbs.blackboard.plan.PlanStep;
import org.openbbs.blackboard.plan.testsupport.NamedPlanStep;

/**
 * @author sks
 */
public class CreditabilityBasedStrategyTest extends TestCase
{
   private CreditabilityBasedStrategy strategy = null;
   private PlanStep step1 = new NamedPlanStep("STEP_1");
   private PlanStep step2 = new NamedPlanStep("STEP_2");
   private PlanStep step3 = new NamedPlanStep("STEP_3");
   private KnowledgeSource source1 = new DummySource();
   private KnowledgeSource source2 = new DummySource();
   private KnowledgeSource source3 = new DummySource();
   private KnowledgeSource source4 = new DummySource();

   protected void setUp()
   {
      this.strategy = new CreditabilityBasedStrategy();
      this.strategy.addKnowledgeSource(step1, source1, 80);
      this.strategy.addKnowledgeSource(step1, source2, 100);
      this.strategy.addKnowledgeSource(step1, source3, 50);
      this.strategy.addKnowledgeSource(step2, source1, 100);
      this.strategy.addKnowledgeSource(step2, source4, 90);
   }

   public void test_returns_sources_ordered_by_creditability()
   {
      List<KnowledgeSource> expectedSources = new ArrayList<KnowledgeSource>();
      expectedSources.add(source2);
      expectedSources.add(source1);
      expectedSources.add(source3);
      assertEquals(expectedSources, this.strategy.getKnowledgeSources(step1));
   }

   public void test_returns_empty_list_if_no_source_is_applicable_for_a_step()
   {
      assertTrue(this.strategy.getKnowledgeSources(step3).isEmpty());
   }

   public void test_cannot_assign_a_source_a_creditability_for_no_step()
   {
      try {
         this.strategy.addKnowledgeSource(NoStep.instance, this.source1, 10);
         fail("it shouldn't be possible to assign a source a creditability for NoStep");
      } catch (IllegalArgumentException _) {
         // expected
      }
   }

   private class DummySource implements KnowledgeSource
   {
      public void attach(Blackboard blackboard, ZoneSelector zoneSelector)
      {
      }

      public boolean isEnabled(KSExecutionContext context)
      {
         return false;
      }

      public void execute(KSExecutionContext context)
      {
      }
   }
}
