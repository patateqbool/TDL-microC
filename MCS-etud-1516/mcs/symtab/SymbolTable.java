/**
 * SymbolTable -- interface representing a symbol table for the microC/microC# language
 * 
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.utils.Map;

interface SymbolTable implements Map<String, SymbolInfo> {
  /**
   * Globally look up into the table.
   * @param name name to look up
   * @param local whether or not to search locally or globally
   * @return the symbol info, or the special variable SymbolInfoNotFound
   */
  public SymbolInfo lookup(String name, boolean local = false);

  /**
   * Insert a symbol into the table
   * @param name name of the symbol
   * @param info information of the symbol
   * @return a boolean, indicating if the insertion has been done
   */
  public boolean insert(String name, SymbolInfo info);

  /**
   * Get the parent of this table
   * @return the parent
   */
  public SymbolTable parent();
}


