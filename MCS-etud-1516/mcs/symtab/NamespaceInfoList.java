/**
 * Represent a list of namespace for using with "using"
 */
package mcs.symtab;

import java.util.ArrayList;

public class NamespaceInfoList extends ArrayList<NamespaceInfo> {
    private static final long serialVersionUID = 1l;
    public NamespaceInfoList() {
        super();
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof NamespaceInfo) {
            NamespaceInfo oni = (NamespaceInfo)o;
            for (NamespaceInfo ni : this) {
                if (ni.equals(oni))
                    return true;
            }
        } else {
            return super.contains(o);
        }

        return false;
    }
		
		public String toString() {
			String str = "";  
			for (NamespaceInfo ni : this)
				str += ni.label() + "\n";
			return str;
		}

}


