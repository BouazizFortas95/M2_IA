/**
 * 
 */
package tp2.hosts;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author Bouaziz Fortas
 *
 */
public class HostA extends Application{

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			HBox root = (HBox) FXMLLoader.load(getClass().getResource("/tp2/hosts/HostAViewer.fxml"));
			Scene scene = new Scene(root, 700, 600);
			primaryStage.setTitle("HostA");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
