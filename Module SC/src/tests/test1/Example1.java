package tests.test1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Example1 extends Application {
	private final double RECT_X = 200;
	private final double RECT_Y = 200;
	private final double SCENE_X = 600;
	private final double SCENE_Y = 600;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Group root = new Group();

		Rectangle r = new Rectangle((SCENE_X - RECT_X) / 2, (SCENE_Y - RECT_Y) / 2, RECT_X, RECT_Y);
		r.setFill(Color.DARKGRAY);
		r.setStroke(Color.BLACK);
		r.setStrokeWidth(2);
		r.setOnZoom(e -> {
			r.setScaleX(r.getScaleX() * e.getZoomFactor());
			r.setScaleY(r.getScaleY() * e.getZoomFactor());
			e.consume();
		});

		r.setOnRotate(e -> {
			r.setRotate(r.getRotate() + e.getAngle());
			e.consume();
		});

		r.setOnScroll(e -> {
			if (!e.isInertia()) {
				double newX = r.getX() + e.getDeltaX();
				if ((newX >= 0) && (newX <= SCENE_X - r.getWidth())) {
					r.setX(newX);
				}
				double newY = r.getY() + e.getDeltaY();
				if ((newY >= 0) && (newY <= SCENE_Y - r.getHeight())) {
					r.setY(newY);
				}
			}
			e.consume();
		});
		r.setOnSwipeLeft(e -> {
			r.setX(0);
			e.consume();
		});
		r.setOnSwipeRight(e -> {
			r.setX(SCENE_X - r.getWidth());
			e.consume();
		});
		r.setOnSwipeUp(e -> {
			r.setY(0);
			e.consume();
		});
		r.setOnSwipeDown(e -> {
			r.setY(SCENE_Y - r.getHeight());
			e.consume();
		});
		
		root.getChildren().add(r);
		 Scene scene = new Scene(root, SCENE_X, SCENE_Y);
		 primaryStage.setTitle("The Gesturator");
		 primaryStage.setScene(scene);
		 primaryStage.show();
	}

}
