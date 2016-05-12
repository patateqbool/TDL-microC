/**
 * StructFields -- class representing the list of the fields of a structure
 *
 * @author G. DUpont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.HashMap;

class StructFields {
	// The list of the fields
	private Map<String, Type> fields;

	public StructFields() {
		fields = new HashMap<String, Type>();
	}

	public void insert(String n, Type t) {
		// TODO: errors when field already exists
		this.fields.put(n, t);
	}

	public Type find(String n) {
		// TODO: errors when field does not exists
		return fields.get(n);
	}

	public int sumSizes() {
		int size = 0;
		for (Type t : this.fields.values()) {
			size = size + t.size();
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


