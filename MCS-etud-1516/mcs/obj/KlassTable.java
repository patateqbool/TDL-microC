/**
 * KlassTable -- class containing all the classes
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.obj;

import java.util.List;
import java.util.ArrayList;
import mcs.symtab.NamespaceInfo;

public class KlassTable {
    private List<Klass> content;

    public KlassTable() {
        this.content = new ArrayList<Klass>();
    }

    public boolean exists(String name, NamespaceInfo ns, List<NamespaceInfo> usedns) {
        for (Klass k : this.content) {
            if (k.name().equals(name) && (k.namespace().equals(ns) || usedns.contains(k.namespace())))
                return true;
        }

        return false;
    }

    // hahaha je suis caché
    public Klass lookup(String name, NamespaceInfo ns, List<NamespaceInfo> usedns) {
        for (Klass k : this.content) {
            if (k.name().equals(name) && (k.namespace().equals(ns) || usedns.contains(k.namespace())))
                return k;
        }
        return null;
    }

    public boolean insert(Klass k) {
        this.content.add(k);
        return true;
    }
}


