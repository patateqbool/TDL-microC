/**
 * VariableTable -- class representing a symbol table used for the variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class VariableTable implements SymbolTable {
  // Attributes
	private Map<String, SymbolInfo> content;
  private SymbolTable parent; // Parent of this table
	private int displacement;

  /**
   * Constructor
   * Create a table from a parent table that could be null
   */
  public VariableTable(VariableTable p) {
    this.parent = p;
		this.content = new HashMap<String, SymbolInfo>();

		if (p != null)
			this.displacement = p.offset();
		else
			this.displacement = 0;
  }

	/**
	 * Constructor
	 * Create a table from a null parent table
	 */
	public VariableTable() {
		this(null);
	}

	/**
	 * Get the current displacement of the table
	 */
	public int offset() {
		return this.displacement;
	}

	public Set<String> symbols() {
		return this.content.keySet();
	}

  /**
   * Look up into the table
   */
  public SymbolInfo lookup(String name, boolean local) {
    for (String key : this.content.keySet()) {
      if (key.equals(name))
        return this.content.get(name);
    }

    if (!local)
      return parent().lookup(name, false);

    return new SymbolInfoNotFound();
  }

	/**
	 * Insert a symbol into the table
	 */
  public boolean insert(String name, SymbolInfo info) {
    if (this.content.containsKey(name))
      return false;
    this.content.put(name, info);
    return true;
  }

	/**
	 * Get the parent of this table
	 */
  public SymbolTable parent() {
    return this.parent;
  }
}


