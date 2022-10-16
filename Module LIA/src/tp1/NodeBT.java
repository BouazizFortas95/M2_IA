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
	private Priorité priorité;
	private NodeBT left, right;
	
	public NodeBT(char symbol, Priorité priorité, NodeBT left, NodeBT right) {
		setSymbol(symbol);
		setPriorité(priorité);
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
	 * @return the priorité
	 */
	public Priorité getPriorité() {
		return priorité;
	}

	/**
	 * @param priorité the priorité to set
	 */
	public void setPriorité(Priorité priorité) {
		this.priorité = priorité;
	}

}
