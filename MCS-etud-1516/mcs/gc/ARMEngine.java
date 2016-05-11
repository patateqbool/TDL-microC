/**
 * ARMEngine -- define the generator for arm
 *
 * @author G. Dupont
 * @version 0.1
 */
package mcs.gc;

public class ARMEngine extends AbstractMachine {
	/**
	 * Constructor
	 */
	public ARMEngine() {
	}

	/**
	 * Suffix for target file : asm
	 */
	public String getSuffix() {
		return ".asm";
	}
}


