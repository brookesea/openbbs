package org.openbbs.blackboard.persistence.prevalence;

import java.io.Serializable;


interface PrevalenceCommand extends Serializable
{
   public void playback(PlaybackDelegate playbackDelegate);
}
