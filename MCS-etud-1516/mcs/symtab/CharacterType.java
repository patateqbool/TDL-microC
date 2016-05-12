/**
 * CharacterType -- class representing the character type
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class CharacterType implements Type {
	/**
	 * Constructor
	 */
	public CharacterType() {
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
	public boolean isCompatible(Type other) {$
		return (other instanceof IntegerType);
	}
}


