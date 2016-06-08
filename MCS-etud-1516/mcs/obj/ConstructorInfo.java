/**
 * Class representing a constructor of the class
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import mcs.symtab.FunctionInfo;

public class ConstructorInfo extends MethodInfo {
  public ConstructorInfo(Klass.AccessSpecifier as, Klass parent) {
    super(parent.name(), as, parent, parent);
    super.assignVtable(null);
  }

  @Override
  public void assignVtable(VirtualTable t) {
  }

  @Override
  public boolean equals(FunctionInfo other) {
    if (other instanceof ConstructorInfo)
      return this.equals((ConstructorInfo)other);
    return false;
  }

  public boolean equals(ConstructorInfo other) {
    return this.parent().isEqualTo(other.parent()) && this.similar(other);
  }
}


