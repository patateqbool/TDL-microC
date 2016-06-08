/**
 * MethodInfo -- class representing a method (ie : a function of a class)
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import mcs.symtab.*;

public class MethodInfo extends FunctionInfo {
	private Klass parent;
  private Klass.AccessSpecifier accSpec;
  private VirtualTable vtable;

	public MethodInfo(String name, Klass.AccessSpecifier as, Type ret, Klass parent) {
		super(name, ret, null);
		this.parent = parent;
    this.accSpec = as;
	}

	public MethodInfo(Klass.AccessSpecifier as, Klass parent, FunctionInfo other) {
		super(other.name(), other.returnType(), other.parameters(), null);
		this.parent = parent;
		this.accSpec = as;
	}

  public Klass parent() {
    return this.parent;
  }

  public Klass.AccessSpecifier accessSpecifier() {
    return this.accSpec;
  }

	/*@Override
	public String label() {
	}*/

  // Vtable related
  public void assignVtable(VirtualTable t) {
    this.vtable = t;
  }

  public VirtualTable vtable() {
    return this.vtable;
  }

  @Override
  public boolean equals(FunctionInfo other) {
    if (other instanceof MethodInfo) {
      return this.equals((MethodInfo)other);
    }
    return false;
  }

  public boolean equals(MethodInfo other) {
    return super.equals(other) && other.parent().isEqualTo(this.parent);
  }
}


