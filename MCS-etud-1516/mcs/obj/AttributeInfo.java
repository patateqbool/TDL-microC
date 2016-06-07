/**
 * AttributeInfo -- class representing an attribute in a class; eg : a variable with an access modifier
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import mcs.symtab.*;

public class AttributeInfo extends VariableInfo {
  // Attributes
  private Klass parent;
  private Klass.AccessSpecifier accSpec;

  public AttributeInfo(Klass.AccessSpecifier as, Type t, int d, Klass parent) {
    super(t, d, null);
    this.accSpec = as;
    this.parent = parent;
  }

	public AttributeInfo(Klass.AccessSpecifier as, Klass parent, VariableInfo vi) {
		this(as, vi.type(), vi.displacement(), parent, null);
	}

  public Klass.AccessSpecifier accessSpecifier() {
    return this.accSpec;
  }

  public Klass parent() {
    return this.parent();
  }
}


