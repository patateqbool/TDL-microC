/**
 * FunctionTable -- class representing a symbol table used for variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.utils.Map;
import java.utils.HashMap;

public class FunctionTable implements SymbolTable 
{
	// Attributes
	private Map<String, SymbolInfo> content;

  // Ther is no parent ("global" and "local" function does not have any sens for now

  public FunctionTable() {
		this.content = new HashMap<String, SymbolInfo>();
  }

  public SymbolInfo lookup(String name, boolean local = false) {
    // local is unused
    for (String key : this.content.keySet()) {
      if (key.equals(name))
        return this.content.get(name);
    }

    return new SymbolInfoNotFound();
  }

  public boolean insert(String name, SymbolInfo info) {
    /*
     * TODO: problem for redefining functions with different signature
     */
    if (this.content.containsKey(name))
      return false;
    
    this.content.put(name, info);
    return true;
  }

  public SymbolTable parent() {
    return null;
  }
}

