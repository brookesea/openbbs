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

import java.io.File;

import junit.framework.TestCase;

import org.openbbs.blackboard.AllZonesSelector;
import org.openbbs.blackboard.NamedZone;
import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.filter.AnyObjectFilter;
import org.openbbs.blackboard.persistence.prevalence.MemoryLockedException;
import org.openbbs.blackboard.persistence.prevalence.PrevalenceMemory;
import org.openbbs.blackboard.persistence.prevalence.SimpleLogFile;
import org.openbbs.blackboard.persistence.snapshot.SnapshottableMemory;
import org.openbbs.blackboard.persistence.snapshot.Snapshotter;

public class AccessLockedPrevalenceMemoryTest extends TestCase
{
   private PrevalenceMemory memory;
   private Zone zone = new NamedZone("ZONE");
   private Object entry = "Hello, World!";

   protected void setUp() throws Exception {
      this.memory = new PrevalenceMemory();
      this.memory.setLogFile(new SimpleLogFile(File.createTempFile("prevalence", ".log")));
      this.memory.createZone(this.zone);
      this.memory.storeEntry(this.zone, this.entry);
   }

   public void testCannotGetEntriesFromLockedMemory() {
      this.memory.lock();
      try {
         this.memory.getEntries(new AllZonesSelector(), new AnyObjectFilter());
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }
   
   public void testCannotGetZoneForEntryFromLockedMemory() {
      this.memory.lock();
      try {
         this.memory.getZone(this.entry);
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }
   
   public void testCannotCheckExistenceOfEntryInLockedMemory() {
      this.memory.lock();
      try {
         this.memory.entryExists(this.entry);
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }
   
   public void testCannotCheckExistenceOfZoneInLockedMemory() {
      this.memory.lock();
      try {
         this.memory.zoneExists(this.zone);
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }
   
   public void testCannotCreateZoneInLockedMemory() {
      this.memory.lock();
      try {
         this.memory.createZone(new NamedZone("NEW_ZONE"));
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }
   
   public void testCannotDropZoneFromLockedMemory() {
      this.memory.lock();
      try {
         this.memory.dropZone(this.zone);
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }

   public void testCannotStoreEntryInLockedMemory() {
      this.memory.lock();
      try {
         this.memory.storeEntry(this.zone, "The quick brown fox jumps over the lazy dog");
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }

   public void testCannotRemoveEntryFromLockedMemory() {
      this.memory.lock();
      try {
         this.memory.removeEntry(this.entry);
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
   }
   
   public void testCannotAccessMemoryDuringRestore() {
      this.memory.setSnapshotter(new ConcurrentWriteSnapshotter());
      try {
         this.memory.restore();
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
      assertFalse(this.memory.isLocked());
   }
   
   public void testCannotAccessMemoryWhileTakingASnapshot() {
      this.memory.setSnapshotter(new ConcurrentWriteSnapshotter());
      try {
         this.memory.snapshot();
         fail("MemoryLockException expected");
      }
      catch (MemoryLockedException exception) {
         // ok
      }
      assertFalse(this.memory.isLocked());
   }
   
   private class ConcurrentWriteSnapshotter extends Snapshotter
   {
      public void takeSnapshot(SnapshottableMemory snapshottableMemory) {
         memory.createZone(new NamedZone("NEW_ZONE"));
      }
      
      public void restoreFromSnapshot(SnapshottableMemory snapshottableMemory) {
         memory.createZone(new NamedZone("NEW_ZONE"));
      }
   }
}
