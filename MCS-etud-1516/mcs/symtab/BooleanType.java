/**
 * BooleanType -- type representing a boolean.
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class BooleanType extends SimpleType {
  public BooleanType() {
    super(4); // ?
  }

  /**
   * Virtual definitions
   */
  public String toString() {
    return "Boolean";
  }

  public boolean isCompatible(Type other) {
    return (other.isEqualTo(this));
  }

  public Object getDefault() {
    return new Boolean(false);
  }
}


