/**
 * ArrayType -- class representing the array type
 *
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

public class ArrayType extends Type {

	// Attributes
        private int size;
	private Type type;

	/**
	 * Constructor
	 */
	public ArrayType(Type t, int s) {
		super(t.size()*s);
    type = t;
	}

	/**
	 * toString();
	 */
	public String toString() {
		return "ARRAY";
	}

	/**
	 * isCompatible()
	 */
	public boolean isCompatible(Type other) {
		return (other instanceof ArrayType) && (other.size() == this.size());
	}

	public Type getType() {
		return this.type;
	}

	public Object getDefault() {
		return null;
	}
}
