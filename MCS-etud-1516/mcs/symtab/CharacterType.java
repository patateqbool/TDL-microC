/**
 * CharacterType -- class representing the character type
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class CharacterType extends Type {
	/**
	 * Constructor
	 */
	public CharacterType() {
		super(1); // 1 byte
	}

	/**
	 * toString();
	 */
	public String toString() {
		return "CHAR";
	}

	/**
	 * isCompatible()
	 */
	public boolean isCompatible(Type other) {
		return (other instanceof CharacterType) || (other instanceof IntegerType);
	}
}


