/**
 * FunctionInfo -- class representing a function
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

import java.utils.List;
import java.utils.ArrayList;

class FunctionInfo implements SymbolInfo {
  // Attributes
  private Type retType; // Return type
  private List<Type> parameters; // Parameters

  /**
   * Create a function symbol from a return type and a list of parameters
   * @param ret return type of the function
   * @param params list of parameters of the function
   */
  public FunctionInfo(Type ret, List<Type> params) {
    this.retType = ret;
    this.parameters = params;
  }

  /**
   * Create a function symbol from a return type and an empty list of parameters
   * @param ret return type of the function
   */
  public FunctionInfo(Type ret) {
    this(ret, List<Type>());
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
}


