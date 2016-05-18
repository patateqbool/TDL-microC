/**
 * ConstantInfo -- class for representing a constant value
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class ConstantInfo extends VariableInfo {
  // Attributes
  private Object value;

  /**
   * Constructor
   * @param t type of the constant
   * @param v value of the constant
   */
  public ConstantInfo(Type t, Object v) {
    super(t, -1);
    this.value = v;
  }

  /**
   * The method displacement() should NEVER be called
   */
  @Override
  public int displacement() {
    throw new RuntimeException("Fatal error : the function displacement() on a constant value should never be called !");
  }

  /**
   * Return the value of the constant
   * @return an object containing the value of this constant
   */
  public Object value() {
    return this.value;
  }

  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Cst: " + this.type() + " := " + this.value;
  }
}


