package org.openbbs.blackboard.persistence.prevalence;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.Zone;

class CreateZoneCommand implements PrevalenceCommand
{
   private final Zone zone;

   public CreateZoneCommand(Zone zone) {
      Validate.notNull(zone, "zone must not be null");
      this.zone = zone;
   }

   public String toString() {
      return new ToStringBuilder(this).append("zone", zone).toString();
   }

   public void playback(PlaybackDelegate playbackDelegate) {
   }
}
