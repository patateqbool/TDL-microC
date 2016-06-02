/**
 * FunctionInfo -- class representing a function
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.List;
import java.util.ArrayList;
import mcs.gc.Register;

public class FunctionInfo extends SymbolInfo {
  // Attributes
  private Type retType; // Return type
  private List<Type> parameters; // Parameters
  private String name;
	private Register reg;

  /**
   * Create a function symbol from a return type and a list of parameters
   * @param ret return type of the function
   * @param params list of parameters of the function
   */
  public FunctionInfo(String name, Type ret, NamespaceInfo ns) {
    super(ns);
    this.namespace = ns;
    this.name = name;
    this.retType = ret;
    this.parameters = new ArrayList<Type>();
  }

	public FunctionInfo(String name, Type ret, List<Type> params, NamespaceInfo ns) {
                super(ns);
                this.namespace = ns;
    this.name = name;
		this.parameters = params;
		this.retType = ret;
	}

  /**
   * Get the return type of the function
   * @return the return type of the function
   */
  public Type returnType() {
    return this.retType;
  }

  /**
   * Get the parameter list of the function
   * @return the list of the parameters
   */
  public List<Type> parameters() {
    return this.parameters;
  }

  /**
   * Get the name of the function
   * @return the name
   */
  public String name() {
    return this.name;
  }

  public NamespaceInfo namespace() {
    return this.namespace;
  }

  /**
   * Set the name of the function
   * @param name the new name
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get the label of the function.
   * @return the label
   */
  public String label() {
    String label = "_" + this.name + "@" + this.retType;

    for (Type t : this.parameters)
      label += "$" + t;

    return label;
  }

  /**
   * Add a parameter to the list of parameters of the function
   * @param t the type to add
   */
  public void add(Type t) {
    this.parameters.add(t);
  }

  /**
   * Add a parameter to the FRONT of the paraemeters list
   * @param t the type to add
   */
  public void pushFront(Type t) {
    this.parameters.add(0, t);
  }

  /**
   * Calculate displacement of given parameter number
   * @param n number of the argument for which to get the displacement
   * @return the displacement
   */
  public int displacement(int n) {
    int disp = -12;

    for (int i = 0; i <= n; i++) {
      int ts = this.parameters.get(i).size();

      if (ts % 4 == 0) // already aligned
        disp -= ts;
      else
        disp -= ts + (4 - (ts % 4));
    }

    return disp;
  }

	/**
	 * Manipulate the register of the function
	 */
	public Register register() {
		return this.reg;
	}

	public void assignRegister(Register r) {
		this.reg = r;
	}

  /**
   * Equal function.
   * @param other the other function
   */
  public boolean equals(FunctionInfo other) {
    return this.similar(other) && this.retType.isEqualTo(other.retType);
  }

	/**
	 * Test if two functions are "similar", that is : they have the same
	 * name and same arguments, but may have different return types
	 */
	public boolean similar(FunctionInfo other) {
		return similar(other.name(), other.parameters());
	}

	/**
	 * Same function but taking the name and the parameters
	 */
	public boolean similar(String name, List<Type> params) {
		if (!this.name.equals(name))
      return false;

    if (this.parameters.size() != params.size())
      return false;

    for (int i = 0; i < this.parameters.size(); i++) {
      if (!this.parameters.get(i).isEqualTo(params.get(i)))
        return false;
    }

		return true;
	}

  /**
   * toString()
   */
  public String toString(String name) {
    String t = this.retType.toString() + " " + name + "(";

    for (int i = 0; i < this.parameters.size(); i++) {
      t += this.parameters.get(i);
      if (i == this.parameters.size() - 1)
        t += ", ";
    }

    t += ")";
    return t;
  }
}


