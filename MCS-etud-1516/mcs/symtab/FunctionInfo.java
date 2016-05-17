/**
 * FunctionInfo -- class representing a function
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.util.List;
import java.util.ArrayList;

public class FunctionInfo implements SymbolInfo {
  // Attributes
  private Type retType; // Return type
  private List<Type> parameters; // Parameters
  private String label;

  /**
   * Create a function symbol from a return type and a list of parameters
   * @param ret return type of the function
   * @param params list of parameters of the function
   */
  public FunctionInfo(Type ret) {
    this.retType = ret;
    this.parameters = new ArrayList<Type>();
  }

  /**
   * Get the return type of the function
   * @return the return type of the function
   */
  public Type returnType() {
    return this.retType;
  }

  /**
   * Get the parameter list of the function
   * @return the list of the parameters
   */
  public List<Type> parameters() {
    return this.parameters;
  }

  /**
   * Set the label of the function. This is done AFTER storing it, by the function table
   * @param lbl new label
   */
  public void setLabel(String lbl) {
    this.label = lbl;
  }

  /**
   * Get the label of the function.
   * @return the label
   */
  public String label() {
    return this.label;
  }

  /**
   * Add a parameter to the list of parameters of the function
   * @param t the type to add
   */
  public void add(Type t) {
    this.parameters.add(t);
  }

  /**
   * Calculate displacement of given parameter number
   * @param n number of the argument for which to get the displacement
   * @return the displacement
   */
  public int displacement(int n) {
    int disp = -12;

    for (int i = 0; i <= n; i++) {
      int ts = this.parameters.get(i).size();

      if (ts % 4 == 0) // already aligned
        disp -= ts;
      else
        disp -= ts + (4 - (ts % 4));
    }

    return disp;
  }

  /**
   * toString()
   */
  public String toString(String name) {
    String t = this.retType.toString() + " " + name + "(";

    for (int i = 0; i < this.parameters.size(); i++) {
      t += this.parameters.get(i);
      if (i == this.parameters.size() - 1)
        t += ", ";
    }

    t += ")";
    return t;

  }
}


