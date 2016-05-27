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

	public MethodInfo(ClassType.AccessSpecifier as, Type ret, ClassType parent) {
		super(ret);
		this.parent = parent;
    this.accSpec = as;
	}

  public ClassType parent() {
    return this.parent;
  }

  public ClassType.AccessSpecifier accessSpecifier() {
    return this.accSpec;
  }
}


