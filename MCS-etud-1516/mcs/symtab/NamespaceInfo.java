/**
 * Namespace -- class for representing a namespace
 *
 * @author J. Guilbon
 * @version 0.1
 */

package mcs.symtab;

public class NamespaceInfo {
    // Attributes
    public String name; // Name of the namespace
    public Namespace parent; // The parent of the namespace
    
    /**
     * @param n name of the namespace
     */
    public Namespace(String n) {
        this.name = n;
        this.parent = null;
    }

    /**
     * @param n name of the namespace
     * @param ns Parent of the namespace
     */
    public Namespace(String n, Namespace ns) {
        this.name = n;
        this.parent = ns;
    }
    
    /**
     * Get the name of the namespace
     * @return the name
     */
    public String name() {
        return this.name;
    }

    /**
     * Get the parent of the namespace
     * @return the parent
     */
    public Namespace parent() {
        return this.parent;
    }

    /**
     * Convert the information into string
     */
    @Override
    public String toString() {
        return "Namespace : " + this.name;
    }
}
