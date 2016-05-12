/**
 * StructField -- class representing the struct fields
 *
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

public class StructField extends ArrayList<Field> {

	// Attributes
	private Type type;

	/**
	 * Constructor
	 */
	public PointeurType(Type t) {
            type = t;
	}

	/**
	 * find(String name);
	 */
	public Field find(String name) {
	        for (Field f : this) {
                    if (f.getName().equals(name))
                        return f;
                }
                return null;
	}

	/**
	 * getLen();
	 */
	public int getLen() {
            int t = 0;
            for (Field f : this) {
                t += f.getType().taille;
            }
            return t;
	}

        /**
	 * toString();
	 */
	public String toString() {
		return "PTR";
	}

	/**
	 * isCompatible()
	 */
	public boolean isCompatible(Type other) {
		return (other instanceof PointerType) && other.getType.isCompatible(this.type);
	}

	public Type getType() {
		return this.type;
	}
}
