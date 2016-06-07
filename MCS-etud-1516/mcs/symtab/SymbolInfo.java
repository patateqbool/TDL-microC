/**
 * SymbolInfo -- interface representing the informations of a symbol
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public abstract class SymbolInfo {

  private NamespaceInfo namespace;

  public SymbolInfo(NamespaceInfo namespace) {
      this.namespace = namespace;
  }

  /**
   * Convert the information of the symbol into a string.
   * This allows for easy debugging.
   */
  abstract public String toString();

  public NamespaceInfo namespace() {
      return namespace;
  }
}


