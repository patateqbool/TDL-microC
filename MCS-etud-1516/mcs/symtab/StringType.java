/**
 * StringType -- class representing a string.
 * String are not directly stored in the memory; it is actually represented by
 * an array of characters.
 * This class should be mainly used for direct constant, not for memory management
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class StringType extends ArrayType {
	public StringType(int s) {
		super(new CharacterType(), s);
	}
}


