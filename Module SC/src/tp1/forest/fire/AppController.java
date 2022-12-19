package tp1.forest.fire;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;

/**
 * @author Bouaziz Fortas
 *
 */
public class AppController implements Initializable {

	@FXML
	private Button btn_clear;

	@FXML
	private Button btn_generate;

	@FXML
	private Button btn_pause;

	@FXML
	private Button btn_start;

	@FXML
	private Canvas canvas;

	@FXML
	private Slider slider_density;

	private final int DIM = 1, WIDTH = 700, HIEGHT = 700;
	private boolean forestOnFire;
	Square[][] squares = new Square[WIDTH][HIEGHT];
	private GraphicsContext gc;

	@FXML
	void generateNewForest(ActionEvent event) {
		clear(gc);
		initForest(slider_density.getValue());
	}

	@FXML
	void startFireInForest(ActionEvent event) {
		setForestOnFire(true);
		Runnable task = () -> {
			do {
				for (int x = 0; x < squares.length - 1; x++) {
					for (int y = 0; y < squares.length - 1; y++) {
//												System.out.println("Pos(X, Y)= {\n[X: " + x + ", Y: " + y + "],\n" + "[X: " + x
//														+ ", Y-1: " + (y - 1) + "],\n" + "[X+1: " + (x + 1) + ", Y: " + y + "],\n"
//														+ "[X: " + x + ", Y+1: " + (y + 1) + "],\n" + "[X-1: " + (x - 1) + ", Y: " + y
//														+ "],\n}");

//						try {
//							Thread.sleep(1);
						propagation(x, y);
//
//						} catch (InterruptedException e) {
//							System.err.println("Thread Exception : "+e.getMessage());
//						}
					}
				}
			} while (isForestOnFire());
		};
		new Thread(task).start();

	}

	private void propagation(int x, int y) {
//		try {
		if (x == 0) {
			if (y == 0) {

				boolean bool = (squares[x + 1][y].getType().equals(TypeSquare.FIRE)
						|| squares[x][y + 1].getType().equals(TypeSquare.FIRE));
				if (bool)
					selectPosition(x, y, bool, TypeSquare.FIRE);
				else
					selectPosition(x, y, bool, TypeSquare.ASH);
			}
			if (y == HIEGHT) {
				boolean bool = (squares[x][y - 1].getType().equals(TypeSquare.FIRE)
						|| squares[x + 1][y].getType().equals(TypeSquare.FIRE));

				if (bool)
					selectPosition(x, y, bool, TypeSquare.FIRE);
				else
					selectPosition(x, y, bool, TypeSquare.ASH);
			}
			if ((0 < y) && (y < HIEGHT)) {
				boolean bool = (squares[x][y - 1].getType().equals(TypeSquare.FIRE)
						|| squares[x][y].getType().equals(TypeSquare.FIRE)
						|| squares[x][y + 1].getType().equals(TypeSquare.FIRE));
				if (bool)
					selectPosition(x, y, bool, TypeSquare.FIRE);
				else
					selectPosition(x, y, bool, TypeSquare.ASH);
			}
		}
		if (x == WIDTH) {
			if (y == 0) {
				boolean bool = (squares[x][y + 1].getType().equals(TypeSquare.FIRE)
						|| squares[x - 1][y].getType().equals(TypeSquare.FIRE));
				if (bool)
					selectPosition(x, y, bool, TypeSquare.FIRE);
				else
					selectPosition(x, y, bool, TypeSquare.ASH);
			}
			if (y == HIEGHT) {
				boolean bool = (squares[x][y - 1].getType().equals(TypeSquare.FIRE)
						|| squares[x - 1][y].getType().equals(TypeSquare.FIRE));
				if (bool)
					selectPosition(x, y, bool, TypeSquare.FIRE);
				else
					selectPosition(x, y, bool, TypeSquare.ASH);
			}
			if ((0 < y) && (y < HIEGHT)) {
				boolean bool = (squares[x][y - 1].getType().equals(TypeSquare.FIRE)
						|| squares[x][y + 1].getType().equals(TypeSquare.FIRE)
						|| squares[x - 1][y].getType().equals(TypeSquare.FIRE));
				if (bool)
					selectPosition(x, y, bool, TypeSquare.FIRE);
				else
					selectPosition(x, y, bool, TypeSquare.ASH);
			}
		}
		if (((0 < x) && (x < WIDTH)) && ((0 < y) && (y < HIEGHT))) {
			boolean bool = (squares[x][y - 1].getType().equals(TypeSquare.FIRE)
					|| squares[x + 1][y].getType().equals(TypeSquare.FIRE)
					|| squares[x][y + 1].getType().equals(TypeSquare.FIRE)
					|| squares[x - 1][y].getType().equals(TypeSquare.FIRE));
			if (bool)
				selectPosition(x, y, bool, TypeSquare.FIRE);
			else
				selectPosition(x, y, bool, TypeSquare.ASH);

		}
//		} catch (Exception e) {
//			System.err.println("Runnable Exception : " + e.getMessage());
//		}

	}

	@FXML
	void clearForest(ActionEvent event) {
		clear(gc);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		gc = canvas.getGraphicsContext2D();
		slider_density.setValue(60);

		initForest(slider_density.getValue());
		canvas.setOnMouseClicked(
				e -> setStartPosition((int) Math.floor(e.getX() / DIM), (int) Math.floor(e.getY() / DIM)));
	}

	private void setStartPosition(int xPos, int yPos) {
		if (squares[xPos][yPos].getType().equals(TypeSquare.TREE)) {
			squareTransition(xPos, yPos, Color.ORANGERED, squares[xPos][yPos], TypeSquare.FIRE);
		}
	}

	private void selectPosition(int xPos, int yPos, boolean bool, TypeSquare ts) {

		if (bool && squares[xPos][yPos].getType().equals(TypeSquare.TREE) && ts.equals(TypeSquare.FIRE)) {
			squareTransition(xPos, yPos, Color.ORANGERED, squares[xPos][yPos], ts);
		}
		if (!bool && squares[xPos][yPos].getType().equals(TypeSquare.FIRE) && ts.equals(TypeSquare.ASH)) {
			squareTransition(xPos, yPos, Color.GRAY, squares[xPos][yPos], TypeSquare.ASH);
		}
		if (!bool && squares[xPos][yPos].getType().equals(TypeSquare.ASH)) {
			squareTransition(xPos, yPos, Color.WHITESMOKE, squares[xPos][yPos], TypeSquare.LAND);
		}

	}

	private void squareTransition(int x, int y, Color color, Square square, TypeSquare ts) {
		gc.setFill(color);
		gc.fillRect(square.getX(), square.getY(), DIM, DIM);
		square.setType(ts);
		squares[x][y] = square;
	}

	private void initForest(double density) {

		Random random = new Random();
		int upperbound = 100;
		Square square;

		for (int x = 0; x < WIDTH; x++) {
			for (int y = 0; y < HIEGHT; y++) {
				int posibilty = random.nextInt(upperbound);
				if (density >= posibilty) {
					gc.setFill(Color.GREENYELLOW);
					gc.fillRect(x * DIM, y * DIM, DIM, DIM);
					square = new Square(x * DIM, y * DIM, TypeSquare.TREE);
				} else {
					gc.setFill(Color.WHITESMOKE);
					gc.fillRect(x * DIM, y * DIM, DIM, DIM);
					square = new Square(x * DIM, y * DIM, TypeSquare.LAND);
				}
				squares[x][y] = square;
			}
		}
	}

	private void clear(GraphicsContext gc) {
		setForestOnFire(false);
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
	}

	/**
	 * @return the forestOnFire
	 */
	public boolean isForestOnFire() {
		return forestOnFire;
	}

	/**
	 * @param forestOnFire the forestOnFire to set
	 */
	public void setForestOnFire(boolean forestOnFire) {
		this.forestOnFire = forestOnFire;
	}
}
