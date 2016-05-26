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
			engine.generateLoadValue(new VariableInfo(new IntegerType(), 26), r1) +
			engine.generateLoadValue(new ConstantInfo(new IntegerType(), 0x00123456), r2) +
			engine.generateOperation(IMachine.Operator.ADD, r1, r2, rout);
		System.out.println(code);
		System.out.println("output register : " + rout);
		System.out.println("======================================");

		code = "";
		for (int i = 0; i < 50; i++) {
			Register r = new Register();
			VariableInfo vi = new VariableInfo(new IntegerType(), i);
			code += engine.generateLoadValue(vi, r);
			code += engine.generateStoreVariable(vi, r);
		}
		System.out.println(code);
		System.out.println(engine.logRegisters());

    Type t1 = new IntegerType();
    Type t2 = new IntegerType();
    Type t3 = new CharacterType();
    System.out.println(t1.isEqualTo(t2));
    System.out.println(t2.isEqualTo(t3));
    System.out.println(t2.isEqualTo(t1));
	}
}


