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

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import mcs.util.OrderedMap;
import mcs.symtab.*;

public class Klass extends CompositeType {
  enum AccessSpecifier {
    AHidden(0),
    APublic(1),
    APrivate(2),
    AProtected(3);

    private final int value;
    private AccessSpecifier(int val) {
      this.value = val;
    }
    public int value() {
      return this.value;
    }
  };

  private static int nextID = 0;

	private int currentDisp = 4;
  private String name;
  private int id;
  private Klass parent;
  private List<MethodInfo> methodTable;
  private List<ConstructorInfo> cstrTable;
  private Map<String,AttributeInfo> attributeTable;
  private Map<String,Integer> daughters;
  private NamespaceInfo namespace;
 
  // public Klass(String name, List<Klass> parents) {
  /**
   * Constructor
   * Build a class from its name and its parent; it also set the
   * class's unique id.
   * @param name name of the class
   * @param parent parent class (can be null)
   */
  public Klass(String name, Klass parent, NamespaceInfo ns) {
    super(4);

    this.name = name;
    this.parent = parent;
    this.namespace = ns;

    // Class id
    this.id = Klass.nextID;
    Klass.nextID++;

    // Internal stuff
    this.methodTable = new ArrayList<MethodInfo>();
    this.attributeTable = new OrderedMap<String,AttributeInfo>();
    this.cstrTable = new ArrayList<ConstructorInfo>();

    if (parent != null) {
      // Add this daughter to the parent
      this.parent.appendDaughter(name, this.id);

      // For each attribute, we add it to the class depending on its reach
      for (String attr : this.parent.attributeTable.keySet()) {
        AttributeInfo ai = this.parent.attributeTable.get(attr);

        if (ai.accessSpecifier() != AccessSpecifier.APrivate) // Private attributes does not appear
          this.addAttribute(attr, ai);
				else
					this.addAttribute(attr, new AttributeInfo(AccessSpecifier.AHidden, this, ai));
      }

      // Virtualize each methods
      for (MethodInfo mi : this.parent.methodTable) {
        if (mi.accessSpecifier() != AccessSpecifier.APrivate) {
          // If we have A -> B, we basically want C(:A) -> B
          mi.vtable().set(this.id, mi.vtable().get(this.parent.id));
          this.addMethod(name, mi);
        }
      }
    }
  }

  /**
   * Get the namespace
   */
  public NamespaceInfo namespace() {
    return this.namespace();
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
	public boolean methodExists(MethodInfo meth) {
		for (MethodInfo mi : this.methodTable) {
			if (mi.equals(meth))
				return true;
		}
		return false;
	}

	public boolean methodExists(String name, MethodInfo meth) {
		for (MethodInfo mi : this.methodTable) {
			if (mi.similar(name, meth.parameters()))
				return true;
		}
		return false;
	}

  public boolean constructorExists(ConstructorInfo cstr) {
    for (ConstructorInfo ci : this.cstrTable)
      if (cstr.equals(ci))
        return true;
    return false;
  }

  public boolean attributeExists(String name) {
    return (this.attributeTable.get(name) != null);
  }

  /**
   * Append a method to the class's function table
	 * @param name name of the symbol
   * @param mi the method info
   */
  public boolean addMethod(String name, MethodInfo mi) {
		if (methodExists(mi)) {
			// Inserting a method that already exists is fine, it
      // is called overriding !
      // This causes a change in the vtable
      MethodInfo old = lookupMethod(name, mi.parameters());
      if (old.vtable().get(this.id) == this.name) // this method is already defined !
        return false;

      old.vtable().set(this.id, this.name);
		} else {
      // This method does not exists; we must create a vtable for it
      VirtualTable vt = new VirtualTable();
      vt.set(this.id, this.name);
      mi.assignVtable(vt);
			mi.setName(name);
		  this.methodTable.add(mi);
    }

    return true;
  }

	/**
	 * Append a method to the class's function table
	 * @param name name of the symbol
	 * @param fi the function info
	 */
	public boolean addMethod(String name, AccessSpecifier as, FunctionInfo fi) {
		return this.addMethod(
				name,
				new MethodInfo(as, this, fi)
		);
	}

	/**
	 * Append an attribute to the class's variable table
	 * @param name name of the symbol
	 * @param ai the attribute info
	 */
	public boolean addAttribute(String name, AttributeInfo ai) {
		if (this.attributeTable.get(name) == null) {
			this.attributeTable.put(name, ai);
			int ts = ai.type().size();
			this.currentDisp = ai.displacement() + (ts + (4 - (ts % 4)));
      return true;
		}
    return false;
	}

	/**
	 * Append an attribute to the class's variable table
	 * @param name name of the symbol
	 * @param as access specifier
	 * @param vi variable info
	 */
	public boolean addAttribute(String name, AccessSpecifier as, Type t) {
		return addAttribute(name, new AttributeInfo(as, t, this.currentDisp, this));
	}

  /**
   * Append a constructor to the class
   * @param ci constructor info to append
   */
  public boolean addConstructor(ConstructorInfo ci) {
    if (!constructorExists(ci)) {
      this.cstrTable.add(ci);
      return true;
    }
    return false;
  }

	public MethodInfo lookupMethod(String name, List<Type> params) {
		for (MethodInfo mi : this.methodTable) {
			if (mi.similar(name, params))
				return mi;
		}
		return null;
	}

	public AttributeInfo lookupAttribute(String name) {
		return this.attributeTable.get(name);
	}

  public ConstructorInfo lookupConstructor(List<Type> params) {
    for (ConstructorInfo ci : this.cstrTable) {
      if (ci.similar(ci.name(), params))
        return ci;
    }
    return null;
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

  public List<Type> attributeTypes() {
    List<Type> res = new ArrayList<Type>();
    for (AttributeInfo ai : this.attributeTable.values()) {
      res.add(ai.type());
    }
    return res;
  }


	/**
	 * Sum all the sizes of the fields preceding the field "to"
	 * @param to field
	 * @return s = sum_{k=0}^{i(to)-1} size(i)
	 * Note: the sum size is padded to prevent alignment problems.
	 * Thus, if a field is of a size not a multiple of 4, the next field will be at a multiple of 4 anyway.
	 * TODO: make better this system
	 */
	public int sumSizes(String to) {
		int ts;
		int size = 0;
		for (String n : this.attributeTable.keySet()) {
			if (n.equals(to))
				break;
			
			ts = this.attributeTable.get(n).type().size();
			if (ts % 4 == 0)
				size += ts;
			else
				size += ts + (4 - (ts % 4));
		}
		return size;
	}

	@Override
	public int realSize() {
		int ts;
		int res = 4; // Class id
		for (AttributeInfo ai : this.attributeTable.values()) {
			ts = ai.type().size();
			res += (ts%4 == 0 ? ts : ts + (4 - (ts%4)));
		}
		return res;
	}

  /**
   * Inherited abstract methods
   */
  public String toString() {
    return "Class";
  }

  public boolean isCompatible(Type other) {
		if (other instanceof Klass) {
			Klass kother = (Klass)other;

			if (kother.isEqualTo(this))
				return true;
			else if (this.parent != null)
				return this.parent.isCompatible(kother);
		}

    return false;
  }

	@Override
	public boolean isEqualTo(Type other) {
		if (other instanceof Klass) {
			Klass kother = (Klass)other;
			return kother.id == this.id;
		}

		return false;
	}

  public Object getDefault() {
    return null;
  }
}


