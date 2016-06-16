/**
 * Class representing an alias table for using with typedef
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.List;
import java.util.ArrayList;

public class AliasTable {
    private class Alias {
        public String name;
        public Type type;
        public NamespaceInfo namespace;

        public Alias(String n, Type t, NamespaceInfo ni) {
            this.name = n;
            this.type = t;
            this.namespace = ni;
        }
    }

    private List<Alias> table;

    /**
     * Constructor
     */
    public AliasTable() {
        this.table = new ArrayList<Alias>();
    }

    /**
     * Append an alias to the table
     * @param name name of the type to alias
     * @param type type of the alias
     * @return true if it is possible
     */
    public boolean insert(String name, Type type, NamespaceInfo ni) {
        this.table.add(new Alias(name, type, ni));
        return true;
    }

    /**
     * Look up for an alias in the table
     * @param name name of the type to look up for
     * @return the type corresponding, or null if no type found
     */
    public Type lookup(String name, NamespaceInfo ni, NamespaceInfoList usedns) {
        for (Alias al : this.table) {
            if (name.equals(al.name) && (al.namespace.equals(ni) || usedns.contains(al.namespace)))
                return al.type;
        }
        return null;
    }

    public boolean exists(String name, NamespaceInfo ni, NamespaceInfoList usedns) {
        for (Alias al : this.table) {
            if (name.equals(al.name) && (al.namespace.equals(ni) || usedns.contains(al.namespace)))
                return true;
        }
        return false;
    }
}


