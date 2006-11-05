/*
 * ControlKnowledgeSourceInteractionTest.java
 *
 * Copyright (C) Dec 21, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard.test;

import junit.framework.AssertionFailedError;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.builder.NameMatchBuilder;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.control.BlackboardControl;
import org.openbbs.blackboard.control.DefaultControlDriver;
import org.openbbs.blackboard.filter.AnyObjectFilter;
import org.openbbs.blackboard.filter.EqualObjectFilter;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.blackboard.plan.testsupport.AlwaysSameSourcesStrategy;
import org.openbbs.blackboard.plan.testsupport.OneStepPlan;

/**
 * @author stefan
 */
public class ControlKnowledgeSourceInteractionTest extends MockObjectTestCase
{
   private Blackboard bb = null;
   private BlackboardControl control = null;
   private AlwaysSameSourcesStrategy ksSelectionStrategy = null;

   protected void setUp()
   {
      this.bb = new ObjectBlackboard(new TestCloneStrategy());
      this.ksSelectionStrategy = new AlwaysSameSourcesStrategy();
      this.control = new BlackboardControl();
      this.control.setBlackboard(this.bb);
      this.control.setZone(Zone.DEFAULT);
      this.control.setPlan(new OneStepPlan());
      this.control.setKSSelectStrategy(this.ksSelectionStrategy);
      this.control.setDriver(new DefaultControlDriver(this.bb));
   }

   public void test_control_asks_knowledge_sources_if_they_are_enabled_when_bb_is_updated()
   {
      Mock sources[] = new Mock[2];
      for (int i = 0; i < sources.length; i++) {
         sources[i] = mock(KnowledgeSource.class);
         sources[i].expects(once()).method("isEnabled").withAnyArguments().will(returnValue(false));
         this.ksSelectionStrategy.addKnowledgeSource((KnowledgeSource)sources[i].proxy());
      }

      this.bb.write(Zone.DEFAULT, "Some Data");

      for (Mock source : sources)
         source.verify();
   }

   public void test_control_plan_provides_execution_context_to_knowledge_sources()
   {
      Mock source = mock(KnowledgeSource.class);
      source.expects(once()).method("isEnabled").with(isA(KSExecutionContext.class)).will(
               returnValue(true)).id("first");
      source.expects(once()).method("execute").with(isA(KSExecutionContext.class));
      ((NameMatchBuilder)source.expects(once()).after("first")).method("isEnabled").will(
               returnValue(false));

      this.ksSelectionStrategy.addKnowledgeSource((KnowledgeSource)source.proxy());
      this.bb.write(Zone.DEFAULT, "Hello, World!");

      source.verify();
   }

   public void test_object_on_blackboard_is_accessible_through_execution_context()
   {
      final String object = "Hello, Blackboard!";
      this.ksSelectionStrategy.addKnowledgeSource(new KnowledgeSource() {
         private boolean didExecute = false;

         public boolean isEnabled(KSExecutionContext context)
         {
            return !didExecute;
         }

         public void execute(KSExecutionContext context)
         {
            assertEquals(object, context.blackboard().read(new EqualObjectFilter(object)));
            this.didExecute = true;
         }
      });
      
      this.bb.write(Zone.DEFAULT, object);
   }

   public void test_control_runs_as_long_as_there_is_at_least_one_source_which_executes()
   {
      Mock source = mock(KnowledgeSource.class);

      source.expects(once()).method("isEnabled").withAnyArguments().will(returnValue(true)).id(
               "canContribute1");

      ((NameMatchBuilder)source.expects(once()).after("canContribute1")).method("execute")
               .withAnyArguments().id("first");

      ((NameMatchBuilder)source.expects(once()).after("first")).method("isEnabled")
               .withAnyArguments().will(returnValue(true)).id("second");

      ((NameMatchBuilder)source.expects(once()).after("second")).method("execute")
               .withAnyArguments().id("third");

      ((NameMatchBuilder)source.expects(once()).after("third")).method("isEnabled")
               .withAnyArguments().will(returnValue(false));

      this.ksSelectionStrategy.addKnowledgeSource((KnowledgeSource)source.proxy());

      this.bb.write(Zone.DEFAULT, "Some Data");

      source.verify();
   }

   public void test_nested_executions_are_avoided()
   {
      EntryContributer source1 = new EntryContributer(this.bb, new ExactZoneSelector(Zone.DEFAULT))
               .nextContributionIs(new AnyObjectFilter(), "Some more Data");
      this.ksSelectionStrategy.addKnowledgeSource(source1);

      EntryContributer source2 = new EntryContributer(this.bb, new ExactZoneSelector(Zone.DEFAULT))
               .nextContributionIs(new EntryFilter() {
                  public boolean selects(Object entry)
                  {
                     return entry.toString().equals("Some more Data");
                  }
               }, "Even more Data");
      this.ksSelectionStrategy.addKnowledgeSource(source2);

      this.bb.write(Zone.DEFAULT, "Some Data");

      assertEquals(1, source1.timesContributed());
      assertEquals(1, source2.timesContributed());
   }

   private static class EntryContributer implements KnowledgeSource
   {
      private static boolean contributionActive = false;

      private Blackboard blackboard = null;
      private ZoneSelector zoneSelector = null;
      private Object nextContribution = null;
      private EntryFilter condition = null;
      private int timesContributed = 0;

      public EntryContributer(Blackboard blackboard, ZoneSelector zoneSelector)
      {
         this.blackboard = blackboard;
         this.zoneSelector = zoneSelector;
      }

      public EntryContributer nextContributionIs(EntryFilter condition, Object object)
      {
         this.condition = condition;
         this.nextContribution = object;
         return this;
      }

      public boolean isEnabled(KSExecutionContext context)
      {
         return (this.nextContribution != null && this.blackboard
                  .read(zoneSelector, this.condition) != null);
      }

      public int timesContributed()
      {
         return this.timesContributed;
      }

      public void execute(KSExecutionContext context)
      {
         if (contributionActive)
            throw new AssertionFailedError("another knowledge source is currently contributing");

         contributionActive = true;
         Object entry = this.nextContribution;
         this.nextContribution = null;
         this.blackboard.write(Zone.DEFAULT, entry);
         this.timesContributed++;
         contributionActive = false;
      }
   }
}
