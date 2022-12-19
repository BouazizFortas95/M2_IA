/**
 * 
 */
package tp1.forest.fire;

/**
 * @author Bouaziz Fortas
 *
 */
public class Square {
	private int x, y;
	private TypeSquare type;

	/**
	 * 
	 */
	public Square(int x, int y, TypeSquare type) {
		setX(x);
		setY(y);
		setType(type);
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @param x the x to set
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param y the y to set
	 */
	public void setY(int y) {
		this.y = y;
	}

	/**
	 * @return the type
	 */
	public TypeSquare getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(TypeSquare type) {
		this.type = type;
	}

}
