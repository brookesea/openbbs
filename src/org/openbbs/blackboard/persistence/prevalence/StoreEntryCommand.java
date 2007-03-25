package org.openbbs.blackboard.persistence.prevalence;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.Zone;

class StoreEntryCommand implements PrevalenceCommand
{
   private final Zone zone;
   private final Object entry;

   public StoreEntryCommand(Zone zone, Object entry) {
      Validate.notNull(zone, "cannot store entry for null zone");
      Validate.notNull(entry, "cannot store null entry");
      Validate.isTrue(entry instanceof Serializable, "entry is not serializable");
      this.zone = zone;
      this.entry = entry;
   }

   public String toString() {
      return new ToStringBuilder(this).append("zone", zone).append("entry", entry).toString();
   }

   public void playback(PlaybackDelegate playbackDelegate) {
      Validate.notNull(playbackDelegate);
      playbackDelegate.storeEntry(this.zone, this.entry);
   }

   private static final long serialVersionUID = 5264242562029115738L;
}
