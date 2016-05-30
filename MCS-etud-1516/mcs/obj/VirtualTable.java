/**
 * VirtualTable -- class representing the virtual table of a method.
 *
 * The virtual table is the base of polymorphism and late linking. Its principle is
 * simple :
 *   - when you define a method in a top mother class, you create a vtable for this
 *   method
 *   - when you inherit the mother class, you put a entry in the vtable with the
 *   id of the daughter class and the method of the mother
 *   - when you override the method of the mother class, you replace the entry of
 *   the daughter class with the label of the new method
 * Therefore, when you call a method on an object, it will pass by the vtable (if
 * needed) to determine the actual function to call.
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import java.util.Map;
import java.util.HashMap;

public class VirtualTable {
  // The actual trick is that the method corresponding to a class has always the
  // same signature, except for the prefix class name.
  // So we just need to store the class's id and the method's class id
  private Map<Integer,Integer> content;

  public VirtualTable() {
    this.content = new HashMap<Integer,Integer>();
  }

  /*
  public void insert(int classid, int methodclassid) {
    // TODO: should we verrify that the classid already exists ?
    this.content.put(classid, methodclassid);
  }*/

  public void set(int classid, int methodclassid) {
    this.content.put(classid, methodclassid);
  }

  public int get(int classid) {
    Integer r = this.content.get(classid);
    if (r == null)
      return -1;
    else
      return r;
  }
}


