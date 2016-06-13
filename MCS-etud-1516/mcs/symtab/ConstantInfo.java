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

		if (v instanceof String) {
    	if (t instanceof IntegerType)
      	this.value = Integer.parseInt((String)v);
    	else if (t instanceof CharacterType)
      	this.value = new Integer((((String)v).charAt(0)));
    	else if (t instanceof StringType)
      	this.value = fromString((String)v);
			else
    		this.value = v;
		} else {
			this.value = v;
		}
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
	static public Object fromString(String str) {
		List<Object> lc = new ArrayList<Object>();
		for (int i = 0; i < str.length(); i++)
			lc.add(str.charAt(i));
		return lc;
	}
}


