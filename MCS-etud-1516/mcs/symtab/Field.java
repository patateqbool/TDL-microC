/**
 * Field -- class that represents the field use in struct
 *
 * @author J. Guilbon
 * @version 0.1
 */
package mcs.symtab;

public class Field {
        private String name;
        private Type type;
	/**
	 * Constructor
	 */
	public Field(String name, Type type) {
            this.name = name;
            this.type = type;
        }

	/**
	 * toString()
	 */
	public String toString() {
		return "Field";
        }

        /**
         * getName()
         */
        public String getName() {
            return name;
        }

        /**
         * getType()
         */
        public String getType() {
            return type;
        }
}

