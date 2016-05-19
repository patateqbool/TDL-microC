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

	/**
	 * Get the displacement of the index id
	 * @param id id of the case
	 * @return general displacement
	 */
	public int idDisplacement(int id) {
		int typesize = this.type.size();
		int alignedtypesize = 4 + (typesize % 4 == 0 ? typesize : typesize + (4 - (typesize % 4))); // The first "cell" contains the size on 4 bytes
		return id*alignedtypesize;
	}

	public Object getDefault() {
		return null;
	}
}
