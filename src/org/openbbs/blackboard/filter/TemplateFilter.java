package org.openbbs.blackboard.filter;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;

import org.apache.commons.lang.Validate;
import org.openbbs.blackboard.EntryFilter;

/**
 *
 * @author sks
 */
public class TemplateFilter implements EntryFilter
{
   public static final String PROPERTY_ATTRIBUTE_DEFAULT_VALUE = "defaultValue";
   private final Object template;
   private Collection<PropertyDescriptor> filterProperties;

   public TemplateFilter(Object template) {
      Validate.notNull(template, "cannot filter by null template");
      this.template = template;

      this.filterProperties = new LinkedList<PropertyDescriptor>();

      try
      {
         BeanInfo beanInfo = Introspector.getBeanInfo(template.getClass(), Object.class);
         for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
            Method readMethod = propertyDesc.getReadMethod();
            if (readMethod != null) {
               Object defaultPropertyValue = propertyDesc.getValue(PROPERTY_ATTRIBUTE_DEFAULT_VALUE);
               Object templatePropertyValue = readMethod.invoke(template, new Object[0]);
               if (templatePropertyValue != null && !templatePropertyValue.equals(defaultPropertyValue)) {
                  this.filterProperties.add(propertyDesc);
               }
            }
         }
      }
      catch (Exception exc)
      {
         throw new RuntimeException("error while building BeanInfo for template: " + exc.getMessage(), exc);
      }
   }

   public boolean selects(Object entry) {
      if (!entry.getClass().isAssignableFrom(template.getClass())) {
         return false;
      }

      for (PropertyDescriptor propertyDesc : this.filterProperties) {
         Method readMethod = propertyDesc.getReadMethod();
         try {
            Object templatePropertyValue = readMethod.invoke(this.template, new Object[0]);
            Object entryPropertyValue = readMethod.invoke(entry, new Object[0]);
            if (!templatePropertyValue.equals(entryPropertyValue)) {
               return false;
            }
         }
         catch (Exception exc) {
            throw new RuntimeException("error getting value for property " + propertyDesc.getName());
         }
      }

      return true;
   }
}
