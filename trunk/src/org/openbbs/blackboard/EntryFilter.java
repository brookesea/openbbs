/*
 * EntrySelector.java
 *
 * Copyright by Stefan Kleine Stegemann, 2005
 */
package org.openbbs.blackboard;

/**
 * An EntryFilter selects one or more entries from a Blackboard
 * by applying domain-specific criteria.
 *
 * @author sks
 */
public interface EntryFilter
{
   public boolean selects(Object entry);
}
