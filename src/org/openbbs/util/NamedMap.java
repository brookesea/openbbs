package org.openbbs.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

/**
 * A NamedMap is just like a normal Map but it has a name assigned. This
 * makes it possible to store a NamedMap on a blackboard and to retrieve
 * it from the blackboard by using it's name.
 * <p />
 * Please note two NamedMaps with identical names are neither considered
 * to be equal nor have the same hashCode. This way, you can safely store
 * multiple NamedMaps with identical names on the same blackboard. It is,
 * however, up to you to find the NamedMap you're interested in :-)
 * <p />
 * Internally, a NamedMap used a Map-Implementation as backingStore. By
 * default, this will be a HashMap. By passing a Map to the constructor,
 * however, you can use any backingStore you like.
 *
 * @author sks
 */
public class NamedMap<K,V> implements Map<K, V>, Serializable
{
   private String name;
   private final Map<K, V> backingStore;

   public NamedMap() {
      this((String) null);
   }

   public NamedMap(Map<K, V> backingStore) {
      this(null, backingStore);
   }

   public NamedMap(String name) {
      this(name, new HashMap<K, V>());
   }

   public NamedMap(String name, Map<K, V> backingStore) {
      Validate.notNull(backingStore, "cannot use null as a backingStore");
      this.name = name;
      this.backingStore = backingStore;
   }
   
   public String getName() {
      return this.name;
   }
   
   public void setName(String name) {
      this.name = name;
   }

   public void clear() {
      backingStore.clear();
   }

   public boolean containsKey(Object key) {
      return backingStore.containsKey(key);
   }

   public boolean containsValue(Object value) {
      return backingStore.containsValue(value);
   }

   public Set<Entry<K, V>> entrySet() {
      return backingStore.entrySet();
   }

   public boolean equals(Object o) {
      return backingStore.equals(o);
   }

   public V get(Object key) {
      return backingStore.get(key);
   }

   public int hashCode() {
      return backingStore.hashCode();
   }

   public boolean isEmpty() {
      return backingStore.isEmpty();
   }

   public Set<K> keySet() {
      return backingStore.keySet();
   }

   public V put(K key, V value) {
      return backingStore.put(key, value);
   }

   public void putAll(Map<? extends K, ? extends V> t) {
      backingStore.putAll(t);
   }

   public V remove(Object key) {
      return backingStore.remove(key);
   }

   public int size() {
      return backingStore.size();
   }

   public Collection<V> values() {
      return backingStore.values();
   }

   private static final long serialVersionUID = -3768515579962373189L;
}
