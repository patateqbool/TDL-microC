/**
 * SymbolTable -- interface representing a symbol table for the microC/microC# language
 * 
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.List;

public interface SymbolTable {
	/**
	 * Detect whether or not the given symbol exists in the table
	 * @param name symbol name
	 * @param si symbol to test
	 * @return true if the symbol exists
	 */
	boolean exists(String name, NamespaceInfo namespace, SymbolInfo si);

  /**
   * Globally look up into the table.
   * @param name name to look up
   * @param local whether or not to search locally or globally
   * @return the symbol info, or the special variable SymbolInfoNotFound
   */
  SymbolInfo lookup(String name, NamespaceInfo namespace, boolean local);

  /**
   * Insert a symbol into the table
   * @param name name of the symbol
   * @param info information of the symbol
   * @return a boolean, indicating if the insertion has been done
   */
  boolean insert(String name, SymbolInfo info);

  /**
   * Get the parent of this table
   * @return the parent
   */
  SymbolTable parent();

	/**
	 * Get the list of symbol in this table
	 * @return a SET of symbols
	 */
	List<String> symbols();
}


