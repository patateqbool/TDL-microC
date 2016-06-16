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

public class FunctionTable implements SymbolTable {
  // Attributes
  private List<FunctionInfo> content;

  // Ther is no parent ("global" and "local" function does not have any sens for now

  public FunctionTable() {
    this.content = new ArrayList<FunctionInfo>();
  }

  public boolean exists(String name, NamespaceInfo namespace, SymbolInfo si) {
		//trace System.out.println("Checking for existence of " + name);
		//trace System.out.println(this.toString());
    for (FunctionInfo csi : this.content) {
      if (csi.similar(name, ((FunctionInfo)si).parameters())) {
        if (csi.namespace().equals(namespace)) {
					// System.out.println("I found something ! \\o/");
          return true;
				}
      }
    }

    return false;
  }

  public boolean exists(SymbolInfo si, NamespaceInfo namespace) {
    for (FunctionInfo csi : this.content) {
      if (csi.equals(si)) {
        if (csi.namespace().equals(namespace))
          return true;
      }
    }

    return false;
  }

	public boolean exists(String name, NamespaceInfo namespace) {
		for (FunctionInfo csi : this.content) {
			if (csi.name().equals(name) && namespace.equals(csi.namespace()))
				return true;
		}
		return false;
	}

  /**
   * Note: this function only serve to see if there is a function
   * named "name".
   * Retrieving the actual function must be done with a complete
   * set of parameters
   */
  public SymbolInfo lookup(String name, NamespaceInfo namespace, boolean local) {
    for (FunctionInfo fi : this.content) {
      if (fi.name().equals(name)) {
        if (fi.namespace().equals(namespace))
          return fi;
      }
		}
    return null;
  }

  public SymbolInfo lookup(String name,  NamespaceInfo namespace, List<Type> params) {
    for (FunctionInfo fi : this.content) {
      if (fi.similar(name, params)) {
        if (fi.namespace().equals(namespace))
          return fi;
      }
    }

    return null;
  }

  public boolean insert(String name, SymbolInfo info) {
    return this.insert(name, info.namespace(), info);
  }

  public boolean insert(String name, NamespaceInfo namespace, SymbolInfo info) {
    if (exists(name, namespace, info))
      return false;

    FunctionInfo fi = (FunctionInfo)info;
    fi.setName(name);
    this.content.add(fi);
    return true;
  }

  public SymbolTable parent() {
    return null;
  }

  public List<String> symbols() {
    List<String> l = new ArrayList<String>();
    for (FunctionInfo fi : this.content)
      l.add(fi.name());
    return l;
  }

  public List<Type> symbolsTypes() {
    return null;
  }

	public String toString() {
		String res = "=== Function table ===\n";
		for (FunctionInfo fi : this.content) {
			res += fi + "\n";
		}
		return res +="======================\n";
	}
}

