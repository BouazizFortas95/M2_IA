/**
 * 
 */
package tests.test1;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author Bouaziz Fortas
 *
 */
public class Simafore extends Application{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// don't automatically shutdown toolkit on window close
	    Platform.setImplicitExit(false);

	    primaryStage.setTitle("Rectangle");
	    AnchorPane root = new AnchorPane();
	    Scene scene = new Scene(root, 600, 700);
	    primaryStage.setScene(scene);
	    primaryStage.setResizable(false);

	    // semaphore for synchronizing the closing 
	    final Semaphore semaphore = new Semaphore(1);

	    // notify loop of window being closed
	    primaryStage.setOnHidden(evt -> semaphore.release());

	    final Scanner scanner = new Scanner(System.in);
	    System.out.println("Enter a number: ");
	    
	    Runnable runnable = () -> {

	        while (true) {
	            // wait for window to be closed
	            try {
	                semaphore.acquire();
	            } catch (InterruptedException ex) {
	                return;
	            }

	            // read number
	            int xx = scanner.nextInt();

	            if (xx == 1) {
	                ArrayList<Boolean> bool = new ArrayList<>();
	                for (int i = 0; i < 10; i++) {
	                    if (i % 2 == 0) {
	                        bool.add(true);
	                    } else {
	                        bool.add(false);
	                    }
	                }
	                //Coordinate variables
	                int y = 50;

	                Node[] children = new Node[bool.size()];

	                //Rectangle loop
	                for (int i = 0; i < children.length; i++) {
	                    Rectangle rectangle = new Rectangle(50, y, 150.0, 50.0);
	                    rectangle.setFill(bool.get(i) ? Color.RED : Color.BLUE);
	                    y += 50;
	                    children[i] = rectangle;
	                }

	                // update gui
	                Platform.runLater(() -> {
	                    root.getChildren().setAll(children);
	                    primaryStage.show();
	                });
	            } else {
	                // shutdown javafx
	                Platform.exit();
	                break;
	            }
	        }
	    };
	    new Thread(runnable).start();
		
	}

}
