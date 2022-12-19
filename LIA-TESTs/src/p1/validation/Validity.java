/**
 * 
 */
package p1.validation;

import java.util.ArrayList;

/**
 * @author Bouaziz Fortas
 *
 */
public class Validity {

	private ArrayList<String> list;
	private ArrayList<String> user_check;
	private ArrayList<String> variables;
	private ArrayList<String> operators;

	/**
	 * 
	 */
	public Validity() {
		setList(new ArrayList<String>());
		setUser_check(new ArrayList<String>());
		setVariables(new ArrayList<String>());
		setOperators(new ArrayList<String>());
	}

	/**
	 * @return the list
	 */
	public ArrayList<String> getList() {
		return list;
	}

	/**
	 * @param list the list to set
	 */
	public void setList(ArrayList<String> list) {
		this.list = list;
	}

	/**
	 * @return the user_check
	 */
	public ArrayList<String> getUser_check() {
		return user_check;
	}

	/**
	 * @param user_check the user_check to set
	 */
	public void setUser_check(ArrayList<String> user_check) {
		this.user_check = user_check;
	}

	/**
	 * @return the variables
	 */
	public ArrayList<String> getVariables() {
		return variables;
	}

	/**
	 * @param variables the variables to set
	 */
	public void setVariables(ArrayList<String> variables) {
		this.variables = variables;
	}

	/**
	 * @return the operators
	 */
	public ArrayList<String> getOperators() {
		return operators;
	}

	/**
	 * @param operators the operators to set
	 */
	public void setOperators(ArrayList<String> operators) {
		this.operators = operators;
	}

	public void addSyntax(String syn) {
		getList().add(syn);
	}

	private void breakSyntax(String syn) {
		for (int i = 0; i < syn.length(); i++) {
			getUser_check().add(syn.substring(i, i + 1));
		}
	}

	private void addUserSyntax(String userSyn) {
		breakSyntax(userSyn);
	}

	private void chunks() {
		String store = "";
		boolean flag = false;
		for (int i = 0; i < getUser_check().size(); i++) {
			if (Character.isLetterOrDigit(getUser_check().get(i).charAt(0))) {
				store += getUser_check().get(i).charAt(0);
				flag = true;
			}

			if (flag == true && !(Character.isLetterOrDigit(getUser_check().get(i).charAt(0)))) {
				getVariables().add(store);
				flag = false;
				store = "";
			}

			if (getUser_check().get(i).charAt(0) != '(' && getUser_check().get(i).charAt(0) != ')'
					&& (Character.isLetterOrDigit(getUser_check().get(i).charAt(0)))) {

				if (getUser_check().get(i).charAt(0) == '/' && this.getUser_check().get(i + 1).charAt(0) == '\\') {
					getOperators().add("&");
				} else if (getUser_check().get(i).charAt(0) == '\\' && getUser_check().get(i + 1).charAt(0) == '/') {
					getOperators().add("|");
				} else if (getUser_check().get(i).charAt(0) == '-' && getUser_check().get(i + 1).charAt(0) == '>') {
					getOperators().add("->");
				} else if (getUser_check().get(i).charAt(0) == '~') {
					getOperators().add("~");
				}
			}
		}
	}

}
