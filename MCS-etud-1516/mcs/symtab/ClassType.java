/**
 * ClassType -- class representing a class (ha ha ha)
 *
 * @author G.Dupont
 * @version 0.1
 */
package mcs.symtab;

class ClassType extends Type {
  public ClassType() {
    super(0);
  }

  public String toString() {
    return "Class";
  }

  public boolean isCompatible(Type other) {
    return false;
  }

  public Object getDefault() {
    return null;
  }
}


