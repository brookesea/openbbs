/*
 * ControlControlPlanInteractionTest.java
 *
 * Copyright (C) Dec 28, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.test;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.builder.NameMatchBuilder;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardControl;
import org.openbbs.blackboard.ControlDriver;
import org.openbbs.blackboard.DefaultControlDriver;
import org.openbbs.blackboard.KnowledgeSource;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.plan.KnowledgeSourceSelectionStrategy;
import org.openbbs.blackboard.plan.NoStep;
import org.openbbs.blackboard.plan.PlanStep;
import org.openbbs.blackboard.plan.rulebased.PlanStepCondition;
import org.openbbs.blackboard.plan.rulebased.RuleBasedControlPlan;

/**
 * @author stefan
 */
public class ControlControlPlanInteractionTest extends MockObjectTestCase
{
   public void test_control_asks_its_plan_to_select_a_source()
   {
      Blackboard bb = new ObjectBlackboard(new TestCloneStrategy());

      StubSource source1 = new StubSource(true);
      StubSource source2 = new StubSource(false);
      StubSource source3 = new StubSource(true);

      Mock stepCondition = mock(PlanStepCondition.class);
      stepCondition.expects(once()).method("evaluate").will(returnValue(true)).id("condition1");
      ((NameMatchBuilder)stepCondition.expects(once()).after("condition1")).method("evaluate")
               .will(returnValue(true)).id("condition2");
      ((NameMatchBuilder)stepCondition.expects(once()).after("condition2")).method("evaluate")
               .will(returnValue(false));
      ((NameMatchBuilder)stepCondition.expects(once()).after("condition2")).method("evaluate")
               .will(returnValue(false));

      RuleBasedControlPlan plan = new RuleBasedControlPlan();
      PlanStep step1 = plan.step("STEP_1").when((PlanStepCondition)stepCondition.proxy());
      PlanStep step2 = plan.step("STEP_2").when((PlanStepCondition)stepCondition.proxy());

      List<KnowledgeSource> expectedSources = new ArrayList<KnowledgeSource>();
      expectedSources.add(source1);
      expectedSources.add(source3);

      Mock strategy = mock(KnowledgeSourceSelectionStrategy.class);
      strategy.expects(once()).method("selectBestSource").with(same(step1), eq(expectedSources))
               .will(returnValue(null));
      strategy.expects(once()).method("selectBestSource").with(same(step2), eq(expectedSources))
               .will(returnValue(source3));
      strategy.expects(once()).method("selectBestSource").with(eq(NoStep.instance),
               eq(expectedSources)).will(returnValue(null));

      BlackboardControl control = new BlackboardControl(bb, Zone.DEFAULT, plan,
               (KnowledgeSourceSelectionStrategy)strategy.proxy());
      control.setDriver(new DefaultControlDriver(bb));
      control.addKnowledgeSource(source1);
      control.addKnowledgeSource(source2);
      control.addKnowledgeSource(source3);

      bb.write(Zone.DEFAULT, "Hello World");

      assertFalse(source1.didContribute());
      assertFalse(source2.didContribute());
      assertTrue(source3.didContribute());

      stepCondition.verify();
      strategy.verify();
   }

   public void test_control_termiantes_if_plan_desires_termination()
   {
      Blackboard bb = new ObjectBlackboard(new TestCloneStrategy());

      StubSource source = new StubSource(true);

      Mock stepCondition = mock(PlanStepCondition.class);
      stepCondition.expects(once()).method("evaluate").will(returnValue(true));

      RuleBasedControlPlan plan = new RuleBasedControlPlan();
      plan.step("TERMINATE").terminates(true).when((PlanStepCondition)stepCondition.proxy());

      Mock strategy = mock(KnowledgeSourceSelectionStrategy.class);
      strategy.expects(atLeastOnce()).method("selectBestSource").withAnyArguments().will(
               returnValue(source));

      BlackboardControl control = new BlackboardControl(bb, Zone.DEFAULT, plan,
               (KnowledgeSourceSelectionStrategy)strategy.proxy());
      control.addKnowledgeSource(source);

      Mock driver = mock(ControlDriver.class);
      driver.expects(once()).method("attachControl").with(same(control));
      driver.expects(once()).method("detachControl").with(same(control));

      control.setDriver((ControlDriver)driver.proxy());
      control.nextRun();

      assertTrue(source.didContribute());
      stepCondition.verify();
      strategy.verify();
      driver.verify();
   }

   private class StubSource implements KnowledgeSource
   {
      private boolean contribute = false;
      private boolean didContribute = false;

      public StubSource(boolean shouldContribute)
      {
         this.contribute = shouldContribute;
      }

      public void attach(Blackboard blackboard, ZoneSelector zoneSelector)
      {
      }

      public boolean canContribute()
      {
         return this.contribute;
      }

      public void contribute()
      {
         assertFalse("source did already contribute", this.didContribute);
         this.didContribute = true;
      }

      public boolean didContribute()
      {
         return this.didContribute;
      }
   }
}
