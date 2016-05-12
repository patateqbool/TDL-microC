/**
 *
 * Type -- interface that represents a type.
 * This is used for variables, parameters, functions, etc.
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public abstract class Type {

    private int size;
    /**
   * Convert a type into string for easy printing
   * @return the string
   */
  public String toString();

  /**
   * Detect if this type is compatible with another.
   * If other is compatible with this, it means that we can cast this to other.
   * @param other the other type to be compared with
   * @return true if other is compatible with this.
   */
  boolean isCompatible(Type other);
}


