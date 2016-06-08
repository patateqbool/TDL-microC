/**
 * VariableTable -- class representing a symbol table used for the variable
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import mcs.util.OrderedMap;

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
    this.content = new OrderedMap<String, SymbolInfo>();

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
    List<String> res = new ArrayList<String>();
    for (String s : this.content.keySet()) {
      res.add(s);
    }
    return res;
  }

  public boolean exists(String name, NamespaceInfo namespace, SymbolInfo si) {
    if (this.content.get(name) != null) {
      if (this.content.get(name).namespace() == namespace)
        return true;
    }
    return false;
  }

  /**
   * Look up into the table
   */
  public SymbolInfo lookup(String name, NamespaceInfo ns, boolean local) {
    SymbolInfo si = this.content.get(name);

    if (si != null) {
      if (si.namespace() == ns)
        return si;
    }

    if (!local && this.parent != null)
      return this.parent.lookup(name, ns, false);

    return null;
  }

  /**
   * Insert a symbol into the table
   */
  public boolean insert(String name, SymbolInfo info) {
    VariableInfo vi = (VariableInfo)info;
    if (this.content.containsKey(name))
      return false;
    this.content.put(name, vi);

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
  public boolean insert(String name, Type type, NamespaceInfo ns) {
    VariableInfo vi = new VariableInfo(type, this.displacement, ns);
    return this.insert(name, vi);
  }

  /**
   * Get the parent of this table
   */
  public SymbolTable parent() {
    return this.parent;
  }

  public List<Type> symbolsTypes() {
    List<Type> res = new ArrayList<Type>();
    for (SymbolInfo si : this.content.values()) {
      res.add(((VariableInfo)si).type());
    }
    return res;
  }
}


