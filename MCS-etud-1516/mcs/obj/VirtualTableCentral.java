/**
 * VirtualTableCentral -- this singleton class allow for instanciating VirtualTable and
 * keep trace of them.
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import java.util.Map;
import java.util.HashMap;

public class VirtualTableCentral {
	static private VirtualTableCentral vtc;

	private Map<MethodInfo,VirtualTable> vtables;

	private VirtualTableCentral() {
		this.vtables = new HashMap<MethodInfo,VirtualTable>();
	}

	public VirtualTable vtable(MethodInfo mi) {
		VirtualTable vt = this.vtables.get(mi);

		if (vt == null) {
			vt = new VirtualTable();
			this.vtables.put(mi, vt);
		}

		return vt;
	}

	static public VirtualTableCentral instance() {
		if (VirtualTableCentral.vtc == null)
			VirtualTableCentral.vtc = new VirtualTableCentral();
		return VirtualTableCentral.vtc;
	}
}


