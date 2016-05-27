/**
 * OrderedMap -- an ordered map
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.util;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class OrderedMap<K,V> implements Map<K,V> {
  public class Entry<K,V> implements Map.Entry<K,V> {
    private K key;
    private V value;

    public Entry(K k, V v) {
      this.key = k;
      this.value = v;
    }

    public boolean equals(Object o) {
      return false;
    }

    public K getKey() {
      return this.key;
    }

    public V getValue() {
      return this.value;
    }

    public int hashCode() {
      return 0; // Not implemented
    }

    public V setValue(V v) {
      V r = this.value;
      this.value = v;
      return r;
    }
  }

  private List<Map.Entry<K,V> > content;

  public OrderedMap() {
    this.content = new ArrayList<Map.Entry<K,V> >();
  }

  public void clear() {
    this.content = new ArrayList<Map.Entry<K,V> >();
  }

  public boolean containsKey(Object key) {
    for (Map.Entry<K,V> e : this.content) {
      if (key.equals(e.getKey()))
        return true;
    }
    return false;
  }

  public boolean containsValue(Object value) {
    for (Map.Entry<K,V> e : this.content) {
      if (value.equals(e.getValue()))
        return true;
    }
    return false;
  }

  public Set<Map.Entry<K,V> > entrySet() {
    Set<Map.Entry<K,V> > result = new TreeSet<Map.Entry<K,V> >();
    for (Map.Entry<K,V> e : this.content) {
      result.add(e);
    }
    return result;
  }

  public boolean equals(Object o) {
    // Not implemented
    return false;
  }

  public V get(Object key) {
    for (Map.Entry<K,V> e : this.content) {
      if (e.getKey().equals(key))
        return e.getValue();
    }
    return null;
  }

  public int hashCode() {
    // Not implemented
    return 0;
  }

  public boolean isEmpty() {
    return this.content.isEmpty();
  }

  public Set<K> keySet() {
    Set<K> result = new TreeSet<K>();
    for (Map.Entry<K,V> e : this.content) {
      result.add(e.getKey());
    }
    return result;
  }

  public V put(K key, V value) {
    V result = null;

    for (int i = 0; i < this.content.size(); i++) {
      Map.Entry<K,V> e = this.content.get(i);
      if (key.equals(e.getKey())) {
        result = e.getValue();
        this.content.set(i, new Entry<K,V>(key,value));
        return result;
      }
    }

    // No already defined key
    this.content.add(new Entry<K,V>(key, value));
    return result;
  }

  public void putAll(Map<? extends K,? extends V> m) {
    // nop
  }
  
  public V remove(Object key) {
    V result = null;
    for (int i = 0; i < this.content.size(); i++) {
      Map.Entry<K,V> e = this.content.get(i);
      if (key.equals(e.getKey())) {
        result = e.getValue();
        this.content.remove(i);
        return result;
      }
    }
    return result;
  }

  public int size() {
    return this.content.size();
  }

  public Collection<V> values() {
    Collection<V> values = new ArrayList<V>();
    for (Map.Entry<K,V> e : this.content) {
      values.add(e.getValue());
    }
    return values;
  }
}


