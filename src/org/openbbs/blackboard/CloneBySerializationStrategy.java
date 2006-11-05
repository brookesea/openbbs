package org.openbbs.blackboard;

import java.io.Serializable;

import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.Validate;

/**
 * An object is (deep-)cloned by creating serialization and de-serialization.
 * Requires that objects which are cloned with this strategy implement the
 * Serializable interface.
 * 
 * @author stefan
 */
public class CloneBySerializationStrategy implements CloneStrategy
{
   public Object clone(Object obj)
   {
      if (obj == null) return null;

      Validate.isTrue(obj instanceof Serializable, "object " + obj.toString()
               + " is not serializable");
      return SerializationUtils.clone((Serializable)obj);
   }
}
