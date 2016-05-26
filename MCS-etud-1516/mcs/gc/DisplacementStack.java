/**
 * DisplacementStack -- a tool class for making up adresses of composite types (eg: structs and arrays)
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

import java.util.Stack;

public class DisplacementStack extends Stack<DisplacementPair> {
	private final static long serialVersionUID = 1l;

	public DisplacementStack() {
		super();
	}

	public void push(int disp, boolean deref) {
		this.push(new DisplacementPair(disp, deref));
	}
}


