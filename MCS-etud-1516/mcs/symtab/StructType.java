/**
 * StructType -- class representing the Struct type
 * 
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

import java.util.Collection;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

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

	/**
	 * Get the number of fields
	 */
	public int numberOfFields() {
		return this.fields.size();
	}

	/**
	 * Get the fields
	 */
	public List<String> fields() {
		return this.fields.fields();
	}

  /**
   * Get the type of the fields
   */
  public Collection<Type> fieldsTypes() {
    return this.fields.types();
  }

	/**
	 * Get the info of the requested field
	 * @param field field to retrieve
	 * @param basedisp base displacement of the structure, such as struct.first_field as a basedisp displacement
	 * @return a VariableInfo of the field
	 */
	public VariableInfo getInfo(String field, int basedisp) {
		return new VariableInfo(
					this.fields.find(field),
					basedisp + this.fields.sumSizes(field)
				);
	}
}
	

