/**
 * MethodInfo -- class representing a method (ie : a function of a class)
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class MethodInfo extends FunctionInfo {
	private ClassType parent;
  private ClassType.AccessSpecifier accSpec;
  private VirtualTable vtable;

	public MethodInfo(String name, ClassType.AccessSpecifier as, Type ret, ClassType parent) {
		super(name, ret);
		this.parent = parent;
    this.accSpec = as;
	}

	public MethodInfo(ClassType.AccessSpecifier as, ClassType parent, FunctionInfo other) {
		super(other.name(), other.returnType(), other.parameters());
		this.parent = parent;
		this.accSpec = as;
	}

  public ClassType parent() {
    return this.parent;
  }

  public ClassType.AccessSpecifier accessSpecifier() {
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
}


