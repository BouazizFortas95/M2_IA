/**
 * 
 */
package tp1;

/**
 * @author Bouaziz Fortas
 *
 */
public class BinaryTree {

	private NodeBT root;

	public BinaryTree(NodeBT root) {
		setRoot(root);
	}

	/**
	 * @return the root
	 */
	public NodeBT getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(NodeBT root) {
		this.root = root;
	}

	public void addNode2BT(NodeBT newNode, NodeBT rootExplore) {
//		if (rootExplore == null) {
//			System.err.println("Null added Node !!!!!");
//		}

		if (newNode.getPriorité().getIndex() >= rootExplore.getPriorité().getIndex()) {
			if (rootExplore.getRight() == null) {
				rootExplore.setRight(newNode);
			} else {
				addNode2BT(newNode, rootExplore.getLeft());
			}
		}
		if (newNode.getPriorité().getIndex() < rootExplore.getPriorité().getIndex()) {
			if (rootExplore.getLeft() == null) {
				rootExplore.setLeft(newNode);
			} else {
				addNode2BT(newNode, rootExplore.getLeft());
			}
		}

	}

}
