/**
 * Represent the default namespace
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class DefaultNamespaceInfo extends NamespaceInfo {
	public DefaultNamespaceInfo() {
		super("__default__", null);
	}

  @Override
  public boolean equals(NamespaceInfo ni) {
      return (ni instanceof DefaultNamespaceInfo);
  }

  @Override
  public String label() {
      return "";
  }
}


