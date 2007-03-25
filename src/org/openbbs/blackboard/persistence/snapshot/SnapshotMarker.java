package org.openbbs.blackboard.persistence.snapshot;

import java.io.Serializable;

import org.apache.commons.lang.Validate;

/**
 * Serves as a marker in snapshot files. In order to achieve
 * maximum compatibilty with different serialization mechanisms,
 * this is a simple class and not an enum.
 */
class SnapshotMarker implements Serializable
{
   public static final SnapshotMarker NEW_ZONE = new SnapshotMarker("NEW_ZONE");

   private final String name;

   private SnapshotMarker(String name) {
      Validate.notNull(name);
      this.name = name;
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof SnapshotMarker)) {
         return false;
      }

      return this.name.equals(((SnapshotMarker)obj).name);
   }

   public int hashCode() {
      return this.name.hashCode();
   }

   private static final long serialVersionUID = 3249732671648756577L;
}
