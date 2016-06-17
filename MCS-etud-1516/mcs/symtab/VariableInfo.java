/**
 * VariableInfo -- class for representing a variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import mcs.gc.Register;

public class VariableInfo extends SymbolInfo {
  // Attributes
  private Type type; // Type of the variable
  private int displacement; // Displacement of the variable in the memory
	private Register reg;

  /**
   * Constructor.
   * @param t type of the variable
   * @param disp displacement of the variable
   * @param ns namespace of the variable
   */
  public VariableInfo(Type t, int disp, NamespaceInfo ns) {
    super(ns);
    this.type = t;
    this.displacement = disp;
  }

  /**
   * Get the type of the variable
   * @return the type
   */
  public Type type() {
    return this.type;
  }

  /**
   * Get the displacement of the variable
   * @return the displacement
   */
  public int displacement() {
    return this.displacement;
  }

	/**
	 * Assign a register to the variable
	 * @param reg register to assign
	 */
	public void assignRegister(Register reg) {
		this.reg = reg;
		this.reg.setStatus(Register.Status.Loaded);
	}

	/**
	 * Free the register, making it unused
	 */
	public void freeRegister() {
		this.reg.setStatus(Register.Status.Used);
	}

	/**
	 * Get assigned register of the variable
	 * @return the register
	 */
	public Register register() {
		return this.reg;
	}

  /**
   * Convert the information into string.
   */
  @Override
  public String toString() {
    return this.namespace() + this.type.toString() + " [" + this.displacement + "];";
  }
}


