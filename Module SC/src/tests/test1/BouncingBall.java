/**
 * 
 */
package tests.test1;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * @author Bouaziz Fortas
 *
 */
public class BouncingBall extends Application {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		RadialGradient g = new RadialGradient(0, 0, 0.35, 0.35, 0.5, true, CycleMethod.REFLECT,
				new Stop(0.0, Color.WHITE), new Stop(1.0, Color.RED));

		Circle ball_1 = new Circle(0, 0, 30);
		Circle ball_2 = new Circle(0, 0, 30);
		ball_1.setFill(g);
		ball_2.setFill(g);
		Group root = new Group();
		root.getChildren().addAll(ball_1, ball_2);
		Scene scene = new Scene(root, 600, 600);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Bouncing Ball");
		primaryStage.show();
		TranslateTransition t1 = new TranslateTransition(Duration.millis(2000), ball_1);
		t1.setFromX(ball_1.getRadius());
		t1.setToX(scene.getWidth() - ball_1.getRadius());
		t1.setFromY(scene.getHeight() / 3);
		t1.setToY(scene.getHeight() / 3);
		t1.setCycleCount(Transition.INDEFINITE);
		t1.setAutoReverse(true);
		t1.setInterpolator(Interpolator.LINEAR);
		t1.play();
		
		
		TranslateTransition t2 = new TranslateTransition(Duration.millis(1500), ball_2);
		t2.setFromX(ball_2.getRadius());
		t2.setToX(scene.getWidth() - ball_2.getRadius());
		t2.setFromY(scene.getHeight() / 3*2);
		t2.setToY(scene.getHeight() / 3*2);
		t2.setCycleCount(Transition.INDEFINITE);
		t2.setAutoReverse(true);
		t2.setInterpolator(Interpolator.LINEAR);
		t2.play();
	}

}
