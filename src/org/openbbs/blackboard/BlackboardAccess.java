package org.openbbs.blackboard;

import java.util.Set;


/**
 * Provides access to a blackboard for KnowledgeSources. Depending
 * on the implementation, access may be restricted to one or more
 * particular zones.
 * 
 * @author stefan
 */
public interface BlackboardAccess
{
   /**
    * Returns the first entry that is selected by the filter. Access
    * restrictions imposed by the BlackboardAccess apply.
    */
   public Object read(EntryFilter filter);
   
   /**
    * Returns all entries that are selected by the filter. Access
    * restrictions imposed by the BlackboardAccess apply.
    */
   public Set<Object> readAll(EntryFilter filter);
   
   /**
    * Returns true if the specified filter selects at least one
    * entry. Access restrictions imposed by the BlackboardAccess
    * apply.
    */
   public boolean exists(EntryFilter filter);
   
   /**
    * Remove and return the first entry that is selected by the filter.
    * Access restrictions imposed by the BlackboardAccess apply.
    */
   public Object take(EntryFilter filter);
   
   /**
    * Write a non-null entry to the blackboard. The zone to which the object
    * is written is determined by the BlackboardAccess.
    */
   public void write(Object entry);
}
