/**
 * 
 */
package applet;

import java.applet.Applet;
import java.awt.Button;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Bouaziz Fortas
 *
 */
@SuppressWarnings("serial")
public class FactInt extends Applet implements ActionListener {

	// Set up a button, label, and input field
	private TextField inputField;
	private Label label;
	private Label outLabel;
	private Button button;

	private int factorial(int n)
	// Assumption: n is not negative
	{
		if (n == 0)
			return 1; // Base case
		else
			return (n * factorial(n - 1)); // General case
	}

	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//
//	}
	
	public void init()
	{
		// Instantiate components
		label = new Label("Enter an integer; click Enter.");
		outLabel = new Label("Answer");
		button = new Button("Enter");
		button.addActionListener(this);
		inputField = new TextField("Value here");
		// Add components
		add(label);
		add(inputField);
		add(button);
		add(outLabel);
		// Specify a layout manager for the window 
		setLayout(new GridLayout(4,1));
		}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		int value;
		value = Integer.parseInt(inputField.getText());
		inputField.setText("");
		outLabel.setText(value + " factorial is " + factorial(value));
	}

}
