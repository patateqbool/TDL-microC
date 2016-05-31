/**
 * CompositeType -- specialisation class for composite types (classes, arrays, structs, ...).
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public abstract class CompositeType extends Type {
	private int realSize;

	public CompositeType(int realSize) {
		super(4); // Basically, we always store that as a pointer
		this.realSize = realSize;
	}

	public int realSize() {
		return this.realSize;
	}
}


