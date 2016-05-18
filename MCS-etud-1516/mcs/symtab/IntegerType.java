/**
 * IntegerType -- class that represents the integer type
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class IntegerType extends Type {
	/**
	 * Constructor
	 */
	public IntegerType() {
		super(4); // 4 bytes
	}

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
		return (other instanceof IntegerType);
	}
}


