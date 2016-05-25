/**
 * TypeExceptions -- class representing the exceptions use by Type
 * @author J. Guilbon
 * @version 0.1
 */

package mcs.compiler;

public class MCSSymbolNotFound extends MCSException {
  private static final long serialVersionUID = 1l;
	public MCSSymbolNotFound(String sym) {
		super("Symbol '" + sym + "' not found.");
	}
}
