package org.openbbs.blackboard.persistence.prevalence;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;

class RemoveEntryCommand implements PrevalenceCommand
{
   private Object entry;

   public RemoveEntryCommand(Object entry) {
      Validate.notNull(entry, "cannot store null entry");
      Validate.isTrue(entry instanceof Serializable, "entry is not serializable");
      this.entry = entry;
   }

   public String toString() {
      return new ToStringBuilder(this).append("entry", entry).toString();
   }

   public void playback(PlaybackDelegate playbackDelegate) {
      playbackDelegate.removeEntry(this.entry);
   }

   private static final long serialVersionUID = -362197486210555599L;
}
