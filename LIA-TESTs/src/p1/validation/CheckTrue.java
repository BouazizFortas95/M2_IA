/**
 * 
 */
package p1.validation;

import java.util.ArrayList;

/**
 * @author Bouaziz Fortas
 *
 */
public class CheckTrue {

	private Validity validity;
	static int count = 0;
	static double rows = 0;
	static int size = 0;
	boolean[] table;
	static int countOP;

	/**
	 * 
	 */
	public CheckTrue(FileHandling fileHandling) {
		setValidity(fileHandling.getValidity());
	}

	/**
	 * @return the validity
	 */
	public Validity getValidity() {
		return validity;
	}

	/**
	 * @param validity the validity to set
	 */
	public void setValidity(Validity validity) {
		this.validity = validity;
	}

	public void initialization() {
		size = ((int) rows * count) + ((int) rows * count / 2);
		this.table = new boolean[size];
	}

	public void decleration() {
		int loop = (int) rows / 2;
		for (int i = 1; i <= size; i++) {
			if (i <= loop) {
				table[i] = true;
			}
			if (i <= rows) {
				table[i] = false;
			}
			if (i == rows) {
				loop = loop / 2;
			}
		}
	}

	public void count() {
		count = getValidity().getVariables().size();
		countOP = getValidity().getOperators().size();
		rows = Math.pow(2, count);
	}

	public boolean trueOrNot(ArrayList<String> list, ArrayList<String> op) {
		int j = 0;
		if ((list.size() == 1) && (op.isEmpty())) {
			return true;
		}

		for (int i = 0; i < list.size(); i++) {
			if (op.get(j) == "&") {
				if (i < list.size() - 1 && list.get(i).matches(list.get(i + 1))) {
					if (j > 0 && op.get(j - 1) == "~")
						return false;
					else if (j < op.size() - 1 && op.get(j + 1) == "~")
						return false;
					else
						return true;
				} else {
					return true;
				}

			}

			if (op.get(j) == "~") {
				j++;
			}

			if (op.get(j) == "|") {
				if (i < list.size() - 1 && list.get(i).matches(list.get(i + 1))) {
					if (j > 0 && op.get(j - 1) == "~") {
						if (j < op.size() - 1 && op.get(j + 1) == "~") {
							return false;
						}
					} else {
						return true;
					}
				} else {
					return true;
				}
			}

			if (op.get(j) == "->") {
				if (i < list.size() - 1 && list.get(i).matches(list.get(i + 1))) {
					if (j < op.size() - 1 && op.get(j + 1) == "~") {
						return false;
					} else {
						return true;
					}
				} else {
					return true;
				}
			}
		}

		return false;
	}

	public boolean solve() {

		ArrayList<String> list = new ArrayList<String>();
		ArrayList<String> oper = new ArrayList<String>();
		ArrayList<String> oper2 = new ArrayList<String>();
		ArrayList<Boolean> bool = new ArrayList<Boolean>();
		int j = 0;

		for (int i = 0; i <= count / 2 + countOP - 2; i++) {
			if (j <= countOP - 1 && j != countOP - 1 && getValidity().getOperators().get(j) == "~") {
				oper2.add(getValidity().getOperators().get(j));
				j++;
			}
			if (i <= count - 1) {
				list.add(getValidity().getVariables().get(i));
				i++;
			}
			if (j <= countOP - 1) {

				oper2.add(getValidity().getOperators().get(j));
				j++;

			}
			if (j <= countOP - 1 && getValidity().getOperators().get(j) == "~") {
				oper2.add(getValidity().getOperators().get(j));
				j++;
			}
			if (i <= count - 1) {
				list.add(getValidity().getVariables().get(i));
			}
			if (j <= countOP - 1) {
				oper.add(getValidity().getOperators().get(j));
				j++;
			}
			if (!list.isEmpty()) {
				bool.add(trueOrNot(list, oper2));
				list.clear();
				oper2.clear();
			}
		}

		for (int i = 0; i < oper.size(); i++) {
			if (!oper.isEmpty() && oper.get(i) == "->") {
				if (bool.get(i) == true && bool.get(i + 1) == false) {
					bool.remove(i);
					bool.remove(i);
					oper.remove(i);
					bool.add(i, false);
				} else {
					bool.remove(i);
					bool.remove(i);
					oper.remove(i);
					bool.add(i, true);
				}

			}
			if (!oper.isEmpty() && oper.get(i) == "&") {
				if (bool.get(i) == true && bool.get(i + 1) == false || bool.get(i) == false && bool.get(i + 1) == true
						|| bool.get(i) == false && bool.get(i + 1) == false) {
					bool.remove(i);
					bool.remove(i);
					oper.remove(i);
					bool.add(i, false);
				} else {
					bool.remove(i);
					bool.remove(i);
					oper.remove(i);
					bool.add(i, true);
				}
			}
			if (!oper.isEmpty() && oper.get(i) == "|") {
				if (bool.get(i) == false && bool.get(i + 1) == false) {
					bool.remove(i);
					bool.remove(i);
					oper.remove(i);
					bool.add(i, false);
				} else {
					bool.remove(i);
					bool.remove(i);
					oper.remove(i);
					bool.add(i, true);
				}
			}
		}
		if (!bool.isEmpty())
			return bool.get(0);
		return true;
	}

}
