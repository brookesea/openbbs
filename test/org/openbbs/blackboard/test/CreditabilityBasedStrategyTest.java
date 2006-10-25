/*
 * CreditabilityBasedStrategyTest.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.KnowledgeSource;
import org.openbbs.blackboard.ZoneSelector;
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
   private KnowledgeSource source1 = new DummySource();
   private KnowledgeSource source2 = new DummySource();
   private KnowledgeSource source3 = new DummySource();
   private KnowledgeSource source4 = new DummySource();

   protected void setUp()
   {
      this.strategy = new CreditabilityBasedStrategy();
      this.strategy.setCreditability(step1, source1, 80);
      this.strategy.setCreditability(step1, source2, 100);
      this.strategy.setCreditability(step1, source3, 50);
      this.strategy.setCreditability(step2, source1, 100);
      this.strategy.setCreditability(step2, source4, 90);
   }

   public void test_returns_null_for_empty_list_of_sources()
   {
      assertNull(this.strategy.selectBestSource(step1, new ArrayList<KnowledgeSource>()));
   }

   public void test_returns_source_with_hightest_creditability_for_a_step()
   {
      List<KnowledgeSource> sources = new ArrayList<KnowledgeSource>();
      sources.add(source1);
      sources.add(source2);
      sources.add(source3);
      sources.add(source4);
      assertSame(source2, this.strategy.selectBestSource(step1, sources));
   }

   public void test_returns_null_if_no_source_is_applicable_for_a_step()
   {
      List<KnowledgeSource> sources = new ArrayList<KnowledgeSource>();
      sources.add(source2);
      sources.add(source3);
      assertNull(this.strategy.selectBestSource(step2, sources));
   }

   public void test_cannot_assign_a_source_a_creditability_for_no_step()
   {
      try {
         this.strategy.setCreditability(NoStep.instance, this.source1, 10);
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

      public boolean canContribute()
      {
         return false;
      }

      public void contribute()
      {
      }
   }
}
