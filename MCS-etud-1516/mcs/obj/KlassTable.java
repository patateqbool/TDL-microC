/**
 * KlassTable -- the table of classes.
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import java.util.List;
import java.util.ArrayList;

public class KlassTable {
	private List<Klass> content;

	public KlassTable() {
		this.content = new ArrayList<Klass>();
	}

	public boolean exists(String name) {
		for (Klass k : this.content) {
			if (k.name().equals(name))
				return true;
		}
		return false;
	}

	public Klass lookup(String name) {
		for (Klass k : this.content) {
			if (k.name().equals(name))
				return k;
		}
		return null;
	}

	public boolean insert(Klass k) {
		if (exists(k.name()))
			return false;
		
		this.content.add(k);
		return true;
	}
}


