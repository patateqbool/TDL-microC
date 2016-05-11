/**
 * ARMEngine -- define the generator for arm
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

import java.utils.List;
import java.utils.ArrayList;

public class ARMEngine extends AbstractMachine {
	static private final int NUM_REGISTER = 13;
	private List<Register> registers;
	private Register sp, lr, pc;

	/**
	 * Constructor
	 */
	public ARMEngine() {
		for (int i = 0; i < NUM_REGISTER; i++) {
			registers.add(new Register("R", i));
		}
		sp = new Register("SP", -1);
		lr = new Register("LR", -1);
		pc = new Register("PC", -1);
	}

	/**
	 * Suffix for target file : asm
	 */
	public String getSuffix() {
		return ".asm";
	}

	/**************************************************/
	private Register getNextUnusedRegister() {
		for (int i = 0; i < registers.length; i++) {
			if (register.status() == Register.Status.Empty)
				return registers[i];
		}
	}
}


