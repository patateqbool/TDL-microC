/**
 * ClassType -- class representing a class (ha ha ha).
 *
 * Note on classID:
 *  Each class have an unique ID that identifies it. A mother class knows
 *  its daughters'; when instanciated, the resulting object also have its
 *  classID, no matter which is the type it is registered under.
 *  This allows, by classID comparison, for late linking : we actually
 *  call the method of a corresponding class ID, and not of the actuall
 *  class 
 * @author G.Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.HashMap;

class ClassType extends Type {
  enum AccessSpecifier {
    APublic, APrivate, AProtected
  };

  private static int nextID = 0;

  private String name;
  private int id;
  private ClassType parent;
  private Map<String,MethodInfo> methodTable;
  private Map<String,AttributeInfo> attributeTable;
 
  // public ClassType(String name, List<ClassType> parents) {
  /**
   * Constructor
   * Build a class from its name and its parent; it also set the
   * class's unique id.
   * @param name name of the class
   * @param parent parent class (can be null)
   */
  public ClassType(String name, ClassType parent) {
    super(4);
    this.name = name;
    this.parent = parent;

    // Class id
    this.id = ClassType.nextID;
    ClassType.nextID++;

    // Internal stuff
    this.methodTable = new HashMap<String,MethodInfo>();
    this.attributeTable = new HashMap<String,AttributeInfo>();

		if (parent != null) {
			////////// Inheritance thingy //////////
			// Attributes
			for (String attr : parent.attributeTable.symbols()) {
				AttributeInfo ai = (AttributeInfo)(parent.attributeTable.lookup(attr, true));

				if (ai.accessSpecifier() != AccessSpecifier.APrivate) {
					AccessSpecifier newSpec = AccessSpecifier.APublic;

					if (ai.accessSpecifier() == AccessSpecifier.AProtected)
						newSpec = AccessSpecifier.APrivate;

					this.addAttribute(attr, newSpec, ai);
				}
			}

			// Methods
			for (String attr : parent.methodTable.symbols()) {
				MethodInfo mi = (MethodInfo)(parent.methodTable.lookup(attr, true));

				if (mi.accessSpecifier() != AccessSpecifier.APrivate) {
					AccessSpecifier newSpec = AccessSpecifier.APublic;

					if (mi.accessSpecifier() == AccessSpecifier.AProtected)
						newSpec = AccessSpecifier.APrivate;

					this.addMethod(attr, newSpec, mi);
				}
			}
		}
  }

  /**
   * Proxy methods
   */
  /**
   * Append a method to the class's function table
	 * @param name name of the symbol
   * @param mi the method info
   */
  public void addMethod(String name, MethodInfo mi) {
		if (this.methodTable.exists(name, mi)) {
			// Inserting a method that already exists is fine, it
      // is called overriding !
      this.methodTable.remove(name);
		}
		
    this.methodTable.put(name, mi);
  }

	/**
	 * Append a method to the class's function table
	 * @param name name of the symbol
	 * @param fi the function info
	 */
	public void addMethod(String name, AccessSpecifier as, FunctionInfo fi) {
		this.methodTable.insert(
				name,
				new MethodInfo(as, this, fi)
		);
	}

	/**
	 * Append an attribute to the class's variable table
	 * @param name name of the symbol
	 * @param ai the attribute info
	 */
	public void addAttribute(String name, AttributeInfo ai) {
		this.attributeTable.insert(name, ai);
	}

	/**
	 * Append an attribute to the class's variable table
	 * @param name name of the symbol
	 * @param as access specifier
	 * @param vi variable info
	 */
	public void addAttribute(String name, AccessSpecifier as, VariableInfo vi) {
		this.attributeTable.insert(
				name,
				new AttributeInfo(as, this, vi)
		);
	}


  /**
   * Accessors
   */
  public ClassType parent() {
    return this.parent;
  }

  public int classId() {
    return this.id;
  }

	public String name() {
		return this.name();
	}


  /**
   * Inherited abstract methods
   */
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


