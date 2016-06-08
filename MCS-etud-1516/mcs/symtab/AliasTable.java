/**
 * Class representing an alias table for using with typedef
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.HashMap;

public class AliasTable {
	private Map<String, Type> table;

	/**
	 * Constructor
	 */
	public AliasTable() {
		this.table = new HashMap<String, Type>();
	}

	/**
	 * Append an alias to the table
	 * @param name name of the type to alias
	 * @param type type of the alias
	 * @return true if it is possible
	 */
	public boolean insert(String name, Type type) {
		if (this.table.containsKey(name))
			return false;
		this.table.put(name, type);
		return true;
	}

	/**
	 * Look up for an alias in the table
	 * @param name name of the type to look up for
	 * @return the type corresponding, or null if no type found
	 */
	public Type lookup(String name) {
		return table.get(name);
	}
}


