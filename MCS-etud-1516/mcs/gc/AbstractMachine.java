package mcs.gc;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import mcs.compiler.MCSException;

/**
 * Cette classe decrit une machine cible.
 * 
 * @author marcel
 * 
 */
public abstract class AbstractMachine implements IMachine {

	/**
	 * Ecrit le code dans un fichier à partir du nom du fichier source et du
	 * suffixe
	 * 
	 * @param fileName
	 * @param code
	 * @throws MCSException
	 */
	@Override
	public void writeCode(String fname, String code) throws MCSException {
		try {
			// pre verifiee au lancement
			int pt = fname.lastIndexOf('.');
			String name = fname.substring(0, pt);
			String asmName = name + "." + getSuffix();
			System.err.println("Ecriture du code dans " + asmName);
			PrintWriter pw = new PrintWriter(new FileOutputStream(asmName));
			pw.print(code);
			pw.close();
		} catch (FileNotFoundException e) {
			throw new MCSException(e.getMessage());
		}
	}

	/**
	 * Utility functions
	 */

	/**
	 * Build an integer from a string
	 * @param val the string
	 * @return the integer
	 */
	public int toInt(String val) {
		return Integer.parseInt(val);
	}

}
