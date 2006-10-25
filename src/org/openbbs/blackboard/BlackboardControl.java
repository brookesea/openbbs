/*
 * BlackboardControl.java
 *
 * Copyright (C) Dec 21, 2005 by Stefan Kleine Stegemann
 */
package org.openbbs.blackboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.plan.ControlPlan;
import org.openbbs.blackboard.plan.KnowledgeSourceSelectionStrategy;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * Monitors a blackboard and triggers knowledge sources when
 * the blackboard is updated. KnowlegdeSources are selected
 * by using a OldControlPlan.
 *
 * @author stefan
 */
public class BlackboardControl
{
   private String name = null;
   private boolean isRunning = false;
   private Blackboard blackboard = null;
   private ZoneSelector zoneSelector = null;
   private ControlPlan plan = null;
   private KnowledgeSourceSelectionStrategy selectionStrategy = null;
   private List<KnowledgeSource> knowledgeSources = new ArrayList<KnowledgeSource>();
   private List<BlackboardControlListener> listeners = new ArrayList<BlackboardControlListener>();
   private ControlDriver driver = null;

   public BlackboardControl(String name, Blackboard blackboard, ZoneSelector zoneSelector,
            ControlPlan plan, KnowledgeSourceSelectionStrategy selectionStrategy)
   {
      Validate.notNull(blackboard);
      Validate.notNull(zoneSelector);
      Validate.notNull(plan);
      Validate.notNull(selectionStrategy);
      Validate.notNull(name);

      this.blackboard = blackboard;
      this.zoneSelector = zoneSelector;
      this.plan = plan;
      this.selectionStrategy = selectionStrategy;
      this.name = name;
   }

   public BlackboardControl(Blackboard blackboard, ZoneSelector zoneSelector, ControlPlan plan,
            KnowledgeSourceSelectionStrategy selectionStrategy)
   {
      this("Control[" + plan.getName() + "]", blackboard, zoneSelector, plan, selectionStrategy);
   }

   public BlackboardControl(String name, Blackboard blackboard, Zone zone, ControlPlan plan,
            KnowledgeSourceSelectionStrategy selectionStrategy)
   {
      this(name, blackboard, new ExactZoneSelector(zone), plan, selectionStrategy);
   }

   public BlackboardControl(Blackboard blackboard, Zone zone, ControlPlan plan,
            KnowledgeSourceSelectionStrategy selectionStrategy)
   {
      this(blackboard, new ExactZoneSelector(zone), plan, selectionStrategy);
   }

   public String name()
   {
      return this.name;
   }

   public void addKnowledgeSource(KnowledgeSource knowledgeSource)
   {
      Validate.notNull(knowledgeSource);

      if (this.knowledgeSources.contains(knowledgeSource)) return;

      knowledgeSource.attach(blackboard, zoneSelector);
      this.knowledgeSources.add(knowledgeSource);
   }

   public void setDriver(ControlDriver driver)
   {
      if (this.driver != null) this.driver.detachControl(this);

      this.driver = null;
      driver.attachControl(this);
      this.driver = driver;
   }

   public void registerListener(BlackboardControlListener listener)
   {
      Validate.notNull(listener);
      if (!this.listeners.contains(listener)) this.listeners.add(listener);
   }

   public void removeListener(BlackboardControlListener listener)
   {
      Validate.notNull(listener);
      this.listeners.remove(listener);
   }

   public boolean nextRun()
   {
      Validate.isTrue(!this.isRunning, "reentrant run loop detected!");

      this.isRunning = true;

      boolean continueRunLoop = false;
      try {
         List<PlanStep> steps = this.plan.getPossibleSteps();
         Validate.notEmpty(steps, "plan did not return any step");

         List<KnowledgeSource> candidateSources = this.selectApplicableSources();

         if (!candidateSources.isEmpty()) {
            this.notifyListenersPossibleStepsSelected(steps);
            this.notifyListenersSourcesFound(candidateSources);

            KnowledgeSource selectedSource = this.selectNextSource(steps, candidateSources);
            if (selectedSource != null) {
               Validate.isTrue(candidateSources.contains(selectedSource),
                        "selection strategy did not return a candidate KnowledgeSource");
               continueRunLoop = true;
               this.notifyListenersSourceSelected(selectedSource);
               selectedSource.contribute();
            }
         }

         // terminate this control?
         for (PlanStep step : steps) {
            if (step.terminates()) {
               this.driver.detachControl(this);
               continueRunLoop = false;
            }
         }
      } finally {
         this.isRunning = false;
      }

      return continueRunLoop;
   }

   public String toString()
   {
      return "a Control " + this.name();
   }

   private List<KnowledgeSource> selectApplicableSources()
   {
      List<KnowledgeSource> applicableSources = new ArrayList<KnowledgeSource>();

      for (KnowledgeSource source : this.knowledgeSources) {
         if (source.canContribute()) applicableSources.add(source);
      }

      return Collections.unmodifiableList(applicableSources);
   }

   private KnowledgeSource selectNextSource(List<PlanStep> steps,
            List<KnowledgeSource> applicableSources)
   {
      KnowledgeSource selected = null;

      for (PlanStep step : steps) {
         selected = this.selectionStrategy.selectBestSource(step, applicableSources);
         if (selected != null) break;
      }

      return selected;
   }

   private void notifyListenersSourcesFound(List<KnowledgeSource> sources)
   {
      sources = Collections.unmodifiableList(sources);
      for (BlackboardControlListener listener : listeners)
         listener.foundApplicableSources(this, sources);
   }

   private void notifyListenersPossibleStepsSelected(List<PlanStep> steps)
   {
      steps = Collections.unmodifiableList(steps);
      for (BlackboardControlListener listener : this.listeners)
         listener.didSelectPossibleSteps(this, steps);
   }

   private void notifyListenersSourceSelected(KnowledgeSource selectedSource)
   {
      for (BlackboardControlListener listener : listeners)
         listener.didSelectNextSource(this, selectedSource);
   }
}
