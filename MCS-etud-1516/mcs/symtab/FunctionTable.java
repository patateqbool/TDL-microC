/**
 * FunctionTable -- class representing a symbol table used for variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class FunctionTable implements SymbolTable 
{
	// Attributes
  private List<String> symbols;
	private Map<String, SymbolInfo> content;

  // Ther is no parent ("global" and "local" function does not have any sens for now

  public FunctionTable() {
		this.content = new HashMap<String, SymbolInfo>();
    this.symbols = new ArrayList<String>();
  }

	public boolean exists(String name, SymbolInfo si) {
		if (this.content.get(name) != null) {
			FunctionInfo ffi = (FunctionInfo)si;
			FunctionInfo ofi = (FunctionInfo)this.lookup(name, true);

			if (ffi.parameters().size() == ofi.parameters().size()) {
				for (int i = 0; i < ffi.parameters().size(); i++) {
					if (!ffi.parameters().get(i).isEqualTo(ofi.parameters().get(i)))
						return false;
				}
				return true;
			}
		}

		return false;
	}

  public SymbolInfo lookup(String name, boolean local) {
    // local is unused
    for (String key : this.symbols) {
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
    this.symbols.add(name);
    return true;
  }

  public SymbolTable parent() {
    return null;
  }

	public List<String> symbols() {
		return this.symbols;
	}
}

