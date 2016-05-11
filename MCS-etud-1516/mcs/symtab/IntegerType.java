/**
 * IntegerType -- class that represents the integer type
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

class IntegerType implements Type {
	/**
	 * Constructor
	 */
	public IntegerType() {}

	/**
	 * toString()
	 */
	public String toString() {
		return "Integer";
	}

	/**
	 * Detect if this type is compatible
	 */
	public boolean isCompatible(Type other) {
		return false;
	}
}


