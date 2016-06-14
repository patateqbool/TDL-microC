/**
 * DisplacementStack -- a tool class for making up adresses of composite types (eg: structs and arrays)
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

import java.util.ArrayList;

public class DisplacementList extends ArrayList<DisplacementPair> {
	private final static long serialVersionUID = 1l;

	public DisplacementList() {
		super();
	}

	public void add(int disp, boolean deref) {
		this.add(new DisplacementPair(disp, deref));
	}

  public DisplacementList pop_back() {
      this.remove(this.size() - 1);
      return this;
  }

	public String toString() {
		String res = "=== Displacement List ===\n";
		for (DisplacementPair dp : this) 
			res += "<" + dp.disp + "," + dp.deref + ">\n";
		return res + "=========================\n";
	}
}


