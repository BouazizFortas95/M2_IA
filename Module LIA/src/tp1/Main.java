/**
 * 
 */
package tp1;

/**
 * @author Bouaziz Fortas
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Posfix postfix = new Posfix("(A|B)>!C");
		String pf = postfix.infixToPostfix();
		
		System.out.println("\t|_____________________________");
		System.out.println("\t| Postfix expression : " + pf);
		System.out.println("\t|_____________________________");
	}

}
