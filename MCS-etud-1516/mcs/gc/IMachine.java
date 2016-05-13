package mcs.gc;

import mcs.compiler.MCSException;
import mcs.symtab.VariableInfo;

/**
 * Cette interface décrit une machine cible. A compléter, selon votre modèle
 * 
 * @author marcel
 * 
 */
public interface IMachine {
	/**
	 * Enumeration that define possible arithmetic operations
	 */
	public enum Operator {
		ADD, // Addition
		SUB, // Substraction
		MUL, // Multiplication
		DIV, // Division
		NEG  // Arithmetic inversion (that is : minus)
	}

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

	/// Load and store
	/**
	 * Generate the code for loading a variable into a register, allowing for (e.g.) doing some operations.
	 * Note: register management is done by the machine.
	 * @param info info of the variable to load
	 * @param output register where the value is put, for later referencing
	 * @return the generated code
	 */
	String generateLoadVariable(VariableInfo info, Register output);

	/**
	 * Generate the code for loading a constant itneger into a register.
	 * Note: register management is done by the machine.
	 * @param value value of the constant to load
	 * @param output register where the value is put, for later referencing
	 * @return the generated code
	 */
	String generateLoadInteger(int value, Register output);

	/**
	 * Generate the code for storing a value into memory
	 * @param info variable info
	 * @return the generated code
	 */
	String generateStoreValue(VariableInfo info);

	/// Calculus
	/**
	 * Generate an arithmetic binary operation
	 * @param r1 first register
	 * @param r2 second register
	 * @param rout output register
	 * @return the generated code
	 */
	String generateOperation(Operator op, Register r1, Register r2, Register rout);

	/**
	 * Generate an arithmetic unary operation
	 * @param rin source register
	 * @param rout destination register
	 * @return the generated code
	 */
	String generateOperation(Operator op, Register rin, Register rout);


}
