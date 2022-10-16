/**
 * 
 */
package tp1_forest_fire;

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

	private final int DIM = 2, WIDTH = 350, HIEGHT = 350;
	private int xStart = -1, yStart = -1;
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

		Runnable task = new Runnable() {
			public void run() {
				do {
					for (int x = getxStart(); x < squares.length; x++) {
						for (int y = getyStart(); y < squares.length; y++) {

							try {
								Thread.sleep(1000);
								if (squares[x][y].getType().equals(TypeSquare.FIRE)) {
									propagation(x, y);
								}

							} catch (InterruptedException e) {
								System.out.println("Thread Error msg : " + e.getMessage());
							}

						}
					}

				} while (isForestOnFire());

			}

		};
		new Thread(task).start();

	}

	private void propagation(int x, int y) {
		try {
			if (x == 0) {
				if (y == 0) {
					selectPosition(x + 1, y);
					selectPosition(x, y + 1);
				} else if (y == HIEGHT - 1) {
					selectPosition(x, y - 1);
					selectPosition(x + 1, y);
				} else if ((0 < y) && (y < HIEGHT - 1)) {
					selectPosition(x, y - 1);
					selectPosition(x + 1, y);
					selectPosition(x, y + 1);
				}
			} else if (x == WIDTH - 1) {
				if (y == 0) {
					selectPosition(x, y + 1);
					selectPosition(x - 1, y);
				} else if (y == HIEGHT - 1) {
					selectPosition(x - 1, y);
					selectPosition(x, y - 1);
				} else if ((0 < y) && (y < HIEGHT - 1)) {
					selectPosition(x, y - 1);
					selectPosition(x, y + 1);
					selectPosition(x - 1, y);
				}
			} else if (((0 < x) && (x < WIDTH - 1)) && ((0 < y) && (y < HIEGHT - 1))) {
				selectPosition(x, y - 1);
				selectPosition(x + 1, y);
				selectPosition(x, y + 1);
				selectPosition(x - 1, y);
			}
		} catch (Exception e) {
			System.err.println("Runnable Exception : " + e.getMessage());
		}
	}

	@FXML
	void clearForest(ActionEvent event) {
		clear(gc);
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		gc = canvas.getGraphicsContext2D();
		slider_density.setValue(65);

		initForest(slider_density.getValue());
		canvas.setOnMouseClicked(e -> selectPosition(setxStart((int) Math.floor(e.getX() / DIM)),
				setyStart((int) Math.floor(e.getY() / DIM))));
	}

	private void selectPosition(int xPos, int yPos) {
		System.out.println("Pos [" + xPos + ", " + yPos + "]");

		if (squares[xPos][yPos].getType().equals(TypeSquare.TREE)) {
			squareTransition(xPos, yPos, Color.ORANGERED, squares[xPos][yPos], TypeSquare.FIRE);
//			if (isForestOnFire())
			propagation(xPos, yPos);
		} else if (squares[xPos][yPos].getType().equals(TypeSquare.FIRE)) {
			squareTransition(xPos, yPos, Color.GRAY, squares[xPos][yPos], TypeSquare.ASH);
//			if (isForestOnFire())
			propagation(xPos, yPos);
		} else if (squares[xPos][yPos].getType().equals(TypeSquare.ASH)) {
			squareTransition(xPos, yPos, Color.WHITESMOKE, squares[xPos][yPos], TypeSquare.LAND);
		}
		setxStart(xPos);
		setyStart(yPos);

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

	/**
	 * @return the xStart
	 */
	public int getxStart() {
		return xStart;
	}

	/**
	 * @param xStart the xStart to set
	 */
	public int setxStart(int xStart) {
		this.xStart = xStart;
		return xStart;
	}

	/**
	 * @return the yStart
	 */
	public int getyStart() {
		return yStart;
	}

	/**
	 * @param yStart the yStart to set
	 */
	public int setyStart(int yStart) {
		this.yStart = yStart;
		return yStart;
	}

}
