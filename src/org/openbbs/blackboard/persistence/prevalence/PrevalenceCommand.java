package org.openbbs.blackboard.persistence.prevalence;

import java.io.Serializable;

/**
 * A PrevalenceCommands represents a change to a PrevalenceMemory. By
 * playing back a set of stored commands, changes can be restored.
 */
public interface PrevalenceCommand extends Serializable
{
   /**
    * Redo the change represented by this command. The command
    * has to send the appropriate message to the specified
    * PlaybackDelegate.
    */
   public void playback(PlaybackDelegate playbackDelegate);
}
