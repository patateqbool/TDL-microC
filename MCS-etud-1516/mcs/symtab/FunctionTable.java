/**
 * FunctionTable -- class representing a symbol table used for variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.utils.HashMap;

class FunctionTable implements SymbolTable extends HashMap<String, SymbolInfo> 
{
  // Ther is no parent ("global" and "local" function does not have any sens for now

  public FunctionTable() {
  }

  public SymbolInfo lookup(String name, boolean local = false) {
    // local is unused
    for (String key : this.keySet()) {
      if (key.equals(name))
        return this.get(name);
    }

    return new SymbolInfoNotFound();
  }

  public boolean insert(String name, SymbolInfo info) {
    /*
     * TODO: problem for redefining functions with different signature
     */
    if (this.containsKey(name))
      return false;
    
    this.put(name, info);
    return true;
  }

  public SymbolTable parent() {
    return null;
  }
}

