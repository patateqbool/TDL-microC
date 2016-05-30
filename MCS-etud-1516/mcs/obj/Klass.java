/**
 * Klass -- class representing a class (ha ha ha).
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
package mcs.obj;

import java.util.Map;
import mcs.util.OrderedMap;
import mcs.symtab.*;

class Klass extends Type {
  enum AccessSpecifier {
    APublic, APrivate, AProtected
  };

  private static int nextID = 0;

  private String name;
  private int id;
  private Klass parent;
  private Map<String,MethodInfo> methodTable;
  private Map<String,AttributeInfo> attributeTable;
  private Map<String,Integer> daughters;
 
  // public Klass(String name, List<Klass> parents) {
  /**
   * Constructor
   * Build a class from its name and its parent; it also set the
   * class's unique id.
   * @param name name of the class
   * @param parent parent class (can be null)
   */
  public Klass(String name, Klass parent) {
    super(4);
    this.name = name;
    this.parent = parent;

    // Class id
    this.id = Klass.nextID;
    Klass.nextID++;

    // Internal stuff
    this.methodTable = new OrderedMap<String,MethodInfo>();
    this.attributeTable = new OrderedMap<String,AttributeInfo>();

    if (parent != null) {
      // Add this daughter to the parent
      this.parent.appendDaughter(name, this.id);

      // For each attribute, we add it to the class depending on its reach
      for (String attr : this.parent.attributeTable.keySet()) {
        AttributeInfo ai = this.parent.attributeTable.get(attr);

        if (ai.accessSpecifier() != AccessSpecifier.APrivate) // Private attributes does not appear
          this.addAttribute(attr, ai);
      }

      // Virtualize each methods
      for (String meth : this.parent.methodTable.keySet()) {
        MethodInfo mi = this.parent.methodTable.get(meth);

        if (mi.accessSpecifier() != AccessSpecifier.APrivate) {
          // If we have A -> B, we basically want C(:A) -> B
          mi.vtable().set(this.id, mi.vtable().get(this.parent.id));
          this.addMethod(name, mi);
        }
      }
    }
  }

  /**
   * Append an id to the daughters.
   * This method is called only by daughters classes
   * @param cclass name of the class
   * @param id id of the class
   */
  private void appendDaughter(String name, int id) {
    this.daughters.put(name, id);

    // Propagation
    if (this.parent != null)
      this.parent.appendDaughter(name, id);
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
		if (this.methodTable.get(name).equals(mi)) {
			// Inserting a method that already exists is fine, it
      // is called overriding !
      // This causes a change in the vtable
      this.methodTable.get(name).vtable().set(this.id, this.id);
		} else {
      // This method does not exists; we must create a vtable for it
      VirtualTable vt = new VirtualTable();
      vt.set(this.id, this.id);
      mi.assignVtable(vt);
		  this.methodTable.put(name, mi);
    }
  }

	/**
	 * Append a method to the class's function table
	 * @param name name of the symbol
	 * @param fi the function info
	 */
	public void addMethod(String name, AccessSpecifier as, FunctionInfo fi) {
		this.addMethod(
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
		this.attributeTable.put(name, ai);
	}

	/**
	 * Append an attribute to the class's variable table
	 * @param name name of the symbol
	 * @param as access specifier
	 * @param vi variable info
	 */
	public void addAttribute(String name, AccessSpecifier as, VariableInfo vi) {
		this.addAttribute(
				name,
				new AttributeInfo(as, this, vi)
		);
	}


  /**
   * Accessors
   */
  public Klass parent() {
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


