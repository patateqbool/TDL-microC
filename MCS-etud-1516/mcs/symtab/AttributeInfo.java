/**
 * AttributeInfo -- class representing an attribute in a class; eg : a variable with an access modifier
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class AttributeInfo extends VariableInfo {
  // Attributes
  private ClassType parent;
  private ClassType.AccessSpecifier accSpec;

  public AttributeInfo(ClassType.AccessSpecifier as, Type t, int d, ClassType parent) {
    super(t, d);
    this.accSpec = as;
    this.parent = parent;
  }

	public AttributeInfo(ClassType.AccessSpecifier as, ClassType parent, VariableInfo vi) {
		this(as, vi.type(), vi.displacement(), parent);
	}

  public ClassType.AccessSpecifier accessSpecifier() {
    return this.accSpec;
  }

  public ClassType parent() {
    return this.parent();
  }
}


