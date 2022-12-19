/**
 * 
 */
package p1.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bouaziz Fortas
 *
 */
public abstract class TruthTable {

	boolean[] variables;
	private List<Boolean> rows;
	private int numRows;

	/**
	 * 
	 */
	public TruthTable() {
		init(getNumVariables());
	}

	/**
	 * @return the rows
	 */
	public List<Boolean> getRows() {
		return rows;
	}

	/**
	 * @param rows the rows to set
	 */
	public void setRows(List<Boolean> rows) {
		this.rows = rows;
	}

	/**
	 * @return the numRows
	 */
	public int getNumRows() {
		return numRows;
	}

	/**
	 * @param numRows the numRows to set
	 */
	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumVariables() {
		return 4;
	}

	/**
	 * @param numVariables
	 * @return this
	 */
	private TruthTable init(int numVariables) {
		variables = new boolean[numVariables];
		setNumRows(((int) Math.pow(2, variables.length)));
		setRows(new ArrayList<Boolean>(getNumRows()));
		return this;
	}

	public TruthTable compute() {
		getRows().clear();
		for (int i = 0; i < getNumRows(); i++) {
			for (int j = variables.length - 1; j >= 0; j--) {
				variables[i] = (i / (int) Math.pow(2, j)) % 2 == 0;
			}
			boolean rowOutput = expression();
			getRows().add(rowOutput);
		}
		return this;
	}

	private boolean expression() {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (getNumRows()!=((TruthTable)obj).getNumRows()) 
			return false;
		compute();
		((TruthTable)obj).compute();
		
		for (int i = 0; i < getRows().size(); i++) {
			if(getRows().get(i) != ((TruthTable)obj).getRows().get(i))
				return false;
		}
		return true;
	}
}
