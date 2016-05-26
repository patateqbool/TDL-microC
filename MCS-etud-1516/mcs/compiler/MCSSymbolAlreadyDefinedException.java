/**
 * TypeExceptions -- class representing the exceptions use by Type
 * @author J. Guilbon
 * @version 0.1
 */

package mcs.compiler;

public class MCSSymbolAlreadyDefinedException extends MCSException {
  private static final long serialVersionUID = 1l;
	public MCSSymbolAlreadyDefinedException(String symbol) {
		super("Symbol '" + symbol + "' has already been defined");
	}
}
