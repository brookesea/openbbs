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
package org.openbbs.blackboard.control;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.Blackboard;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.ks.KSExecutionContext;
import org.openbbs.blackboard.ks.KnowledgeSource;
import org.openbbs.blackboard.plan.ControlPlan;
import org.openbbs.blackboard.plan.KSSelectionStrategy;
import org.openbbs.blackboard.plan.PlanStep;

/**
 * @author stefan
 */
public class BlackboardControl
{
   private Blackboard blackboard;
   private Zone zone;
   private ControlPlan plan = null;
   private KSSelectionStrategy ksSelectionStrategy = null;
   private List<KnowledgeSource> transformations = null;
   private ControlDriver driver = null;
   private boolean isRunning = false;
   private boolean closeZoneOnTerminate = false;
   private List<BlackboardControlListener> listeners = new ArrayList<BlackboardControlListener>();

   public BlackboardControl() {
      return;
   }

   public void setBlackboard(Blackboard blackboard) {
      Validate.notNull(blackboard);
      this.blackboard = blackboard;
   }

   public void setZone(Zone zone) {
      Validate.notNull(zone);
      this.zone = zone;
   }

   public void setCloseZoneOnTerminate(boolean removeZoneOnTerminate) {
      this.closeZoneOnTerminate = removeZoneOnTerminate;
   }

   public void setPlan(ControlPlan plan) {
      Validate.notNull(plan);
      this.plan = plan;
   }

   public void setKSSelectStrategy(KSSelectionStrategy ksSelectionStrategy) {
      Validate.notNull(ksSelectionStrategy);
      this.ksSelectionStrategy = ksSelectionStrategy;
   }

   public void setTransformations(List<KnowledgeSource> transformations) {
      this.transformations = null;
   }

   public void setDriver(ControlDriver driver) {
      if (this.driver != null) this.driver.detachControl(this);

      this.driver = null;
      driver.attachControl(this);
      this.driver = driver;
   }

   public void registerListener(BlackboardControlListener listener) {
      Validate.notNull(listener);
      if (!this.listeners.contains(listener)) this.listeners.add(listener);
   }

   public void removeListener(BlackboardControlListener listener) {
      Validate.notNull(listener);
      this.listeners.remove(listener);
   }

   public boolean nextRun() {
      Validate.isTrue(!this.isRunning, "reentrant run loop detected!");

      this.isRunning = true;

      boolean continueRunLoop = false;
      try {
         KSExecutionContext executionContext = this.createKSExecutionContext();

         this.runApplicableTransformations(executionContext);

         List<PlanStep> steps = this.plan.getPossibleSteps(executionContext);
         Validate.notEmpty(steps, "plan did not return any step");
         this.notifyListenersPossibleStepsSelected(steps);

         KnowledgeSource selectedSource = this.selectNextSource(steps, executionContext);
         if (selectedSource != null) {
            continueRunLoop = true;
            this.notifyListenersSourceSelected(selectedSource);
            selectedSource.execute(executionContext);
         }

         // terminate this control?
         for (PlanStep step : steps) {
            if (step.terminates()) {
               continueRunLoop = false;
               this.terminate();
               break;
            }
         }
      }
      finally {
         this.isRunning = false;
      }

      return continueRunLoop;
   }

   private KnowledgeSource selectNextSource(List<PlanStep> steps, KSExecutionContext executionContext) {
      for (PlanStep step : steps) {
         Collection<KnowledgeSource> sourcesForStep = this.ksSelectionStrategy.getKnowledgeSources(step);

         if (sourcesForStep == null) continue;

         for (KnowledgeSource candidateSource : sourcesForStep) {
            if (candidateSource.isEnabled(executionContext)) {
               return candidateSource;
            }
         }
      }

      return null;
   }

   private void runApplicableTransformations(KSExecutionContext executionContext) {
      if (this.transformations == null || this.transformations.isEmpty()) {
         return;
      }

      for (KnowledgeSource transformation : this.transformations) {
         if (transformation.isEnabled(executionContext)) {
            transformation.execute(executionContext);
         }
      }
   }

   private void terminate() {
      this.driver.detachControl(this);
      this.driver = null;

      if (this.closeZoneOnTerminate) {
         this.blackboard.closeZone(zone);
      }
   }

   private KSExecutionContext createKSExecutionContext() {
      return new KSExecutionContextImpl(this.blackboard, this.zone);
   }

   private void notifyListenersPossibleStepsSelected(List<PlanStep> steps) {
      steps = Collections.unmodifiableList(steps);
      for (BlackboardControlListener listener : this.listeners)
         listener.didSelectPossibleSteps(this, steps);
   }

   private void notifyListenersSourceSelected(KnowledgeSource selectedSource) {
      for (BlackboardControlListener listener : listeners)
         listener.didSelectNextSource(this, selectedSource);
   }
}
