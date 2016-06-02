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
import mcs.compiler.*;
import mcs.obj.*;

public class ARMEngine extends AbstractMachine {
	/**
	 * Number of registers.
	 * ARM has in fact 15 "multi-purpose registers", but three of them is used for stack, link and program counter.
	 * Plus, we decided to use R12 as the heap top and R11 as the stack base, as they are not implemented directly into ARM.
   * Moreover, we use R10 as a register for storing the object's class id when we jump from methods to vtables.
	 */
	static private final int NUM_REGISTER = 10;
	static private final String Prefix = "\t\t";
	private List<Register> registers;
	private Register sp, lr, pc, ht, sb, oi;
	private int heapbase = 0;
  private int condition_nb = 0;

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
    oi = new Register("R", 10);
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
  /////////////////////// MEMORY INSTRUCTIONS ///////////////////////
  
  ///////////// LOAD /////////////
 
  /**
   * Generate the code for loading a variable into a register
   * @param info info of the variable to load
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  public String generateLoadValue(VariableInfo info, Register rout) throws MCSException  {
    String code = "";

    if (info instanceof ConstantInfo)
      code += generateLoadConstant((ConstantInfo)info, rout);
    else {
      code += generateLoadFromStack(info.displacement(), rout);
    }

    return code;
  }

  /**
   * Generate the code for loading a variable into a register, with an optionnal field name (for structs)
   * @param info info of the variable to load
   * @param disp (integer) displacement to consider (for structs)
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  public String generateLoadValue(VariableInfo info, int disp, Register rout) throws MCSException  {
    String code = "", addr = "";
    Register r = getNextUnusedRegister();
    Register raddr = new Register();

    if (info.type() instanceof CompositeType) {
      code += generateLoadFromStack(info.displacement(), raddr);

      if (disp < 65536) {
        addr = "[" + raddr + ", #" + disp + "]";
      } else {
        Register rdisp = new Register();
        code += generateLoadConstant(new ConstantInfo(new IntegerType(), disp), rdisp);
        addr = "[" + raddr + ", " + rdisp + "]";
        rdisp.setStatus(Register.Status.Used);
      }

      raddr.setStatus(Register.Status.Used);
    } else {
      // we shouldn't be using this function, don't we ?
      return "";
    }

    code +=
      ARMEngine.Prefix + "LDR\t" + r + ", " + addr + "\n";

    r.setStatus(Register.Status.Loaded);
    rout.copy(r);

    return code;
  }

  /**
   * Generate the code for loading a variable into a register, with an optionnal displacement register (for arrays)
   * @param info info of the variable to load
   * @param rdisp (register) displacement to consider
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  public String generateLoadValue(VariableInfo info, Register rdisp, Register rout) throws MCSException  {
    Register raddr = new Register();
    Register r = getNextUnusedRegister();
    String code = "", addr = "";
    Type t = info.type();

    if (t instanceof CompositeType) {
      code += generateLoadFromStack(info.displacement(), raddr);
      addr = "[" + raddr + ", " + rdisp + "]";
    } else {
      // not supposed to be called, isn't it ?
      return "";
    }

    code +=
      ARMEngine.Prefix + "LDR\t" + r + ", " + addr + "\n";

    raddr.setStatus(Register.Status.Used);
    rdisp.setStatus(Register.Status.Used);
    r.setStatus(Register.Status.Loaded);
    rout.copy(r);

    return code;
  }

  /**
   * Generate the code for loading a value from the stack to a register
   * @param disp displacement of the variable to load
   * @param rout (out) register in which the value will be
   * @return the generated code
   */
  public String generateLoadFromStack(int disp, Register rout) throws MCSException  {
    Register r = getNextUnusedRegister();
    String code = "", snd = "";

    if (disp < 65536) {
      snd = "#" + Integer.toString(-disp);
    } else {
      Register rval = new Register();
      code += generateLoadConstant(new ConstantInfo(new IntegerType(), -disp), rval);
      snd = rval.toString();
      rval.setStatus(Register.Status.Used);
    }
    code +=
      ARMEngine.Prefix + "LDR\t" + r + ", [" + sb + ", " + snd + "]\n";

    r.setStatus(Register.Status.Loaded);
    rout.copy(r);
    return code;
  }

  /**
	 * Generate the code for loading a constant itneger into a register.
	 * @param info info of the constant to load
	 * @param rout register where the value is put, for later referencing
	 * @return the generated code
	 */
	public String generateLoadConstant(ConstantInfo info, Register rout) throws MCSException  {
    String code = "";
    Register r = getNextUnusedRegister();
    Type t = info.type();

    if (t instanceof SimpleType) {
      Integer val = (Integer)info.value();

      code +=
        ARMEngine.Prefix + "MOV\t" + r + ", #" + (val & 0x0000FFFF) + "\n";

      if (val >= 65536) {
        code +=
          ARMEngine.Prefix + "MOVT\t" + r + ", #" + (val >> 16) + "\n";
      }
    } else if (t instanceof StructType) {
      // TODO
    } else if (t instanceof ArrayType) {
      // TODO
    } else if (t instanceof Klass) {
    }

    r.setStatus(Register.Status.Loaded);
    rout.copy(r);

    return code;
  }

  /**
   * Generate the code for loading a variable from the heap into a register
   * @param raddr register containing the address
   * @param disp optionnal displacement (integer) for struct
   * @param rout register where the value is put
   * @return the generated code
   */
  public String generateLoadFromHeap(Register raddr, int disp, Register rout) throws MCSException  {
    Register r = new Register();
    String code = "";

    if (disp < 65536) {
      r = getNextUnusedRegister();
      code += ARMEngine.Prefix + "LDR\t" + r + ", [" + raddr + ", #" + disp + "]\n";
      r.setStatus(Register.Status.Loaded);
      raddr.setStatus(Register.Status.Used);
      rout.copy(r);
    } else {
      code += generateLoadConstant(new ConstantInfo(new IntegerType(), disp), r);
      code += generateLoadFromHeap(raddr, r, rout);
    }

    return code;
  }

  /**
   * Generate the code for loading a variable from the heap into a register, with an optionnal register displacement
   * @param raddr register containing the address
   * @param rdisp (register) optionnal displacement in a register (for arrays)
   * @param rout register where the value is put
   * @return the generated code
   */
  public String generateLoadFromHeap(Register raddr, Register rdisp, Register rout) throws MCSException  {
    String code = "";
    Register r = getNextUnusedRegister();

    code += ARMEngine.Prefix + "LDR\t" + r + ", [" + raddr + ", " + rdisp + "]\n";

    raddr.setStatus(Register.Status.Used);
    rdisp.setStatus(Register.Status.Used);
    r.setStatus(Register.Status.Loaded);
    rout.copy(r);

    return code;
  }

  ///////////// STORE ////////////

  /**
   * Generate the code for 'updating' the value of a variable in the stack
   * @param info info of the variable to store
   * @param rin value to put in the variabe
   * @return the generated code
   */
  public String generateStoreVariable(VariableInfo vinfo, Register rin) throws MCSException  {
    Type t = vinfo.type();
    String code = "";

    if (t instanceof SimpleType) {
      code +=
        ARMEngine.Prefix + "STR\t" + rin + ", [SB, " + Integer.toString(-vinfo.displacement()) + "]\n";
    } else if (t instanceof StructType) {
      // Shouldn't be called like that
    } else if (t instanceof ArrayType) {
      // Shouldn't be called like that
    } else if (t instanceof Klass) {
    }

    rin.setStatus(Register.Status.Used);
    return code;
  }

  /**
   * Generate the code for 'updating' the value of a variable in the stack, with an additionnal displacement
   * @param info info of the variable to store
   * @param disp (integer) displacement to consider
   * @param rin value to put in the variabe
   * @return the generated code
   */
  public String generateStoreVariable(VariableInfo vinfo, int disp, Register rin) throws MCSException  {
    String code = "", addr = "";
    Type t = vinfo.type();

    if (t instanceof CompositeType) {
      Register raddr = new Register();
      code +=
        generateLoadFromStack(vinfo.displacement(), raddr) +
        generateStoreInHeap(raddr, disp, rin);
    } else {
      // Shouldn't be called ?
    }

    return code;
  }

  /**
   * Generate the code for 'updating' the value of a variable in the stack
   * @param info info of the variable to store
   * @param rdisp (register) displacement to consider
   * @param rin value to put in the variabe
   * @return the generated code
   */
  public String generateStoreVariable(VariableInfo vinfo, Register rdisp, Register rin) throws MCSException  {
    String code = "";
    Type t = vinfo.type();

    if (t instanceof CompositeType) {
      Register raddr = new Register();
      code +=
        generateLoadFromStack(vinfo.displacement(), raddr) +
        generateStoreInHeap(raddr, rdisp, rin);
    } else {
      // Shouldn't be called ?
    }

    return code;
  }

  /**
   * Generate the code for storing a variable into the heap
   * @param raddr register containing the address
   * @param disp (integer) optionnal displacement for structs
   * @param rin register containing the value to be stored
   * @return the generated code
   */
  public String generateStoreInHeap(Register raddr, int disp, Register rin) throws MCSException  {
    String code = "", addr = raddr + ", ";

    raddr.setStatus(Register.Status.Used);

    if (disp < 65536) {
      addr += "#" + disp;
    } else {
      Register r = new Register();
      code +=
        generateLoadConstant(new ConstantInfo(new IntegerType(), disp), r);
      addr += r;
      r.setStatus(Register.Status.Used);
    }
    
    code += 
      ARMEngine.Prefix + "STR\t" + rin + ", [" + addr + "]\n";

    rin.setStatus(Register.Status.Used);
    return code;
  }

  /**
   * Generate the code for storing a variable into the heap, with optionnal register displacement
   * @param raddr register containing the address
   * @param rdisp (register) optionnal displacement for arrays
   * @param rin register containing the value to be stored
   * @return the generated code
   */
  public String generateStoreInHeap(Register raddr, Register rdisp, Register rin) throws MCSException  {
    String code =
      ARMEngine.Prefix + "STR\t" + rin + ", [" + raddr + ", " + rdisp + "]\n";
    raddr.setStatus(Register.Status.Used);
    rdisp.setStatus(Register.Status.Used);
    rin.setStatus(Register.Status.Used);
    return code;
  }

  /**
   * Generate the code for allocating a variable in the stack
   * @param type type to allocate
   * @return the generated code
   */
  public String generateAllocateInStack(Type type) throws MCSException  {
    String code = "";

    if (type instanceof StructType) {
      Register raddr = new Register();
      code += generateAllocate(type, raddr, null);
      code += ARMEngine.Prefix + "PUSH\t" + raddr + "\n";
    } else if (type instanceof ArrayType) {
    } else {
    }

    return code;
  }
	
  /**
	 * Generate the code for allocating a block in the heap
	 * @param type type to allocate
	 * @param raddr (out) register containing the address of the block
	 * @param rsize register containing the size of the block (array only)
	 * @return the generated code
	 */
	public String generateAllocate(Type type, Register raddr, Register rsize) throws MCSException  {
		Register reg = getNextUnusedRegister();

		String code = ARMEngine.Prefix + "MOV\t" + reg + ", " + ht + " ; Store current address\n";

		if (type instanceof StructType) {
			StructType ts = (StructType)type;
			Register r = new Register();
			for (Type t : ts.fieldsTypes()) {
				code += generateAllocate(t, r, null);
			}
		} else if (type instanceof Klass) {
      Klass k = (Klass)type;
      Register r = new Register();
      for (Type t : k.attributeTypes()) {
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
	 * Generate the code for flushing the stack top variable
	 * @param type type of the variable
	 * @return the generated code
	 */
	public String generateFlushVariable(Type type) throws MCSException  {
		Register reg = getNextUnusedRegister();
		String code = ARMEngine.Prefix;

		/*if (type instanceof StructType) {
			StructType ts = (StructType)type;
			for (Type tsf : ts.fieldsTypes()) {
				generateFlushVariable(tsf);
			}
		} else {*/
			code += "POP\t" + reg + "\n";
			heapbase++;
		/*}*/

		return code;
	}

	/**
	 * Generate the code for flushing every variable of a symbol table
	 * Note: used when going out from a block
	 * @param symtab the symbol table
	 * @return the generated code
	 */
	public String generateFlush(SymbolTable symtab) throws MCSException  {
		Register reg = getNextUnusedRegister();
		String code = "";
		ListIterator<String> iter = symtab.symbols().listIterator(symtab.symbols().size());

		while (iter.hasPrevious()) {
			VariableInfo vi = (VariableInfo)symtab.lookup(iter.previous(), true);
			//code += ARMEngine.Prefix + "LDR\t" + reg + ", [SP, " + vi.displacement() + "]\n";
			code += generateFlushVariable(vi.type());
		}

		return code;
	}


	/// Functions related

	/**
	 * Generate the code for the beginning of declaring a function
	 * @param info the info of the function
	 * @param code code generated for the content of the function
	 * @return the generated code
	 */
	public String generateFunctionDeclaration(FunctionInfo info, String blockcode) throws MCSException {
		Register r = getNextUnusedRegister();
		info.assignRegister(r);
		
		String code =
			info.label() + ":\n" +
			ARMEngine.Prefix + "; Push link register, stack base and stack pointer\n" +
			ARMEngine.Prefix + "PUSH\t" + lr + "\n" +
			ARMEngine.Prefix + "PUSH\t" + sb + "\n" + 
			ARMEngine.Prefix + "PUSH\t" + sp + "\n" +
			blockcode;

		// End of the function
		if (!(info.returnType() instanceof VoidType)) {
			Register rval = getNextUnusedRegister();

			code +=
				ARMEngine.Prefix + "; Default return. It is not wise to reach this point\n" +
				ARMEngine.Prefix + "MOV\t" + rval + ", #0\n" + 
				ARMEngine.Prefix + "MOV\t" + info.register() + ", " + ht + "\n" +
				ARMEngine.Prefix + "STMIA\t!" + ht + ", " + rval + "\n\n";
		}

		code +=
			info.label() + "_end:\n" +
			ARMEngine.Prefix + "; Pop registers \n" +
			ARMEngine.Prefix + "POP\t" + sp + "\n" +
			ARMEngine.Prefix + "POP\t" + sb + "\n" +
			ARMEngine.Prefix + "POP\t" + lr + "\n\n" +
			ARMEngine.Prefix + "; Pop arguments \n";

		// As we push arguments in one order, we need to pop them in the
		// other
		ListIterator<Type> iter = info.parameters().listIterator(info.parameters().size());
		while (iter.hasPrevious()) {
			code += generateFlushVariable(iter.previous());
		}

		code +=
			ARMEngine.Prefix + "BX\t" + lr + "\n\n";

		return code;
	}


	/**
	 * Generate the code for the 'return' keyword
	 * @param info the info of the fuunction
	 * @param rval the register containing the value to be returned
	 * @return the generated code
	 */
	public String generateFunctionReturn(FunctionInfo info, Register rval) {
		String code = "";

		if (!(info.returnType() instanceof VoidType)) {
			code +=
				ARMEngine.Prefix + "MOV\t" + info.register() + ", " + ht + "\n" +
				ARMEngine.Prefix + "STMIA\t!" + ht + ", " + rval + "\n";

			info.register().setStatus(Register.Status.Loaded);
			rval.setStatus(Register.Status.Used);
		}

		code +=
			ARMEngine.Prefix + "B\t" + info.label() + "_end\n\n";

		return code;
	}

	/**
	 * Generate the code for pushing an argument
	 * @param reg register in which the argument is stored
	 * @return the generated code
	 */
	public String generateFunctionPushArgument(Register reg) throws MCSException  {
		String code =
			ARMEngine.Prefix + "PUSH\t" + reg + "\n";
		reg.setStatus(Register.Status.Used);
		heapbase++;
		return code;
	}


	/**
	 * Generate the code for the call to a function
	 * @param info info of the function
	 * @return the generated code
	 */
	public String generateFunctionCall(FunctionInfo info) throws MCSException  {
		String code =
			ARMEngine.Prefix + "BL\t" + info.label() + "\n";
		return code;
	}

  /**
   * Generate the code for the declaration of a method
   * @param info info of the method
   * @return the generated code
   */
  public String generateMethodDeclaration(MethodInfo info, String blockcode) throws MCSException {
    String code =
      ARMEngine.Prefix + "; Vtable redirection\n" +
      ARMEngine.Prefix + 
  }

	////////////////////////////// MISC ///////////////////////////////
	/**
	 * Generate the code for making an address from a list of displacement pair.
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
	public String generateMakeAddress(DisplacementList dlist, Register raddr) throws MCSException {
		return generateMakeAddress(dlist, sb, raddr);
	}

	/**
	 * Generate the code for making an address from a list of displacement pair,
	 * using the specified register as base register.
	 * @param dlist displacement list
	 * @param rbaseaddr base address register
	 * @param raddr (out) register that will contain the address
	 * @return the generated code
	 */
	public String generateMakeAddress(DisplacementList dlist, Register rbaseaddr, Register raddr) throws MCSException {
    String code = "";
		Register r = getNextUnusedRegister();
		DisplacementPair dp;
		
		// First displacement is the one of the struct itself
		// it is special because it is relative to the stack
		ListIterator<DisplacementPair> iter = dlist.listIterator();
		dp = iter.next();
		code += ARMEngine.Prefix + "LDR\t" + r + ", [" + rbaseaddr + ", #" + Integer.toString(-dp.disp) + "]\n";

		while (iter.hasNext()) {
			dp = iter.next();
			if (dp.deref)
				code +=
					ARMEngine.Prefix + "LDR\t" + r + ", [" + r + "]\n";
			code +=
				ARMEngine.Prefix + "LDR\t" + r + ", [" + r + ", #" + dp.disp + "]\n";
		}

		rbaseaddr.setStatus(Register.Status.Used);
		r.setStatus(Register.Status.Loaded);
		raddr.copy(r);

		return code;
	}

  /**
   * Generate the code for an if-then-else structure
   * @param rcond register containing the result of the condition
   * @param cif code for the if branch
   * @param celse code for the else branch
   * @return the generated code
   */
  public String generateIfThenElse(Register rcond, String cif, String celse) throws MCSException {
		boolean else_present = !(celse.isEmpty());
    String code = 
      ARMEngine.Prefix + "CBZ\t" + rcond + ", " + (else_present ? "else" : "end") + "_" + condition_nb + "\n" +
      cif + "\n";

		if (else_present) {
			code +=
      	ARMEngine.Prefix + "B\tend_" + condition_nb + "\n" +
      	"else_" + condition_nb + ":\n" +
      	celse + "\n";
		}

		code +=
      "end_" + condition_nb + ":\n\n";

    rcond.setStatus(Register.Status.Used);
    condition_nb++;

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
	public String generateOperation(Operator op, Register r1, Register r2, Register rout) throws MCSException  {
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
	public String generateOperation(Operator op, Register rin, Register rout) throws MCSException  {
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
			case PLS:
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

	public String generateOperation(RelationalOperator op, Register r1, Register r2, Register rout) throws MCSException  {
		Register r = getNextUnusedRegister();
		String code = ARMEngine.Prefix + "MOV\t" + r + "#0\n"; 
		switch (op) {
			/* AND is a specification of EQ */
			case EQ:
				code += ARMEngine.Prefix + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case NEQ:
				code += ARMEngine.Prefix + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case LT:
				code += ARMEngine.Prefix + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case LEQ:
				code += ARMEngine.Prefix + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case GT:
				code += ARMEngine.Prefix + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case GEQ:
				code += ARMEngine.Prefix + "CMP\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case AND:
				code += ARMEngine.Prefix + "ANDS\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
				break;	
			case OR:
				code += ARMEngine.Prefix + "ORRS\t" + r1 + ", " + r2 + "\n";
				code += ARMEngine.Prefix + "MOVEQ\t" + r + "1" + "\n";
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
	public String generateOperation(RelationalOperator op, Register rin, Register rout) throws MCSException {
		Register r = getNextUnusedRegister();
		String code = ARMEngine.Prefix + "MOV\t" + r + "#0\n";
		switch (op) {
			case NOT:
				code += ARMEngine.Prefix + "CMP\t" + rin + ", 0\n";
				code += ARMEngine.Prefix + "MOVNE\t" + r + "1" + "\n";
				break;
		}

		// Information about register
		rin.setStatus(Register.Status.Used);

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
	private Register getNextUnusedRegister() throws MCSException {
		// TODO: register use policy
		for (int i = 0; i < registers.size(); i++) {
			if (registers.get(i).status() == Register.Status.Empty)
				return registers.get(i);
		}

		for (int i = 0; i < registers.size(); i++) {
			if (registers.get(i).status() == Register.Status.Used)
				return registers.get(i);
		}

    throw new MCSRegisterLimitReachedException();
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


