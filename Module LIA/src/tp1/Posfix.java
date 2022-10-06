/**
 * 
 */
package tp1;

import java.util.Arrays;
import java.util.Stack;


/**
 * @author Bouaziz Fortas
 *
 */
public class Posfix {
	
	private String infix;
	private String postfix;
	
	private Stack<Priorité> stack = new Stack<Priorité>();
	private static final String[] operans = {"(", ")", "!", "&", "|", ">", "#"};

	
	public Posfix(String infix) {
		setInfix(infix);
		setPostfix("");
	}

	
	public String getInfix() {
		return infix;
	}

	public void setInfix(String infix) {
		this.infix = infix;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	private Priorité getPriorité(String symbol) {
		switch (symbol) {
		case "(":
			return Priorité.LEFT_OPER;
		case ")":
			return Priorité.RIGHT_OPER;
		case "!":
			return Priorité.NOT;
		case "&":
			return Priorité.AND;
		case "|":
			return Priorité.OR;
		case ">":
			return Priorité.IPML;
		case "#":
			return Priorité.EQUALS;
		default:
			return Priorité.OPEREND;
		}
	}

	public String infixToPostfix() {
		Priorité symbol;
		System.out.println("\t _____________________________");
		for (int i = 0; i < infix.length(); i++) {
			symbol = getPriorité(Character.toString(infix.charAt(i)));
			if(i!=0)
				System.out.println("\t|_____________________________");
			System.out.println("\t| Symbol " + infix.charAt(i) + " : " + symbol);
			if (symbol == Priorité.OPEREND) {
				postfix += infix.charAt(i);
			} else if(symbol == Priorité.RIGHT_OPER) {
				while (stack.lastElement() != Priorité.LEFT_OPER) {
					postfix += operans[stack.pop().getIndex()];
				}
				stack.pop();
			}else {
				while ((!stack.isEmpty()) && (stack.lastElement().getIndex() >= symbol.getIndex())) {
					postfix += operans[stack.pop().getIndex()];
				}
				stack.push(symbol);
			}
			System.out.println("\t| Postfix '" + postfix + "' \n\t| " + Arrays.deepToString(stack.toArray()));
		}
		while ((!stack.isEmpty()) && ((symbol = stack.pop()) != Priorité.OPEREND)) {
			postfix += operans[symbol.getIndex()]; 
		}
		return postfix;
	}
}
