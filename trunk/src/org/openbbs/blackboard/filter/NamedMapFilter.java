package org.openbbs.blackboard.filter;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;
import org.openbbs.util.NamedMap;

/**
 * @author sks
 */
public class NamedMapFilter implements EntryFilter
{
   private final String mapName;

   public NamedMapFilter(String mapName) {
      Validate.notNull(mapName, "name of the map must not be null");
      this.mapName = mapName;
   }

   public boolean selects(Object entry) {
      if (!(entry instanceof NamedMap)) {
         return false;
      }
      return this.mapName.equals(((NamedMap<?, ?>) entry).getName());
   }
}
