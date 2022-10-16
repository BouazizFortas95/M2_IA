/**
 * 
 */
package tp1;

/**
 * @author Bouaziz Fortas
 *
 */
public class NodeBT{

	private char symbol;
	private Priorit� priorit�;
	private NodeBT left, right;
	
	public NodeBT(char symbol, Priorit� priorit�, NodeBT left, NodeBT right) {
		setSymbol(symbol);
		setPriorit�(priorit�);
		setLeft(left);
		setRight(right);
	}

	/**
	 * @return the symbol
	 */
	public char getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol the symbol to set
	 */
	public void setSymbol(char symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the left
	 */
	public NodeBT getLeft() {
		return left;
	}

	/**
	 * @param left the left to set
	 */
	public void setLeft(NodeBT left) {
		this.left = left;
	}

	/**
	 * @return the right
	 */
	public NodeBT getRight() {
		return right;
	}

	/**
	 * @param right the right to set
	 */
	public void setRight(NodeBT right) {
		this.right = right;
	}

	/**
	 * @return the priorit�
	 */
	public Priorit� getPriorit�() {
		return priorit�;
	}

	/**
	 * @param priorit� the priorit� to set
	 */
	public void setPriorit�(Priorit� priorit�) {
		this.priorit� = priorit�;
	}

}
