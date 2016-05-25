/**
 * PointeurType -- class representing the pointeur type
 * 
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

public class PointerType extends Type {

	// Attributes
	private Type type;

	/**
	 * Constructor
	 */
	public PointerType(Type t) {
		super(4);
    type = t;
	}

	/**
	 * toString();
	 */
	public String toString() {
		return this.type + "*";
	}

	/**
	 * isCompatible()
	 */
	public boolean isCompatible(Type other) {
		if (other instanceof PointerType)
			return (((PointerType)other).getType().isCompatible(this.type));
		else
			return false;
	}

	public Type getType() {
		return this.type;
	}

  public Object getDefault() {
    return null;
  }
}
