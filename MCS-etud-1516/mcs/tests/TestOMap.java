/**
 * Test of the ordered map
 */
package mcs.tests;

import java.util.Map;
import mcs.util.OrderedMap;

public class TestOMap {
  public static <K,V> void printMap(Map<K,V> map) {
    System.out.println("=========================");
    for (K e : map.keySet()) {
      System.out.println("   " + e.toString() + " => " + map.get(e).toString());
    }
    System.out.println(";;");
  }

  public static void main(String args[]) {
    Map<Integer,String> om = new OrderedMap<Integer,String>();
    om.put(1, "a");
    printMap(om);
    om.put(2, "b");
    om.put(3, "d");
    om.put(4, "d");
    printMap(om);
    System.out.println("Contains k1 : " + om.containsKey(1) + ", k9 : " + om.containsKey(9) + "; v'a' : " + om.containsValue("a") + ", v'x' : " + om.containsValue("x"));
    om.put(3, "c");
    printMap(om);
  }
}


