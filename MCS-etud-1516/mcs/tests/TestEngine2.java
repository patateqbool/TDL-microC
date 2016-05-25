package mcs.tests;

import mcs.gc.*;
import mcs.symtab.*;

class TestEngine2 {

    public static void main(String args[]) {
        System.out.println("Test de SUB");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register r2 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 3), r2) +
            engine.generateOperation(IMachine.Operation.SUB, r1, r2, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de MUL");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register r2 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 3), r2) +
            engine.generateOperation(IMachine.Operation.MUL, r1, r2, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de DIV");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register r2 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 3), r2) +
            engine.generateOperation(IMachine.Operation.DIV, r1, r2, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de NEG");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateOperation(IMachine.Operation.NEG, r1, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de AND");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register r2 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 3), r2) +
            engine.generateOperation(IMachine.Operation.AND, r1, r2, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de OR");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register r2 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 3), r2) +
            engine.generateOperation(IMachine.Operation.OR, r1, r2, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de MOD");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register r2 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 3), r2) +
            engine.generateOperation(IMachine.Operation.MOD, r1, r2, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de NOT");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateOperation(IMachine.Operation.NOT, r1, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");

        System.out.println("Test de PLS");
        String code = "";
        ARMEngine engine = new ARMEngine();
        Register r1 = new Register("R", -1);
        Register rout = new Register("R", -1);
        code =
            engine.generateLoadValue(new VariableInfo(new IntegerType(), 2), r1) +
            engine.generateOperation(IMachine.Operation.PLS, r1, rout);
        System.out.println(code);
        System.out.println("output register : " + rout);
        System.out.println("----------------------------");
    }
}

