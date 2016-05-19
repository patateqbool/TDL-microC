package mcs.gc;

import mcs.compiler.MCSException;
import mcs.symtab.VariableInfo;
import mcs.symtab.FunctionInfo;
import mcs.symtab.SymbolTable;
import mcs.symtab.ConstantInfo;
import mcs.symtab.Type;

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
		NEG, // Arithmetic inversion (that is : minus)
    		NOP,  // Syntaxic stuff only
		AND, // And bitwise
		OR, // Or bitwise
    		MOD, // Modulo operator
    		PLS, // Unary plus
    		NOT // Not operator
	}
	
	public enum RelationnalOperator {
		EQ,  // Equal
		NEQ, // Non equal
		LT,  // Lesser
		LEQ, // Strict Inferior
		GT,  // Superior
		GEQ, // Strict Superior
		AND, // And 
		OR,  // Or
		NOT, // Not
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
	 * @param info info of the constant to load
	 * @param output register where the value is put, for later referencing
	 * @return the generated code
	 */
	String generateLoadConstant(ConstantInfo info, Register output);

  /**
   * Generate the code for loading data directly from memory
   * @param raddr register containing the address
   * @param size size of the data to retrieve
   * @param rout register that will contain the data
   * @return the generated code
   */
  String generateLoadFromMemory(Register raddr, int size, Register rout);

	/**
	 * Generate the code for storing a value into memory (the stack)
	 * @param info variable info
	 * @return the generated code
	 */
	String generateStoreVariable(VariableInfo info);

  /**
   * Generate the code for allocating a block in the heap
   * @param type type to allocate
   * @param raddr register containing the address of the block
   * @param rsize register containing the size of the block (array only)
   * @return the generated code
   */
  String generateAllocate(Type type, Register addr, Register rsize);

  /**
   * Generate the code for storing a value into the heap
   * @param rdata register containing the data
   * @param raddr register containing the address in which to put the data
   * @return the generated code
   */
  String generateStoreHeap(Register rdata, Register raddr);

  /**
   * Generate the code for flushing the stack top variable
   * @param info the info of the variable
   * @return the generated code
   */
  String generateFlushVariable(VariableInfo info);

	/**
	 * Generate the code for flushing every variable of a symbol table
	 * Note: used when going out from a block
	 * @param symtab the symbol table
	 * @return the generated code
	 */
	String generateFlush(SymbolTable symtab);

  /**
   * Generate the code for memory allocation in the heap
   * @param rsize register containing the size to allocate
   * @param rout register in which the address will be
   * @return the generated code
   */
  String generateMemoryAllocation(Register rsize, Register rout);

  /// Functions related
  
  /**
   * Generate the code for the beginning of declaring a function
   * @param info the info of the function
   * @param code code generated for the content of the function
   * @return the generated code
   */
  String generateFunctionDeclaration(FunctionInfo info, String code);

  /**
   * Generate the code for the 'return' keyword
   * @param info the info of the function
   * @param vinfo the info of the value to return
   * @return the generated code
   */
  String generateFunctionReturn(FunctionInfo info, VariableInfo vinfo);

  /**
   * Generate the code for pushing an argument
   * @param reg register in which the argument is stored
   * @return the generated code
   */
  String generateFunctionPushArgument(Register reg);

  /**
   * Generate the code for the call to a function
   * @param info info of the function
   * @return the generated code
   */
  String generateFunctionCall(FunctionInfo info);

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
