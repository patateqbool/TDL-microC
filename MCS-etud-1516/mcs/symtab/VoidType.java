/**
 * VoidType -- class that represent the void type
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class VoidType extends Type {
  /**
   * Constructor
   */
  public VoidType() {
    super(0);
  }

  /**
   * toString()
   */
  public String toString() {
    return "Void";
  }

  /**
   * Detect if this type is compatible
   */
  public boolean isCompatible() {
    return false;
  }
}


