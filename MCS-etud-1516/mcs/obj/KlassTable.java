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

  public boolean exists(String name, NamespaceInfo ns) {
    for (Klass k : this.content) {
      if (k.name().equals(name) && k.namespace() == ns)
        return true;
    }

    return false;
  }
}


