/**
 * VariableTable -- class representing a symbol table used for the variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.utils.HashMap;

class VariableTable implements SymbolTable extends HashMap<String, SymbolInfo> {
  // Attributes
  private SymbolTable parent; // Parent of this table

  /**
   * Constructor
   * Create a table from a parent table that could be null
   */
  public VariableTable(SymbolTable p = null) {
    this.parent = p;
  }

  /**
   * Look up into the table
   */
  public SymbolInfo lookup(String name, boolean local = false) {
    for (String key : this.keySet()) {
      if (key.equals(name))
        return this.get(name);
    }

    if (!local)
      return parent().lookup(name, true);

    return new SymbolInfoNotFound();
  }

  public boolean insert(String name, SymbolInfo info) {
    if (this.containsKey(name))
      return false;
    this.put(name, info);
    return true;
  }

  public SymbolTable parent() {
    return this.parent;
  }
}


