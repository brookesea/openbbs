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
import org.openbbs.blackboard.BlackboardControl;
import org.openbbs.blackboard.DefaultControlDriver;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.blackboard.KnowledgeSource;
import org.openbbs.blackboard.ObjectBlackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.filter.AnyObjectFilter;
import org.openbbs.blackboard.plan.testsupport.FirstSourceStrategy;
import org.openbbs.blackboard.plan.testsupport.OneStepPlan;

/**
 * @author stefan
 */
public class ControlKnowledgeSourceInteractionTest extends MockObjectTestCase
{
   private Blackboard bb = null;
   private BlackboardControl control = null;

   protected void setUp()
   {
      this.bb = new ObjectBlackboard(new TestCloneStrategy());
      this.control = new BlackboardControl(this.bb, Zone.DEFAULT, new OneStepPlan(),
               new FirstSourceStrategy());
      this.control.setDriver(new DefaultControlDriver(this.bb));
   }

   public void test_blackboard_is_attached_to_knowledge_sources()
   {
      Mock source = mock(KnowledgeSource.class);
      source.expects(once()).method("attach").with(same(this.bb), isA(ZoneSelector.class));

      this.control.addKnowledgeSource((KnowledgeSource)source.proxy());

      source.verify();
   }

   public void test_control_asks_knowledge_sources_if_they_can_contribute_when_bb_is_updated()
   {
      Mock sources[] = new Mock[2];
      for (int i = 0; i < sources.length; i++) {
         sources[i] = mock(KnowledgeSource.class);
         sources[i].expects(once()).method("attach").withAnyArguments();
         sources[i].expects(once()).method("canContribute").withNoArguments().will(
                  returnValue(false));
         this.control.addKnowledgeSource((KnowledgeSource)sources[i].proxy());
      }

      this.bb.write(Zone.DEFAULT, "Some Data");

      for (Mock source : sources)
         source.verify();
   }

   public void test_control_runs_as_long_as_there_is_at_least_one_source_which_contributes()
   {
      Mock source = mock(KnowledgeSource.class);

      source.expects(once()).method("attach").with(same(this.bb), isA(ZoneSelector.class));

      source.expects(once()).method("canContribute").withNoArguments().will(returnValue(true)).id(
               "canContribute1");

      ((NameMatchBuilder)source.expects(once()).after("canContribute1")).method("contribute")
               .withNoArguments().id("contribute1");

      ((NameMatchBuilder)source.expects(once()).after("contribute1")).method("canContribute")
               .withNoArguments().will(returnValue(true)).id("canContribute2");

      ((NameMatchBuilder)source.expects(once()).after("canContribute2")).method("contribute")
               .withNoArguments().id("contribute2");

      ((NameMatchBuilder)source.expects(once()).after("contribute2")).method("canContribute")
               .withNoArguments().will(returnValue(false));

      this.control.addKnowledgeSource((KnowledgeSource)source.proxy());

      this.bb.write(Zone.DEFAULT, "Some Data");

      source.verify();
   }

   public void test_nested_contributions_are_avoided()
   {
      EntryContributer source1 = new EntryContributer().nextContributionIs(new AnyObjectFilter(),
               "Some more Data");
      this.control.addKnowledgeSource(source1);

      EntryContributer source2 = new EntryContributer().nextContributionIs(new EntryFilter() {
         public boolean selects(Object entry)
         {
            return entry.toString().equals("Some more Data");
         }
      }, "Even more Data");
      this.control.addKnowledgeSource(source2);

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

      public EntryContributer nextContributionIs(EntryFilter condition, Object object)
      {
         this.condition = condition;
         this.nextContribution = object;
         return this;
      }

      public void attach(Blackboard blackboard, ZoneSelector zoneSelector)
      {
         this.blackboard = blackboard;
         this.zoneSelector = zoneSelector;
      }

      public boolean canContribute()
      {
         return (this.nextContribution != null && this.blackboard
                  .read(zoneSelector, this.condition) != null);
      }

      public int timesContributed()
      {
         return this.timesContributed;
      }

      public void contribute()
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
