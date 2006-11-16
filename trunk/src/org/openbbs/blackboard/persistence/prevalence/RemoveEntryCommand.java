package org.openbbs.blackboard.persistence.prevalence;

import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.openbbs.blackboard.Zone;

class RemoveEntryCommand implements PrevalenceCommand
{
   private Zone zone;
   private Object entry;
   
   public RemoveEntryCommand(Zone zone, Object entry) {
      Validate.notNull(zone, "cannot store entry for null zone");
      Validate.notNull(entry, "cannot store null entry");
      Validate.isTrue(entry instanceof Serializable, "entry is not serializable");
   }
   
   public String toString() {
      return new ToStringBuilder(this).append("zone", zone).append("entry", entry).toString();
   }
}
