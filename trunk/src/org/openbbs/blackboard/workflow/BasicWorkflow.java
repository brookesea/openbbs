/*
 * BasicWorkflow.java
 *
 * Copyright by Stefan Kleine Stegemann, 2006
 */
package org.openbbs.blackboard.workflow;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.BlackboardControl;
import org.openbbs.blackboard.ExactZoneSelector;
import org.openbbs.blackboard.KnowledgeSource;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ZoneSelector;
import org.openbbs.blackboard.plan.ControlPlan;
import org.openbbs.blackboard.plan.KnowledgeSourceSelectionStrategy;
import org.openbbs.blackboard.plan.rulebased.RuleBasedControlPlan;
import org.openbbs.util.ClassnameHelper;

/**
 * @author sks
 */
public abstract class BasicWorkflow implements Workflow
{
   private final Blackboard blackboard;
   private final ZoneSelector zoneSelector;

   protected BasicWorkflow(Blackboard blackboard, ZoneSelector zoneSelector)
   {
      Validate.notNull(blackboard);
      Validate.notNull(zoneSelector);
      this.blackboard = blackboard;
      this.zoneSelector = zoneSelector;
   }

   protected BasicWorkflow(Blackboard blackboard, Zone zone)
   {
      this(blackboard, new ExactZoneSelector(zone));
      this.blackboard.openZone(zone);
   }

   public final BlackboardControl setupControl()
   {
      String planName = ClassnameHelper.nameWithoutPackage(this.getClass()) + "Plan";
      ControlPlan plan = new RuleBasedControlPlan(planName);
      this.setupPlan(plan, this.blackboard, this.zoneSelector);

      List<KnowledgeSource> sources = new ArrayList<KnowledgeSource>();
      KnowledgeSourceSelectionStrategy selectionStrategy = this
               .setupKnowledgeSources(plan, sources);

      BlackboardControl control = new BlackboardControl(this.blackboard, this.zoneSelector, plan,
               selectionStrategy);
      for (KnowledgeSource source : sources)
         control.addKnowledgeSource(source);

      return control;
   }

   protected abstract void setupPlan(ControlPlan plan, Blackboard blackboard,
            ZoneSelector zoneSelector);

   protected abstract KnowledgeSourceSelectionStrategy setupKnowledgeSources(ControlPlan plan,
            List<KnowledgeSource> sources);
}
