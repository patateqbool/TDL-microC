/**
 * ARMEngine -- define the generator for arm
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

import java.util.List;
import java.util.ArrayList;
import mcs.symtab.*;
import mcs.compiler.MCSException;

public class ARMEngine extends AbstractMachine {
	/**
	 * Number of registers.
	 * ARM has in fact 15 "multi-purpose registers", but three of them is used for stack, link and program counter.
	 * Plus, we decided to use R12 as the heap top, as it is not implemented directly into ARM
	 */
	static private final int NUM_REGISTER = 12;
	static private final String Prefix = "\t\t";
	private List<Register> registers;
	private Register sp, lr, pc, ht;
	private int heapbase = 0;

	/**
	 * Constructor
	 */
	public ARMEngine() {
		registers = new ArrayList<Register>();

		for (int i = 0; i < NUM_REGISTER; i++) {
			registers.add(new Register("R", i));
		}
		sp = new Register("SP", -1);
		lr = new Register("LR", -1);
		pc = new Register("PC", -1);
		ht = new Register("R", 12);
	}

	/**
	 * Suffix for target file : asm
	 */
	public String getSuffix() {
		return ".asm";
	}

	public void writeCode(String fileName, String code) throws MCSException {
		String preliminary = 
			"HT	EQU R12\n" +
			"HB EQU " + heapbase*4 + "\n" +
      "\n" +
      "MOV HT, HB\n" +
			"\n";

		super.writeCode(fileName, preliminary + code);
	}

	/**********************************************************
	 * Generation function
	 **********************************************************/

	/// Load an store
	/**
	 * Generate the code for loading a variable into a register, allowing for (e.g.) doing some operations.
	 * Note: register management is done by the machine.
	 * @param info info of the variable to load
	 * @param output register where the value is put, for later referencing
	 * @return the generated code
	 */
	public String generateLoadVariable(VariableInfo info, Register output) {
		// Retrieve a valid register for putting the result
		Register reg = getNextUnusedRegister();

		// Generate code
		String code = ARMEngine.Prefix;
		
		Type t = info.type();
		
		if (t instanceof IntegerType || t instanceof PointerType) {
			code += "LDR\t" + reg + ", [SP, " + info.displacement() + "]\n";
		} else if (t instanceof CharacterType) {
			code += "LDRSB\t" + reg + ", [SP, " + info.displacement() + "]\n";
		} else if (t instanceof StructType) {
			// we shouldn't load a whole struct in register, isn'it ?
		}

		// Manage register
		reg.setStatus(Register.Status.Loaded);
		output.copy(reg);
		info.assignRegister(reg);

		// Modify heap base (added 1 instruction)
		heapbase++;

		// End
		return code;
	}

	/**
	 * Generate the code for loading a constant itneger into a register.
	 * Note: register management is done by the machine.
	 * @param value value of the constant to load
	 * @param output register where the value is put, for later referencing
	 * @return the generated code
	 */
	public String generateLoadInteger(int value, Register output) {
		/*
		 * The main problem is that the MOV instruction (which put a value into a register)
		 * can only work on halfwords (16-bit data).
		 * The trick is to use MOV and MOVT; the latter set the 16 MSB of the register to
		 * the value specified.
		 * Of course, we do that only if needed.
		 */
		// Retrieve a valid register for putting the result
		Register reg = getNextUnusedRegister();

		// Generate code
		String code = ARMEngine.Prefix + "MOV\t" + reg + ", #0x" + Integer.toHexString(value & 0x0000FFFF) + "\n";

		if (value > 65535) {
			// If needed, we must set the top part of the value
			code = code + ARMEngine.Prefix + "MOVT\t" + reg + ", #0x" + Integer.toHexString(value >> 16) + "\n";
			heapbase++; // > this generate an additionnal instruction
		}

		// Manage registers
		reg.setStatus(Register.Status.Loaded);
		output.copy(reg);

		// Modify heap base
		heapbase++;

		// End
		return code;
	}

  /**
   * Generate the code for loading data directly from memory
   * @param raddr register containing the address
   * @param size size of the data to retrieve
   * @param rout register that will contain the data
   * @return the generated code
   */
  public String generateLoadFromMemory(Register raddr, int size, Register rout){
    String code = ARMEngine.Prefix + "LDR";
    Register reg = getNextUnusedRegister();

    if (size == 1)
      code += "SB"; // We load a signed byte
    /*else
      //probably an error
      */

    // Generate code
    code += "\t" + reg + ", [" + raddr + "]\n";

    // Update register status
    reg.setStatus(Register.Status.Loaded);
    raddr.setStatus(Register.Status.Used);

    // Copy register in output
    rout.copy(reg);

    // Modify heap base
    heapbase++;

    // End
    return code;
  }
	
  /**
	 * Generate the code for storing a value into memory
	 * @param info variable info; it MUST have a register assigned to work
	 * @return the generated code
	 */
	public String generateStoreVariable(VariableInfo info) {
		String code = ARMEngine.Prefix;
		Type t = info.type();

		if (t instanceof IntegerType || t instanceof PointerType) {
			code = code + "STR\t" + info.register() + ", [SP, " + info.displacement() + "]\n";
      heapbase++;
		} else if (t instanceof CharacterType) {
			code = code + "STRSB\t" + info.register() + ", [SP, " + info.displacement() + "]\n";
      heapbase++;
		} else if (t instanceof StructType) {
			// TODO: struct affectation is special
			// We generate a load variable for every field of the struct
			StructType st = (StructType)t;
			Register reg = new Register();
			VariableInfo vinfo;
			for (String f : st.fields()) {
				vinfo = st.getInfo(f, info.displacement());
				code += generateLoadVariable(vinfo, reg);
				code += generateStoreVariable(vinfo);
			}
		}

		info.freeRegister();
		return code;
	}
	
	/**
	 * Generate the code for flushing every variable of a symbol table
	 * Note: used when going out from a block
	 * @param symtab the symbol table
	 * @return the generated code
	 */
	public String generateFlush(SymbolTable symtab) {
		Register reg = new Register();

		String code = generateLoadInteger(0, reg);
		
		for (String key : symtab.symbols()) {
			VariableInfo vi = (VariableInfo)symtab.lookup(key, true);
			code += ARMEngine.Prefix + "LDR\t" + reg + ", [SP, " + vi.displacement() + "]\n";
      heapbase++;
		}

		reg.setStatus(Register.Status.Used);
		return code;
	}

  /**
   * Generate the code for memory allocation in the heap
   * @param size size to allocate
   * @param rout register in which the address will be
   * @return the generated code
   */
  public String generateMemoryAllocation(Register rsize, Register rout) {
    Register reg = getNextUnusedRegister();
    String code =
      ARMEngine.Prefix + "MOV\t" + reg + ", HT\n" +
      ARMEngine.Prefix + "ADD\tHT, HT, " + rsize + "\n" +
      /*
       * This part is a realignment code;
       * In the case where the size is not a multiple of 4, we
       * could have an alignement problem. The solution is to
       * realign the heap top by making it dividable by 4. For that,
       * we first get rid of the 2 LSB so that they equals 0 (hence creating
       * a value that is a multiple of 4). Then, we add 4 beacause we want
       * to majorate the value (not minorate, which the first step does).
       */
      ARMEngine.Prefix + "LSR\tHT, HT, #2\n" +
      ARMEngine.Prefix + "LSL\tHT, HT, #2\n" +
      ARMEngine.Prefix + "ADD\tHT, HT, #4\n";
    rsize.setStatus(Register.Status.Used);
    reg.setStatus(Register.Status.Loaded);
    rout.copy(reg);
    return code;
  }
	
  /// Calculus
	/**
	 * Generate an arithmetic binary operation
	 * @param r1 first register
	 * @param r2 second register
	 * @param rout output register
	 * @return the generated code
	 */
	public String generateOperation(Operator op, Register r1, Register r2, Register rout) {
		// TODO: wrong operation type
		// The last part of the code never changes : xxx Rx, R<1>, R<2>
		String code = ", " + r1 + ", " + r2 + "\n";

		// Find the operation code
		String opcode = "";
		switch (op) {
			case ADD:
				opcode = "ADD";
				break;
			case SUB:
				opcode = "SUB";
				break;
			case MUL:
				opcode = "MUL";
				break;
			case DIV:
				opcode = "SDIV";
				break;
		}

		// Source register are no longer used
		r1.setStatus(Register.Status.Used);
		r2.setStatus(Register.Status.Used);
	
		// Get the next register
		Register r = getNextUnusedRegister();

		// Generate code
		code = ARMEngine.Prefix + opcode + "\t" + r + code;

		// Manage register
		r.setStatus(Register.Status.Loaded);
		rout.copy(r);

		// Modify heapbase
		heapbase++;

		// End
		return code;
	}

	/**
	 * Generate an arithmetic unary operation
	 * @param rin source register
	 * @param rout destination register
	 * @return the generated code
	 */
	public String generateOperation(Operator op, Register rin, Register rout) {
		// TODO: wrong operation type
		String code = ", " + rin + ", #0\n";

		// Find the operation code
		String opcode = "";
		switch (op) {
			case NEG:
				opcode = "RSB";
				break;
		}

		// Source register are no longer used
		rin.setStatus(Register.Status.Used);

		// Retrieve next register
		Register r = getNextUnusedRegister();

		// Generate code
		code = ARMEngine.Prefix + opcode + "\t" + r + code;

		// Manage register
		r.setStatus(Register.Status.Loaded);
		rout.copy(r);

		// Modify heapbase
		heapbase++;

		// End
		return code;
	}

	/**************************************************/
	private Register getNextUnusedRegister() {
		// TODO: register use policy
		for (int i = 0; i < registers.size(); i++) {
			if (registers.get(i).status() == Register.Status.Empty)
				return registers.get(i);
		}

		for (int i = 0; i < registers.size(); i++) {
			if (registers.get(i).status() == Register.Status.Used)
				return registers.get(i);
		}

		return null;
	}

	public String logRegisters() {
		String txt = "";
		for (Register r : this.registers) {
			txt += r + " => ";
			switch (r.status()) {
				case Empty:
					txt += "E";
					break;
				case Loaded:
					txt += "L";
					break;
				case Used:
					txt += "U";
					break;
			}
			txt += "\n";
		}
		return txt;
	}
}


