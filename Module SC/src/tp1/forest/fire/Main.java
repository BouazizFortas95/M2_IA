/**
 * 
 */
package tp1.forest.fire;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * @author Bouaziz Fortas
 *
 */
public class Main extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			HBox root = (HBox) FXMLLoader.load(getClass().getResource("App.fxml"));
			Scene scene = new Scene(root, 1000, 700);
//			scene.getStylesheets().add(getClass().getResource("tp1.forest.fire.css").toExternalForm());
			primaryStage.setTitle("Simulation de feux de forêt");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
