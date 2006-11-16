package org.openbbs.blackboard.persistence.prevalence;

import java.io.Serializable;

import org.openbbs.blackboard.persistence.PlaybackDelegate;

interface PrevalenceCommand extends Serializable
{
   public void playback(PlaybackDelegate playbackDelegate);
}
