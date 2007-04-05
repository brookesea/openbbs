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
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.openbbs.blackboard.Zone;
import org.openbbs.blackboard.persistence.prevalence.PlaybackDelegate;
import org.openbbs.blackboard.persistence.prevalence.PrevalenceCommand;
import org.openbbs.blackboard.persistence.prevalence.SimpleLogFile;

/**
 * Tests whether it is possible to write commands to a SimpleLogFile,
 * then close the file and then append more commands. Therewas a bug
 * which prevented SimpleLogFiles created in such a way from beeing
 * read (StreamCorruptedException).
 */
public class ReopenSimpleLogFileTest extends TestCase
{
   private File ouputFile;
   private List<MockCommand> commands;

   protected void setUp() throws Exception {
      this.ouputFile = File.createTempFile("prevalence", ".log");
      SimpleLogFile logFile = new SimpleLogFile(this.ouputFile);

      this.commands = new ArrayList<MockCommand>();
      for (int i = 0; i < 10; i++) {
         MockCommand command = new MockCommand();
         this.commands.add(command);
         logFile.writeCommand(command);
         // close log after every 2 commands (only 1 command in the first turn)
         if (i % 2 == 0) {
            logFile.closeLog();
         }
      }
   }

   public void testReplay() {
      SimpleLogFile logFile = new SimpleLogFile(this.ouputFile);
      logFile.playback(new DummyPlaybackDelegate());
      assertEquals(this.commands.size(), MockCommand.playbackCount);
   }

   @SuppressWarnings("serial")
   private static class MockCommand implements PrevalenceCommand
   {
      private static int count = 0;
      private static int playbackCount = 0;
      private final int number;
      private boolean playedBack = false;

      public MockCommand() {
         this.number = count++;
      }

      public boolean wasPlayedBack() {
         return this.playedBack;
      }

      public int getNumber() {
         return this.number;
      }

      public void playback(PlaybackDelegate playbackDelegate) {
         if (playbackCount != this.number) {
            throw new IllegalStateException("expected this command to be the " + number
                  + " during playback but was number " + playbackCount);
         }
         playbackCount++;
         this.playedBack = true;
      }
   }

   private static class DummyPlaybackDelegate implements PlaybackDelegate
   {
      public void createZone(Zone zone) {
         throw new UnsupportedOperationException("createZone was not expected");
      }

      public void dropZone(Zone zone) {
         throw new UnsupportedOperationException("dropZone was not expected");
      }

      public void removeEntry(Object entry) {
         throw new UnsupportedOperationException("removeEntry was not expected");
      }

      public void storeEntry(Zone zone, Object entry) {
         throw new UnsupportedOperationException("storeEntry was not expected");
      }
   }
}
