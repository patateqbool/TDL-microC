/**
 * MethodInfo -- class representing a method (ie : a function of a class)
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.symtab;

public class MethodInfo extends FunctionInfo {
	private ClassType parent;

	public MethodInfo(Type ret, ClassType parent) {
		super(ret);
		this.parent = parent;
	}
}


