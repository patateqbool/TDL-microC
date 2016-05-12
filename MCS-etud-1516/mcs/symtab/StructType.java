/**
 * StructType -- class representing the Struct type
 * 
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.HashMap;

public class StructType extends Type {

	// Attributes
	private StructFields fields;

	/**
	 * Constructor
	 */
	public StructType(StructFields f) {
		super(f.sumSizes());
		this.fields = f;
	}

	/**
	 * toString();
	 */
	public String toString() {
		return "struct { " + this.fields + " }";
	}

	/**
	 * isCompatible()
	 */
	public boolean isCompatible(Type other) {
		return false;
	}
}
	

