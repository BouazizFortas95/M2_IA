/**
 * 
 */
package tp1;

import java.util.Arrays;
import java.util.Stack;
import java.util.Vector;

/**
 * @author Bouaziz Fortas
 *
 */
public class Posfix {

	private String infix;
	private String postfix;

	private Stack<Priorité> stack = new Stack<Priorité>();
	private static final char[] operans = { '(', ')', '!', '&', '|', '>', '#' };

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

	private Priorité getPriorité(char symbol) {
		switch (symbol) {
		case '(':
			return Priorité.LEFT_BRACKET;
		case ')':
			return Priorité.RIGHT_BRACKET;
		case '!':
			return Priorité.NOT;
		case '&':
			return Priorité.AND;
		case '|':
			return Priorité.OR;
		case '>':
			return Priorité.IPML;
		case '#':
			return Priorité.EQUALS;
		default:
			return Priorité.OPEREND;
		}
	}

	public String infixToPostfix() {
		Priorité symbol;
		System.out.println("\t______________________________");
		for (int i = 0; i < infix.length(); i++) {
			symbol = getPriorité(infix.charAt(i));
			if (i != 0)
				System.out.println("\t_____________________________");
			System.out.println("\tSymbol " + infix.charAt(i) + " : " + symbol);
			if (symbol == Priorité.OPEREND) {
				postfix += infix.charAt(i);
			} else if (symbol == Priorité.RIGHT_BRACKET) {
				while (stack.lastElement() != Priorité.LEFT_BRACKET) {
					postfix += operans[stack.pop().getIndex()];
				}
				stack.pop();
			} else {
				while ((!stack.isEmpty()) && (stack.lastElement().getIndex() > Priorité.OPEREND.getIndex())) {
					postfix += operans[stack.pop().getIndex()];
				}
				stack.push(symbol);
			}
			System.out.println("\tPostfix '" + postfix + "' \n\t" + Arrays.deepToString(stack.toArray()));
		}

		while (!stack.isEmpty()) {
			symbol = stack.pop();
			if (!symbol.equals(Priorité.LEFT_BRACKET))
				postfix += operans[symbol.getIndex()];
		}
		return postfix;
	}

	public BinaryTree infixToPostfixWithBT(BinaryTree binaryTree) {
		Priorité priorité;
		char symbol;

		for (int i = 0; i < infix.length(); i++) {

			symbol = infix.charAt(i);
			priorité = getPriorité(symbol);
			if (symbol == '>') {
				continue;
			} else {
				try {
					if (priorité == Priorité.OPEREND) {
						binaryTree.addNode2BT(new NodeBT(symbol, priorité, null, null), binaryTree.getRoot());
					} else if (priorité == Priorité.RIGHT_BRACKET) {
						while ((!stack.isEmpty()) && (!stack.lastElement().equals(Priorité.LEFT_BRACKET))) {
							priorité = stack.pop();
							binaryTree.addNode2BT(new NodeBT(operans[priorité.getIndex()],
									getPriorité(operans[priorité.getIndex()]), null, null), binaryTree.getRoot());
						}
						stack.pop();
					} else {
						while ((!stack.isEmpty()) && (stack.lastElement().getIndex() > Priorité.OPEREND.getIndex())) {
							priorité = stack.pop();
							binaryTree.addNode2BT(new NodeBT(operans[priorité.getIndex()],
									getPriorité(operans[priorité.getIndex()]), null, null), binaryTree.getRoot());
						}
						stack.push(getPriorité(symbol));
					}
				} catch (Exception e) {
					//System.err.println(e.getMessage());
				}
			}

			while ((!stack.isEmpty()) && (!stack.isEmpty())) {
				try {
					priorité = stack.pop();
					if (!priorité.equals(Priorité.LEFT_BRACKET))
						binaryTree.addNode2BT(new NodeBT(operans[priorité.getIndex()], priorité, null, null),
								binaryTree.getRoot());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		}
		return binaryTree;
	}

	// Find Euler Tour
	public Vector<NodeBT> eulerTree(NodeBT root, Vector<NodeBT> euler) {
		// store current node's data
		euler.add(root);

		// If left node exists
		if (root.getLeft() != null) {
			// traverse left subtree
			euler = eulerTree(root.getLeft(), euler);

			// store parent node's data
			euler.add(root);
		}

		// If right node exists
		if (root.getRight() != null) {
			// traverse right subtree
			euler = eulerTree(root.getRight(), euler);

			// store parent node's data
			euler.add(root);
		}
		return euler;
	}

	// Function to print Euler Tour of tree
	public void printEulerTour(NodeBT root) {
		// Stores Euler Tour
		Vector<NodeBT> euler = new Vector<NodeBT>();

		euler = eulerTree(root, euler);

		for (int i = 0; i < euler.size(); i++)
			System.out.print(euler.get(i).getSymbol()+" ");
	}

	
	public void preorder(NodeBT root) {
        if (root == null) {
            return;
        }

        System.out.print(root.getSymbol()+" ");
        preorder(root.getLeft());
        preorder(root.getRight());

    }
}
