package mcs.gc;

import mcs.compiler.MCSException;

/**
 * Cette interface décrit une machine cible. A compléter, selon votre modèle
 * 
 * @author marcel
 * 
 */
public interface IMachine {
	/**
	 * Suffixe du fichier cible (.tam par exemple)
	 * 
	 * @return
	 */
	String getSuffix();

	/**
	 * Ecrit le code dans un fichier à partir du nom du fichier source et du
	 * suffixe
	 * 
	 * @param fileName
	 * @param code
	 * @throws MCSException
	 */

	void writeCode(String fileName, String code) throws MCSException;

	/**********************************************************
	 * Generation function
	 **********************************************************/

	/**
	 * Generate the code for loading a variable into a register, allowing for (e.g.) doing some operations.
	 * Note: register management is done by the machine.
	 * @param info info of the variable to load
	 * @return the generated code
	 */
	String generateLoadVariable(VariableInfo info);

	/**
	 * Generate the code for loading a constant itneger into a register.
	 * Note: register management is done by the machine.
	 * @param value value of the constant to load
	 * @return the generated code
	 */
	String generateLoadInteger(int value);

}
