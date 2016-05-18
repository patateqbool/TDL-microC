/**
 * VariableTable -- class representing a symbol table used for the variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class VariableTable implements SymbolTable {
  // Attributes
	private Map<String, SymbolInfo> content;
  private List<String> symbols; // we need to store an ordered list of symbols (for pop/push eg)
  private SymbolTable parent; // Parent of this table
	private int displacement;

  /**
   * Constructor
   * Create a table from a parent table that could be null
   */
  public VariableTable(VariableTable p) {
    this.parent = p;
		this.content = new HashMap<String, SymbolInfo>();
    this.symbols = new ArrayList<String>();

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

	public List<String> symbols() {
		return this.symbols;
	}

  /**
   * Look up into the table
   */
  public SymbolInfo lookup(String name, boolean local) {
    for (String key : this.symbols) {
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
    VariableInfo vi = (VariableInfo)info;
    if (this.content.containsKey(name))
      return false;
    this.content.put(name, vi);
    this.symbols.add(name);

    int ts = vi.type().size();
    this.displacement += (ts % 4 == 0 ? ts : ts + (4 - (ts % 4)));
    return true;
  }

  /**
   * Insert a symbol into the table from its name and type, giving it
   * a default displacement
   * @param name name of the symbol
   * @param type type of the symbol
   * @return true if it is possible
   */
  public boolean insert(String name, Type type) {
    VariableInfo vi = new VariableInfo(type, this.displacement);
    return this.insert(name, vi);
  }

	/**
	 * Get the parent of this table
	 */
  public SymbolTable parent() {
    return this.parent;
  }
}


