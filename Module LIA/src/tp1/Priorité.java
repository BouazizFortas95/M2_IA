/**
 * 
 */
package tp1;

/**
 * @author Bouaziz Fortas
 *
 */
public enum Priorité {

	LEFT_BRACKET(0),
	RIGHT_BRACKET(1),
	NOT(2),
	AND(3),
	OR(4),
	IPML(5),
	EQUALS(6),
	OPEREND(7);

	private int index;
	
	Priorité(int index)
    {
        this.index = index;
    }
	
	public int getIndex() {
		return index;
	}
}
