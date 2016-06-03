/**
 * ConstantInfo -- class for representing a constant value
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.List;
import java.util.ArrayList;

public class ConstantInfo extends VariableInfo {
  // Attributes
  private Object value;

  /**
   * Constructor
   * @param t type of the constant
   * @param v value of the constant
   */
  public ConstantInfo(Type t, Object v) {
    super(t, -1, null);
    this.value = v;
  }

	/**
	 * Default constructor (constructing a default value)
	 */
	public ConstantInfo(Type t) {
		this(t, t.getDefault());
	}

  /**
   * The method displacement() should NEVER be called
   */
  @Override
  public int displacement() {
    throw new RuntimeException("Fatal error : the function displacement() on a constant value should never be called !");
  }

  /**
   * Return the value of the constant
   * @return an object containing the value of this constant
   */
  public Object value() {
    return this.value;
  }

  /**
   * toString()
   */
  @Override
  public String toString() {
    return "Cst: " + this.type() + " := " + this.value;
  }


	/**
	 * A bunch of static function for creating constant from anything
	 */
	static public ConstantInfo fromInt(int v) {
		return new ConstantInfo(new IntegerType(), new Integer(v));
	}

	static public ConstantInfo fromChar(char v) {
		return new ConstantInfo(new CharacterType(), new Character(v));
	}

	static public ConstantInfo fromArray(List<Object> obj, Type arraytype) {
		return new ConstantInfo(new ArrayType(arraytype, obj.size()), obj);
	}

	static public ConstantInfo fromString(String str) {
		List<Object> lc = new ArrayList<Object>();
		for (int i = 0; i < str.length(); i++)
			lc.add(str.charAt(i));
		return new ConstantInfo(new StringType(lc.size()), lc);
	}
}


