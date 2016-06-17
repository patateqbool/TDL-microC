/**
 * Internal exception indicating that a function has not been used correctly
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.compiler;

public class MCSWrongUseException extends MCSException {
	private static final long serialVersionUID = 1L;
	public MCSWrongUseException(String m, String compl) {
		super("Method '" + m + "' is not supposed to be used like that: " + compl);
	}
}


