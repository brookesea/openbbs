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
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.control.BlackboardControl;
import org.openbbs.blackboard.control.ControlDriver;
import org.openbbs.blackboard.control.DefaultControlDriver;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.blackboard.plan.KSSelectionStrategy;
import org.openbbs.blackboard.plan.NoStep;
import org.openbbs.blackboard.plan.PlanStep;
import org.openbbs.blackboard.plan.rulebased.PlanStepCondition;
import org.openbbs.blackboard.plan.rulebased.RuleBasedControlPlan;

/**
 * @author stefan
 */
public class ControlControlPlanInteractionTest extends MockObjectTestCase
{
   public void test_control_asks_its_strategy_to_select_sources()
   {
      Blackboard bb = new ObjectBlackboard(new TestCloneStrategy());

      RuleBasedControlPlan plan = new RuleBasedControlPlan();

      Mock step1Condition = mock(PlanStepCondition.class);
      step1Condition.expects(once()).method("evaluate").will(returnValue(true)).id("c1");
      ((NameMatchBuilder)step1Condition.expects(once()).after("c1")).method("evaluate").will(returnValue(false)).id("c2");
      ((NameMatchBuilder)step1Condition.expects(once()).after("c2")).method("evaluate").will(returnValue(false));
      PlanStep step1 = plan.step("STEP_1").when((PlanStepCondition)step1Condition.proxy());

      Mock step2Condition = mock(PlanStepCondition.class);
      step2Condition.expects(once()).method("evaluate").will(returnValue(false)).id("c1");
      ((NameMatchBuilder)step2Condition.expects(once()).after("c1")).method("evaluate").will(returnValue(true)).id("c2");
      ((NameMatchBuilder)step2Condition.expects(once()).after("c2")).method("evaluate").will(returnValue(false));
      PlanStep step2 = plan.step("STEP_2").when((PlanStepCondition)step2Condition.proxy());

      List<StubSource> sourcesStep1 = new ArrayList<StubSource>();
      sourcesStep1.add(new StubSource(false));
      sourcesStep1.add(new StubSource(true));

      List<StubSource> sourcesStep2 = new ArrayList<StubSource>();
      sourcesStep2.add(new StubSource(true));
      sourcesStep2.add(new StubSource(true));

      Mock strategy = mock(KSSelectionStrategy.class);
      strategy.expects(once()).method("getKnowledgeSources").with(same(step1)).will(
               returnValue(sourcesStep1));
      strategy.expects(once()).method("getKnowledgeSources").with(same(step2)).will(
               returnValue(sourcesStep2));
      strategy.expects(once()).method("getKnowledgeSources").with(eq(NoStep.instance)).will(
               returnValue(null));

      BlackboardControl control = new BlackboardControl();
      control.setBlackboard(bb);
      control.setZone(Zone.DEFAULT);
      control.setPlan(plan);
      control.setKSSelectStrategy((KSSelectionStrategy)strategy.proxy());
      control.setDriver(new DefaultControlDriver(bb));
      bb.write(Zone.DEFAULT, "Hello World");

      step1Condition.verify();
      step2Condition.verify();
      strategy.verify();

      assertFalse(sourcesStep1.get(0).didContribute());
      assertTrue(sourcesStep1.get(1).didContribute());
      assertTrue(sourcesStep2.get(0).didContribute());
      assertFalse(sourcesStep2.get(1).didContribute());
   }

   public void test_control_termiantes_if_plan_desires_termination()
   {
      Mock stepCondition = mock(PlanStepCondition.class);
      stepCondition.expects(once()).method("evaluate").will(returnValue(true));

      RuleBasedControlPlan plan = new RuleBasedControlPlan();
      plan.step("TERMINATE").terminates(true).when((PlanStepCondition)stepCondition.proxy());

      List<StubSource> sources = new ArrayList<StubSource>();
      sources.add(new StubSource(true));
      Mock strategy = mock(KSSelectionStrategy.class);
      strategy.expects(atLeastOnce()).method("getKnowledgeSources").withAnyArguments().will(returnValue(sources));

      BlackboardControl control = new BlackboardControl();
      control.setBlackboard(new ObjectBlackboard());
      control.setZone(new NamedZone("BlackHole"));
      control.setPlan(plan);
      control.setKSSelectStrategy((KSSelectionStrategy)strategy.proxy());

      Mock driver = mock(ControlDriver.class);
      driver.expects(once()).method("attachControl").with(same(control));
      driver.expects(once()).method("detachControl").with(same(control));

      control.setDriver((ControlDriver)driver.proxy());
      control.nextRun();

      assertTrue(sources.get(0).didContribute());
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

      public boolean isEnabled(KSExecutionContext context)
      {
         return this.contribute;
      }

      public void execute(KSExecutionContext context)
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
