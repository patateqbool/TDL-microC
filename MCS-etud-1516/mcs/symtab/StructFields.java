/**
 * StructFields -- class representing the list of the fields of a structure
 *
 * @author G. DUpont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import mcs.compiler.MCSSymbolNotFound;
import mcs.compiler.MCSSymbolAlreadyDefineException;

class StructFields {
	// The list of the fields
	/*
	 * Problem : when reading the keys of the fields, we iterate over set, which is
	 * non-deterministic. Besides, we want the struct to have ORDERED fields;
	 * Solution : creating an "orderer" list, which associate number of the field to
	 * name of the field, therefore ordering the field map
	 */
	private Map<String, Type> fields;
	private List<String> fieldsnb;
	
	/**
	 * Constructor
	 */
	public StructFields() {
		fields = new HashMap<String, Type>();
		fieldsnb = new ArrayList<String>();
	}
	
	/**
	 * Insert a field in the struct
	 * @param n name of the field
	 * @param t type of the field
	 */
	public void insert(String n, Type t) throws MCSException {
		type = this.fields.put(n, t);
		if (f != null) {
			this.fields.put(n, type);
			throw new MCSSymbolAlreadyDefineException();
		}
		this.fieldsnb.add(n);
	}
	
	/**
	 * Find the type of a field, raise an exception if the field is not found
	 * @param n name of the field
	 * @return t type of the field
	 */ 
	public Type find(String n) {
		t = this.fields.get(n);
		if (t == null) {
			throw new MCSSymbolNotFound();
		}
		return t;
	}
	
	/**
	 * Return the number of field in the struct
	 * @return the number of field in the struct
	 */
	public int size() {
		return this.fieldsnb.size();
	}
	
	/**
	 * Return the list ordered of the field in the struct
	 * @return the list ordered of the field in the struct
	 */
	public List<String> fields() {
		return this.fieldsnb;
	}

	public int sumSizes() {
		int size = 0;
		for (Type t : this.fields.values()) {
			int ts = t.size();

			size += (ts%4 == 0 ? ts : ts + (4 - (ts % 4)));
		}
		return size;
	}

	/**
	 * Sum all the sizes of the fields preceding the field "to"
	 * @param to field
	 * @return s = sum_{k=0}^{i(to)-1} size(i)
	 * Note: the sum size is padded to prevent alignment problems.
	 * Thus, if a field is of a size not a multiple of 4, the next field will be at a multiple of 4 anyway.
	 * TODO: make better this system
	 */
	public int sumSizes(String to) {
		int size = 0;
		for (String n : this.fieldsnb) {
			if (n.equals(to))
				break;
			
			int ts = this.fields.get(n).size();
			if (ts % 4 == 0)
				size += ts;
			else
				size += ts + (4 - (ts % 4));
		}
		return size;
	}

	public String toString() {
		String str = "";
		for (String n : this.fields.keySet())
			str = str + n + ": " + this.fields.get(n) + ";";
		return str;
	}

}


