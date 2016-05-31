/**
 * VoidType -- class that represent the void type
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class VoidType extends SimpleType {
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
  public boolean isCompatible(Type other) {
    return (other instanceof VoidType);
  }

	/**
	 * Default
	 */
	public Object getDefault() {
		return null;
	}
}


