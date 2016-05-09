/**
 * VariableInfo -- class for representing a variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

class VariableInfo implements SymbolInfo {
  // Attributes
  private Type type; // Type of the variable
  private int displacement; // Displacement of the variable in the memory

  /**
   * Constructor.
   * @param t type of the variable
   * @param disp displacement of the variable
   */
  public VariableInfo(Type t, int disp) {
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
   * Convert the information into string.
   */
  @Override
  public String toString() {
    return "Var: " + this.type.toString() + " [" + this.displacement + "];";
  }
}


