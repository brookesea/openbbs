package org.openbbs.blackboard;

/**
 * A {@link ZoneSelector} that selects the default zone on a 
 * {@link Blackboard}.
 *
 * @author sks
 */
public class DefaultZoneSelector implements ZoneSelector
{
   public boolean selects(Zone zone) {
      return Zone.DEFAULT.equals(zone);
   }
}
