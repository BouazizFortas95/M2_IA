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
	
	private Stack<Priorit�> stack = new Stack<Priorit�>();
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

	private Priorit� getPriorit�(String symbol) {
		switch (symbol) {
		case "(":
			return Priorit�.LEFT_OPER;
		case ")":
			return Priorit�.RIGHT_OPER;
		case "!":
			return Priorit�.NOT;
		case "&":
			return Priorit�.AND;
		case "|":
			return Priorit�.OR;
		case ">":
			return Priorit�.IPML;
		case "#":
			return Priorit�.EQUALS;
		default:
			return Priorit�.OPEREND;
		}
	}

	public String infixToPostfix() {
		Priorit� symbol;
		System.out.println("\t _____________________________");
		for (int i = 0; i < infix.length(); i++) {
			symbol = getPriorit�(Character.toString(infix.charAt(i)));
			if(i!=0)
				System.out.println("\t|_____________________________");
			System.out.println("\t| Symbol " + infix.charAt(i) + " : " + symbol);
			if (symbol == Priorit�.OPEREND) {
				postfix += infix.charAt(i);
			} else if(symbol == Priorit�.RIGHT_OPER) {
				while (stack.lastElement() != Priorit�.LEFT_OPER) {
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
		while ((!stack.isEmpty()) && ((symbol = stack.pop()) != Priorit�.OPEREND)) {
			postfix += operans[symbol.getIndex()]; 
		}
		return postfix;
	}
}
