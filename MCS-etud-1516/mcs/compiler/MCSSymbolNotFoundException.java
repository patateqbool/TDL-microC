/**
 * TypeExceptions -- class representing the exceptions use by Type
 * @author J. Guilbon
 * @version 0.1
 */

package mcs.compiler;

public class MCSSymbolNotFoundException extends MCSException {
  private static final long serialVersionUID = 1l;
	public MCSSymbolNotFoundException(String sym) {
		super("Symbol '" + sym + "' not found.");
	}
}
