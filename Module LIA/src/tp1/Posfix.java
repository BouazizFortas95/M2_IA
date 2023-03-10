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

	private Stack<Priorit?> stack = new Stack<Priorit?>();
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

	private Priorit? getPriorit?(char symbol) {
		switch (symbol) {
		case '(':
			return Priorit?.LEFT_BRACKET;
		case ')':
			return Priorit?.RIGHT_BRACKET;
		case '!':
			return Priorit?.NOT;
		case '&':
			return Priorit?.AND;
		case '|':
			return Priorit?.OR;
		case '>':
			return Priorit?.IPML;
		case '#':
			return Priorit?.EQUALS;
		default:
			return Priorit?.OPEREND;
		}
	}

	public String infixToPostfix() {
		Priorit? symbol;
		System.out.println("\t______________________________");
		for (int i = 0; i < infix.length(); i++) {
			symbol = getPriorit?(infix.charAt(i));
			if (i != 0)
				System.out.println("\t_____________________________");
			System.out.println("\tSymbol " + infix.charAt(i) + " : " + symbol);
			if (symbol == Priorit?.OPEREND) {
				postfix += infix.charAt(i);
			} else if (symbol == Priorit?.RIGHT_BRACKET) {
				while (stack.lastElement() != Priorit?.LEFT_BRACKET) {
					postfix += operans[stack.pop().getIndex()];
				}
				stack.pop();
			} else {
				while ((!stack.isEmpty()) && (stack.lastElement().getIndex() > Priorit?.OPEREND.getIndex())) {
					postfix += operans[stack.pop().getIndex()];
				}
				stack.push(symbol);
			}
			System.out.println("\tPostfix '" + postfix + "' \n\t" + Arrays.deepToString(stack.toArray()));
		}

		while (!stack.isEmpty()) {
			symbol = stack.pop();
			if (!symbol.equals(Priorit?.LEFT_BRACKET))
				postfix += operans[symbol.getIndex()];
		}
		return postfix;
	}

	public BinaryTree infixToPostfixWithBT(BinaryTree binaryTree) {
		Priorit? priorit?;
		char symbol;

		for (int i = 0; i < infix.length(); i++) {

			symbol = infix.charAt(i);
			priorit? = getPriorit?(symbol);
			if (symbol == '>') {
				continue;
			} else {
				try {
					if (priorit? == Priorit?.OPEREND) {
						binaryTree.addNode2BT(new NodeBT(symbol, priorit?, null, null), binaryTree.getRoot());
					} else if (priorit? == Priorit?.RIGHT_BRACKET) {
						while ((!stack.isEmpty()) && (!stack.lastElement().equals(Priorit?.LEFT_BRACKET))) {
							priorit? = stack.pop();
							binaryTree.addNode2BT(new NodeBT(operans[priorit?.getIndex()],
									getPriorit?(operans[priorit?.getIndex()]), null, null), binaryTree.getRoot());
						}
						stack.pop();
					} else {
						while ((!stack.isEmpty()) && (stack.lastElement().getIndex() > Priorit?.OPEREND.getIndex())) {
							priorit? = stack.pop();
							binaryTree.addNode2BT(new NodeBT(operans[priorit?.getIndex()],
									getPriorit?(operans[priorit?.getIndex()]), null, null), binaryTree.getRoot());
						}
						stack.push(getPriorit?(symbol));
					}
				} catch (Exception e) {
					//System.err.println(e.getMessage());
				}
			}

			while ((!stack.isEmpty()) && (!stack.isEmpty())) {
				try {
					priorit? = stack.pop();
					if (!priorit?.equals(Priorit?.LEFT_BRACKET))
						binaryTree.addNode2BT(new NodeBT(operans[priorit?.getIndex()], priorit?, null, null),
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
