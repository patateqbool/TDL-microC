package mcs.gc;

import mcs.compiler.MCSException;
import mcs.symtab.*;
import mcs.obj.*;
import mcs.compiler.MCSException;
import java.util.List;

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
    NOP(0), // Syntaxic stuff
    // Arithmetics
    ADD(1), // Addition
    SUB(2), // Substraction
    MUL(3), // Multiplication
    DIV(4), // Division
    NEG(8), // Arithmetic inversion (that is : minus)
    AND(5), // And bitwise
    OR(6), // Or bitwise
    MOD(7), // Modulo operator
    PLS(10), // Unary plus
    NOT(9), // Not operator
    // Relationnal
    EQ(11),  // Equal
    NEQ(12), // Non equal
    LT(13),  // Lesser
    LEQ(14), // Strict Inferior
    GT(15),  // Superior
    GEQ(16), // Strict Superior
    RAND(17), // And 
    ROR(18),  // Or
    RNOT(19); // Not

    private final int value;
    private Operator(int value) {
      this.value = value;
    }
    public int value() {
      return this.value;
    }
    public boolean isArithmetic() {
      return this.value > 0 && this.value < 11;
    }
  }

  public static final Operator[] IntToOperator = {Operator.NOP,Operator.ADD,Operator.SUB,Operator.MUL,Operator.DIV,Operator.AND,Operator.OR,Operator.MOD,Operator.NEG,Operator.NOT,Operator.PLS,Operator.EQ,Operator.NEQ,Operator.LT,Operator.LEQ,Operator.GT,Operator.GEQ,Operator.RAND,Operator.ROR,Operator.RNOT};

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

  /////////////////////// MEMORY INSTRUCTIONS ///////////////////////

  ///////////// LOAD /////////////

  /**
   * Generate the code for loading a variable into a register
   * @param info info of the variable to load
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  String generateLoadValue(VariableInfo info, Register rout) throws MCSException;

  /**
   * Generate the code for loading a variable into a register, with an optionnal field name (for structs)
   * @param info info of the variable to load
   * @param disp (integer) displacement to consider (for structs)
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  String generateLoadValue(VariableInfo info, int disp, Register rout) throws MCSException;

  /**
   * Generate the code for loading a variable into a register, with an optionnal displacement register (for arrays)
   * @param info info of the variable to load
   * @param rdisp (register) displacement to consider
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  String generateLoadValue(VariableInfo info, Register rdisp, Register rout) throws MCSException;

  /**
   * Generate the code for loading a value from the stack to a register
   * @param disp displacement of the variable to load
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  String generateLoadFromStack(int disp, Register rout) throws MCSException;

  /**
   * Generate the code for loading a constant itneger into a register.
   * @param info info of the constant to load
   * @param rout register where the value is put, for later referencing
   * @return the generated code
   */
  String generateLoadConstant(ConstantInfo info, Register rout) throws MCSException;

  /**
   * Generate the code for loading a variable from the heap into a register
   * @param raddr register containing the address
   * @param disp optionnal displacement (integer) for struct
   * @param rout register where the value is put
   * @return the generated code
   */
  String generateLoadFromHeap(Register raddr, int disp, Register rout) throws MCSException;

  /**
   * Generate the code for loading a variable from the heap into a register, with an optionnal register displacement
   * @param raddr register containing the address
   * @param rdisp (register) optionnal displacement in a register (for arrays)
   * @param rout register where the value is put
   * @return the generated code
   */
  String generateLoadFromHeap(Register raddr, Register rdisp, Register rout) throws MCSException;

  ///////////// STORE ////////////

  /**
   * Generate the code for 'updating' the value of a variable
   * @param info info of the variable to store
   * @param rin value to put in the variabe
   * @return the generated code
   */
  String generateStoreVariable(VariableInfo vinfo, Register rin) throws MCSException;

  /**
   * Generate the code for 'updating' the value of a variable
   * @param info info of the variable to store
   * @param disp (integer) displacement to consider
   * @param rin value to put in the variabe
   * @return the generated code
   */
  String generateStoreVariable(VariableInfo vinfo, int disp, Register rin) throws MCSException;

  /**
   * Generate the code for 'updating' the value of a variable
   * @param info info of the variable to store
   * @param rdisp (register) displacement to consider
   * @param rin value to put in the variabe
   * @return the generated code
   */
  String generateStoreVariable(VariableInfo vinfo, Register rdisp, Register rin) throws MCSException;

  /**
   * Generate the code for storing a variable into the heap
   * @param raddr register containing the address
   * @param disp (integer) optionnal displacement for structs
   * @param rin register containing the value to be stored
   * @return the generated code
   */
  String generateStoreInHeap(Register raddr, int disp, Register rin) throws MCSException;

  /**
   * Generate the code for storing a variable into the heap, with optionnal register displacement
   * @param raddr register containing the address
   * @param rdisp (register) optionnal displacement for arrays
   * @param rin register containing the value to be stored
   * @return the generated code
   */
  String generateStoreInHeap(Register raddr, Register rdisp, Register rin) throws MCSException;

  /////// MEMORY MANAGEMENT ///////
  /**
   * Generate the code for allocating a variable in the stack
   * @param type type to allocate
   * @return the generated code
   */
  String generateAllocateInStack(Type type) throws MCSException;

  /**
   * Generate the code for allocating a block in the heap
   * @param type type to allocate
   * @param raddr (out) register containing the address of the block
   * @param rsize register containing the size of the block (array only)
   * @return the generated code
   */
  String generateAllocate(Type type, Register addr, Register rsize) throws MCSException;

  /**
   * Generate the code for flushing the stack top variable
   * @param type type of the variable
   * @return the generated code
   */
  String generateFlushVariable(Type type) throws MCSException;

  /**
   * Generate the code for flushing every variable of a symbol table
   * Note: used when going out from a block
   * @param symtab the symbol table
   * @return the generated code
   */
  String generateFlush(SymbolTable symtab) throws MCSException;

  /////////////////////// FUNCTION MANAGEMENT ///////////////////////

  /**
   * Generate the code for the beginning of declaring a function
   * @param info the info of the function
   * @param code code generated for the content of the function
   * @return the generated code
   */
  String generateFunctionDeclaration(FunctionInfo info, String code) throws MCSException;

  /**
   * Generate the code for the 'return' keyword
   * @param info the info of the function
   * @param rval the register containing the value to be returned
   * @return the generated code
   */
  String generateFunctionReturn(FunctionInfo info, Register rval) throws MCSException;

  /**
   * Generate the code for pushing an argument
   * @param reg register in which the argument is stored
   * @return the generated code
   */
  String generateFunctionPushArgument(Register reg) throws MCSException;

  /**
   * Generate the code for the call to a function
   * @param info info of the function
   * @return the generated code
   */
  String generateFunctionCall(FunctionInfo info) throws MCSException;

  /**
   * Generate the code for the declaration of a method
   * @param info info of the method
   * @param code code of the block
   * @return the generated code
   */
  String generateMethodDeclaration(MethodInfo info, String code) throws MCSException;

  /**
   * Generate the code for the call of a method
   * @param info info of the method
   * @param robj register containing the address of the object on which we call the method
   * @return the generated code
   */
  String generateMethodCall(MethodInfo info, Register robj) throws MCSException;

  /**
   * Generate the code for declaring a constructor
   * @param info info of the constructor (the register attribute will be set) 
   * @param code code of the block
   * @return the generated code
   */
  String generateConstructorDeclaration(ConstructorInfo info, String code) throws MCSException;

  /**
   * Generate the code for calling a constructor
   * @param info info of the constructor to call
	 * @param base determines if this call is made to the super constructor (in another constructor)
   * @return the generated code
   */
  String generateConstructorCall(ConstructorInfo info, boolean base) throws MCSException;

  ////////////////////////////// MISC ///////////////////////////////
	/**
	 * Generate an instruction with various number of parameters
	 * @param inst instruction
	 * @param p1,p2,... parameters
	 * @param enclose enclose the last parameters in brackets (for addressing)
	 * @return the generated code
	 */
String generateInstruction(String inst, List<Object> params) throws MCSException;
	String generateInstruction(String inst, boolean enclose, Object p1, Object p2, Object p3) throws MCSException;
String generateInstruction(String inst, Object p1, Object p2, Object p3) throws MCSException;
	String generateInstruction(String inst, boolean enclose, Object p1, Object p2) throws MCSException;
							String generateInstruction(String inst, Object p1, Object p2) throws MCSException;
	String generateInstruction(String inst, Object p1) throws MCSException;

	/**
	 * Generate a label
	 * @param label
	 * @return the generated code
	 */
	String generateLabel(String label) throws MCSException;
	
	/**
	 * Generate a direct constant for use in instructions
	 * @param val value of the constant
	 * @return the generated code
	 */
	String generateDirect(int value) throws MCSException;

	/**
	 * Generate a register
	 * @param reg the register
	 * @return the generated code
	 */
	String generateRegister(Register reg) throws MCSException;
	
  /**
   * Generate the code for making an address from a list of displacement pair; the 
   * base register is stack base.
   * The principle is to get into a register the addres of, let us say, the field
   * of a structure. The thing is that we can chain struct fields calls, thus
   * making the address calculation quite complex.
   * To achieve this calculation, we first create a displacement list, storing
   * displacement of each fields one by one (as welle as a boolean indicating
   * if should dereference the field (arrow) or not (point).
   * Then, we can create the addres by a succession of LDR
   * @param dlist displacement list
   * @param raddr (out) register that will contain the address
   * @return the generated code
   */
  String generateMakeAddress(DisplacementList dlist, Register raddr) throws MCSException;

  /**
   * Generate the code for making an address from a list of displacement pair,
   * using the specified register as base register.
   * @param dlist displacement list
   * @param rbaseaddr base address register
   * @param raddr (out) register that will contain the address
   * @return the generated code
   */
  String generateMakeAddress(DisplacementList dlist, Register rbaseaddr, Register raddr) throws MCSException;

  /**
   * Generate the code for an if-then-else structure
   * @param rcond register containing the result of the condition
   * @param cif code for the if branch
   * @param celse code for the else branch
   * @return the generated code
   */
  String generateIfThenElse(Register rcond, String cif, String celse) throws MCSException;

  /**
   * Generate a comment
   * @param com comment to print
   * @param ident indenting to apply
   * @return the generated code
   */
  String generateComment(String comm, String indent) throws MCSException;

  /**
   * Generate a multiline comment (utility)
   * @param com list of '\n' separated comments
   * @param indent indent to apply
   * @return the generated code
   */
  String generateMultiComments(String com, String indent) throws MCSException;

	/**
	 * Return the function return register
	 * @return the register
	 */
	Register functionReturn() throws MCSException;
	
  //////////////////////////// CALCULUS /////////////////////////////

  /**
   * Generate an arithmetic binary operation
   * @param r1 first register
   * @param r2 second register
   * @param rout output register
   * @return the generated code
   */
  String generateOperation(int op, Register r1, Register r2, Register rout) throws MCSException;

  /**
   * Generate an arithmetic unary operation
   * @param rin source register
   * @param rout destination register
   * @return the generated code
   */
  String generateOperation(int op, Register rin, Register rout) throws MCSException;
}
