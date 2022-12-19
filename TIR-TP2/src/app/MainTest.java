/**
 * 
 */
package app;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 * @author Bouaziz Fortas
 *
 */
public class MainTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// TODO Dynamic Array
				ArrayList<String> arrayList = new ArrayList<String>();
				arrayList.add("Fares");
				arrayList.add("Mohammed");
				arrayList.add("Basset");
				arrayList.add("Saif");
//				arrayList.remove(0);
				for (String name : arrayList) {
					System.out.println("My name is : "+name);
				}
				System.err.println("-----------------");
				// TODO Linked List
				LinkedList<Integer> linkedList = new LinkedList<Integer>();
				linkedList.add(5);
				
				// TODO Stack
				Stack<String> stack = new Stack<String>();
				stack.push("Fares");
				stack.push("Mohamed");
				System.out.println("Name of Last Item : "+stack.pop());
	}

}
