package mcs.tests;

import mcs.gc.*;
import mcs.symtab.*;

class TestEngine3 {

	public static void main(String args[]) {
		String code = "";
		ARMEngine engine = new ARMEngine();
		StructFields stf = new StructField();
		stf.insert("int1", new IntegerType());
		stf.insert("int2", new IntegerType());
		st = new StructType(stf);
		Register raddr = new Register("R", -1);
		code = engine.generateAllocate(StructType, raddr, null);	
		System.out.println(code);
		System.out.println("output register : " + raddr);
		System.out.println("======================================");
	}
}


