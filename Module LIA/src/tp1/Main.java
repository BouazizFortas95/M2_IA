/**
 * 
 */
package tp1;

import java.util.Scanner;

/**
 * @author Bouaziz Fortas
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Examples de TD : 
		// 1.   (P|((Q&!R)>P))
		// 2.   (P>(Q|R))|!(R>W)
		
		System.out.print("Infix : ");
		Scanner input = new Scanner(System.in);
		String infix = input.nextLine();
		
//		Posfix postfix = new Posfix(infix);
//		String pf = postfix.infixToPostfix();
//		
//		System.out.println("\t_____________________________");
//		System.out.println("\tPostfix Expression : " + pf);
//		System.out.println("\t_____________________________");
		
		Posfix postfix = new Posfix(infix);
		NodeBT root = new NodeBT('>', Priorité.IPML, null, null);
		BinaryTree bt = new BinaryTree(root);
		
		postfix.infixToPostfixWithBT(bt);
		
		postfix.printEulerTour(root);
		
		input.close();
	}

}
