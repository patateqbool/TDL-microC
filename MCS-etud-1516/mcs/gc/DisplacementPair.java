/**
 * DisplacementPair -- a tool class representing a (int, bool) pair, for calculating
 * address of composite types; the first field is the displacement and the second one
 * determines whether or not we should dereference the value
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

public class DisplacementPair {
	public int disp;
	public boolean deref;

	public DisplacementPair(int disp, boolean deref) {
		this.disp = disp;
		this.deref = deref;
	}
}


