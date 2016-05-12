/**
 * StructType -- class representing the Struct type
 * 
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

public class StructType implements Type {

	// Attributes
	private Champs champs;

	/**
	 * Constructor
	 */
	public PointeurType() {
	}

	/**
	 * toString();
	 */
	public String toString() {
		return "PTR";
	}

	/**
	 * isCompatible()
	 */
	public boolean isCompatible(Type other) {
		return (other instanceof PointerType) and other.type.isCompatible(this.type);
	}
	
	public Type getType() {
		return this.type;
	}
}
	

