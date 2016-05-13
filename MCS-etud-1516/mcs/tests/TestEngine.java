package mcs.tests;

import mcs.gc.*;
import mcs.symtab.*;

class TestEngine {

	public static void main(String args[]) {
		String code = "";
		ARMEngine engine = new ARMEngine();
		Register r1 = new Register("R", -1);
		Register r2 = new Register("R", -1);
		Register rout = new Register("R", -1);
		code =
			engine.generateLoadVariable(new VariableInfo(new IntegerType(), 26), r1) +
			engine.generateLoadInteger(0x00123456, r2) +
			engine.generateOperation(IMachine.Operator.ADD, r1, r2, rout);
		System.out.println(code);
		System.out.println("output register : " + rout);
		System.out.println("======================================");

		code = "";
		for (int i = 0; i < 50; i++) {
			Register r = new Register();
			VariableInfo vi = new VariableInfo(new IntegerType(), i);
			code += engine.generateLoadVariable(vi, r);
			code += engine.generateStoreVariable(vi);
		}
		System.out.println(code);
		System.out.println(engine.logRegisters());
	}
}


