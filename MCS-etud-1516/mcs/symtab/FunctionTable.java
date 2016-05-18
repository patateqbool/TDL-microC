/**
 * FunctionTable -- class representing a symbol table used for variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class FunctionTable implements SymbolTable 
{
	// Attributes
	private Map<String, SymbolInfo> content;

  // Ther is no parent ("global" and "local" function does not have any sens for now

  public FunctionTable() {
		this.content = new HashMap<String, SymbolInfo>();
  }

  public SymbolInfo lookup(String name, boolean local) {
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

    if (!(info instanceof FunctionInfo))
      return false;

    FunctionInfo fi = (FunctionInfo)info;
    String label = "_" + name + "@" + fi.returnType();

    for (Type t : fi.parameters())
      label += "$" + t;

    fi.setLabel(label);
    
    this.content.put(name, fi);
    return true;
  }

  public SymbolTable parent() {
    return null;
  }

	public Set<String> symbols() {
		return this.content.keySet();
	}
}

