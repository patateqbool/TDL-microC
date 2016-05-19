/**
 * ARMEngine -- define the generator for arm
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

import java.util.List;
import java.util.ArrayList;
import java.util.ListIterator;
import mcs.symtab.*;
import mcs.compiler.MCSException;

public class ARMEngine extends AbstractMachine {
	/**
	 * Number of registers.
	 * ARM has in fact 15 "multi-purpose registers", but three of them is used for stack, link and program counter.
	 * Plus, we decided to use R12 as the heap top, as it is not implemented directly into ARM
	 */
	static private final int NUM_REGISTER = 11;
	static private final String Prefix = "\t\t";
	private List<Register> registers;
	private Register sp, lr, pc, ht, sb;
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
    sb = new Register("R", 11);
	}

	/**
	 * Suffix for target file : asm
	 */
	public String getSuffix() {
		return ".asm";
	}

	public void writeCode(String fileName, String code) throws MCSException {
		String preliminary = 
			"HT	EQU " + ht + "\n" +
      "SB EQU " + sb + "\n" +
			"HB EQU " + heapbase*4 + "\n" +
      "\n" +
      ARMEngine.Prefix + "MOV\tHT, HB\n" +
      ARMEngine.Prefix + "MOV\tSB, ST\n" +
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
			code += "LDR\t" + reg + ", [SB, " + (-info.displacement()) + "]\n";
		} else if (t instanceof CharacterType) {
			code += "LDRSB\t" + reg + ", [SB, " + (-info.displacement()) + "]\n";
		} else if (t instanceof StructType) {
      // Struct are stored as pointers
      code += "LDR\t" + reg + ", [SB, " + (-info.displacement()) + "]\n";
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
	 * @param info info of the constant to load
	 * @param output register where the value is put, for later referencing
	 * @return the generated code
	 */
	public String generateLoadConstant(ConstantInfo info, Register output) {
    Type t = info.type();
    String code = "";
    Register reg = getNextUnusedRegister();

    if (t instanceof IntegerType || t instanceof PointerType || t instanceof StructType) {
      Integer value = (Integer)info.value();
		   /*
		    * The main problem is that the MOV instruction (which put a value into a register)
		    * can only work on halfwords (16-bit data).
		    * The trick is to use MOV and MOVT; the latter set the 16 MSB of the register to
		    * the value specified.
		    * Of course, we do that only if needed.
		    */
		  // Generate code
		  code = ARMEngine.Prefix + "MOV\t" + reg + ", #0x" + Integer.toHexString(value & 0x0000FFFF) + "\n";

		  if (value > 65535) {
			  // If needed, we must set the top part of the value
			  code = code + ARMEngine.Prefix + "MOVT\t" + reg + ", #0x" + Integer.toHexString(value >> 16) + "\n";
			  heapbase++; // > this generate an additionnal instruction
		  }
    } else {
      // dafuq ?
    }

		// Manage registers
    info.assignRegister(reg);
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
		String code = "";
		Type t = info.type();

    if (info.register() == null) {
      info.assignRegister(getNextUnusedRegister());
    }

    // The only precondition for the code below is to have an initialized
    // register object

		/*if (t instanceof IntegerType || t instanceof PointerType) {
			code = code + "STR\t" + info.register() + ", [SP, " + info.displacement() + "]\n";
      heapbase++;
		} else if (t instanceof CharacterType) {
			code = code + "STRSB\t" + info.register() + ", [SP, " + info.displacement() + "]\n";
      heapbase++;
		}*/ 
    if (t instanceof StructType) {
			// TODO: struct affectation is special
			// We generate a load variable for every field of the struct
			StructType st = (StructType)t;
			Register reg = new Register();
			VariableInfo vinfo;
			for (String f : st.fields()) {
				vinfo = st.getInfo(f, info.displacement());
        
			}
      code +=
        ARMEngine.Prefix + "MOV\t" + info.register() + ", " + ht + "\n";
		}
    
    code += ARMEngine.Prefix + "PUSH\t" + info.register() + "\n";

		info.freeRegister();
		return code;
	}

  /**
   * Generate the code for allocating a block in the heap
   * @param type type to allocate
   * @param raddr register containing the address of the block
   * @param rsize register containing the size of the block (array only)
   * @return the generated code
   */
  public String generateAllocate(Type type, Register raddr, Register rsize) {
    Register reg = getNextUnusedRegister();

    String code = ARMEngine.Prefix + "MOV\t" + reg + ", " + ht + " ; Store current address\n";
    
    if (type instanceof StructType) {
      StructType ts = (StructType)type;
      Register r = new Register();
      for (Type t : ts.fieldsTypes()) {
        code += generateAllocate(t, r, null);
      }
    } else if (type instanceof ArrayType) {
      ArrayType t = (ArrayType)type;
      Register rs = getNextUnusedRegister();
      code +=
        ARMEngine.Prefix + "UMUL\t" + rs + ", " + rsize + ", #" + t.getType().size() + "\n" +
        ARMEngine.Prefix + "ST\t" + rs + ", [" + ht + "]\n" +
        ARMEngine.Prefix + "ADD\t" + ht + ", " + ht + ", #4\n" + 
        ARMEngine.Prefix + "ADD\t" + ht + ", " + ht + ", " + rs + "\n";
      rsize.setStatus(Register.Status.Used);
    } else {
      Register rs = new Register();
      code +=
        generateLoadConstant(new ConstantInfo(new IntegerType(), type.size()), rs);
      code +=
        ARMEngine.Prefix + "ADD\t" + ht + ", " + ht + ", " + rs + "\n";
    }

    reg.setStatus(Register.Status.Loaded);
    raddr.copy(reg);

    return code;
  }

  /**
   * Generate the code for storing a value into the heap
   * @param rdata register containing the data
   * @param raddr register containing the address in which to put the data
   * @return the generated code
   */
  public String generateStoreHeap(Register rdata, Register raddr) {
    String code = "";

    Type t = info.type();

    if (t instanceof StructType) {
    } else {
      code +=
        ARMEngine.Prefix + "STR\t" + rdata + ", [" + raddr + "]\n";
    }

    return code;
  }
  
  /**
   * Generate the code for flushing the stack top variable
   * @param info the info of the variable
   * @return the generated code
   */
  public String generateFlushVariable(VariableInfo info) {
    Register reg = getNextUnusedRegister();
    String code = ARMEngine.Prefix;

    if (info.type() instanceof StructType) {
      StructType ts = (StructType)info.type();
      for (Type tsf : ts.fieldsTypes()) {
        generateFlushVariable(new VariableInfo(tsf, 0));
      }
    } else {
      code += "POP\t" + reg + "\n";
      heapbase++;
    }

    return code;
  }
	
	/**
	 * Generate the code for flushing every variable of a symbol table
	 * Note: used when going out from a block
	 * @param symtab the symbol table
	 * @return the generated code
	 */
	public String generateFlush(SymbolTable symtab) {
		Register reg = getNextUnusedRegister();
    String code = "";
    ListIterator iter = symtab.symbols().listIterator(symtab.symbols().size());

		while (iter.hasPrevious()) {
			VariableInfo vi = symtab.lookup(iter.previous(), true);
			//code += ARMEngine.Prefix + "LDR\t" + reg + ", [SP, " + vi.displacement() + "]\n";
      code += generateFlushVariable(vi);
		}

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

  /// Functions related
  
  /**
   * Generate the code for the beginning of declaring a function
   * @param info the info of the function
   * @param code code generated for the content of the function
   * @return the generated code
   */
  public String generateFunctionDeclaration(FunctionInfo info, String blockcode) {
    String code =
      info.label() + ":\n" +
      ARMEngine.Prefix + "; Push link register, stack base and stack pointer\n" +
      ARMEngine.Prefix + "PUSH\t" + lr + "\n" +
      ARMEngine.Prefix + "PUSH\t" + sb + "\n" + 
      ARMEngine.Prefix + "PUSH\t" + sp + "\n" +
      blockcode;

    code +=
      ARMEngine.Prefix + "; Default return. It is not wise to reach this point\n" +
      generateFunctionReturn(info, new ConstantInfo());

    return code;
  }


  /**
   * Generate the code for the 'return' keyword
   * @param info the info of the fuunction
   * @param vinfo info of the value to return (register must be set)
   * @return the generated code
   */
  public String generateFunctionReturn(FunctionInfo info, VariableInfo vinfo) {
    String code =
      ARMEngine.Prefix + "; Pop registers \n" +
      ARMEngine.Prefix + "POP\t" + sp + "\n" +
      ARMEngine.Prefix + "POP\t" + sb + "\n" +
      ARMEngine.Prefix + "POP\t" + lr + "\n";

    code +=
      ARMEngine.Prefix + "; Pop arguments \n";

    // As we push arguments in one order, we need to pop them in the
    // other
    ListIterator iter = info.parameters().listIterator(info.parameters().size());
    while (iter.hasPrevious()) {
      code += generateFlushVariable(iter.previous());
    }

    if (!(info.returnType() instanceof VoidType))
      code += generateStoreVariable(vinfo);

    code +=
      ARMEngine.Prefix + "BA\t" + lr + "\n\n";
  }

  /**
   * Generate the code for pushing an argument
   * @param reg register in which the argument is stored
   * @return the generated code
   */
  public String generateFunctionPushArgument(Register reg) {
    String code =
      ARMEngine.Prefix + "PUSH\t" + reg + "\n";
    reg.setStatus(Register.Status.Used);
    return code;
  }


  /**
   * Generate the code for the call to a function
   * @param info info of the function
   * @return the generated code
   */
  public String generateFunctionCall(FunctionInfo info) {
    String code =
      ARMEngine.Prefix + "BL\t" + info.label() + "\n";
    // TODO: result of the function is on the stack
    // (addr = SP)
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

                 String code = "";
 
                 // Get the next register
                 Register r = getNextUnusedRegister();
 
                 switch (op) {
                         case ADD:
                                code += ARMEngine.Prefix + "ADD\t" + r + ", " + r1 + ", " + r2 + "\n";
				heapbase++;
                                break;
                         case SUB:
                                code += ARMEngine.Prefix + "SUB\t" + r + ", " + r1 + ", " + r2 + "\n";
				heapbase++;
                                break;
                         case MUL:
                                code += ARMEngine.Prefix + "MUL\t" + r + ", " + r1 + ", " + r2 + "\n";
				heapbase++;
                                break;
                         case DIV:
                                code += ARMEngine.Prefix + "SIV\t" + r + ", " + r1 + ", " + r2 + "\n";
				heapbase++;
                                break;
                         case AND:
                                code += ARMEngine.Prefix + "AND\t" + r + ", " + r1 + ", " + r2 + "\n";
				heapbase++;
                                break;
                         case OR:
                                code += ARMEngine.Prefix + "ORR\t" + r + ", " + r1 + ", " + r2 + "\n";
				heapbase++;
                                break;
                         case MOD:
                                // Do this : q = a/b, b*q, a-bq = r
                                code += ARMEngine.Prefix + "SDIV\t" + r + ", " + r1 + ", " + r2 + "\n";
                                code += ARMEngine.Prefix + "MUL\t" + r + ", " + r2 + ", " + r + "\n";
                                code += ARMEngine.Prefix + "SUB\t" + r + ", " + r1 + ", " + r + "\n";
				heapbase += 3;
                                break;
                 }

		// Source register are no longer used
		r1.setStatus(Register.Status.Used);
		r2.setStatus(Register.Status.Used);
	

		// Manage register
		r.setStatus(Register.Status.Loaded);
		rout.copy(r);

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
			case NOT:
				opcode = "MVN";
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

	public String generateOperation(RelationalOperator op, Register r1, Register r2, Register rout) {
		Register r = getNextUnusedRegister();
		String code = ARMEngine.Prefix + "MOV\t" + r + "#0x0"; 
		switch (op) {
			/* AND is a specification of EQ */
			case EQ:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case NEQ:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case LT:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case LEQ:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case GT:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case GEQ:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case AND:
				code += ARMPrefix.Engine + "ANDS\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case OR:
				code += ARMPrefix.Engine + "ORRS\t" + r1 + ", " + r2 + "\n";
				code += ARMPrefix.Engine + "MOVEQ\t" + r + "1" + "\n";
				break;
		}
		// Information about register
		r1.setStatus(Register.Status.Used);
		r2.setStatus(Register.Status.Used);
		
	
		// Manage register
		r.setStatus(Register.Status.Loaded);
		rout.copy(r);
		
		heapbase += 3;
		
		return code;
	}

	/**
	 * Generate an relationnal binary operation
	 * @param rin input register 
	 * @param rout output register
	 * @return the generated code
	 */
	public String generateOperation(RelationnalOperator op, Register rin, Register rout) {
		String code = ARMPrefix.Engine + "MOV\t" + r + "#0x0";
		switch (op) {
			case NOT:
				code += ARMPrefix.Engine + "CMP\t" + r1 + ", 0\n";
				code += ARMPrefix.Engine + "MOVNE\t" + r + "1" + "\n";
				break;
		}
		
		// Information about register
		r1.setStatus(Register.Status.Used);
		
		// Manage register
		r.setStatus(Register.Status.Loaded);
		rout.copy(r);
		
		heapbase += 3;
		
		return code;
 	}
	

	/**************************************************/
  /**
   * Get the next unused register in the register database
   * @return the register
   */
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


