/**
 * 
 */
package p1.validation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.FileHandler;

/**
 * @author Bouaziz Fortas
 *
 */
public class FileHandling extends FileHandler {

	private FileInputStream in;
	private Validity validity;

	/**
	 * 
	 */
	public FileHandling() throws IOException, SecurityException {

		setIn(new FileInputStream("C:\\Users\\Bouaziz Fortas\\M2_IA\\LIA-TESTs\\src\\p1\\validation\\Rules"));
		setValidity(new Validity());
	}

	/**
	 * @return the in
	 */
	public FileInputStream getIn() {
		return in;
	}

	/**
	 * @param in the in to set
	 */
	public void setIn(FileInputStream in) {
		this.in = in;
	}

	/**
	 * @return the validity
	 */
	public Validity getValidity() {
		return validity;
	}

	/**
	 * @param validity the validity to set
	 */
	public void setValidity(Validity validity) {
		this.validity = validity;
	}

	public void handling() {
		int c;
		String collect = "";
		try {
			while ((c = getIn().read()) != -1) {
				if (c != 44) {
					String parse = String.valueOf(Character.toChars(c));
					collect += parse;
				}
				if (c == 44) {
					getValidity().addSyntax(collect);
					collect = "";
				}
			}
		} catch (IOException e) {
			System.err.println("#IOException : " + e.getMessage());
		}
	}
}
