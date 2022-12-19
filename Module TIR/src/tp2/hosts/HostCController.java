/**
 * 
 */
package tp2.hosts;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.ResourceBundle;

import javax.naming.ldap.SortKey;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * @author Bouaziz Fortas
 *
 */
public class HostCController implements Initializable {

	@FXML
	private HBox hbox_connect, hbox_server_run;

	@FXML
	private Button btn_run, btn_connect, btn_send;

	@FXML
	private Spinner<Integer> client_port;

	@FXML
	private Spinner<Integer> server_port;

	@FXML
	private ListView<String> list_client_log, list_server_log;

	@FXML
	private TextField tf_url, tf_service;

	@FXML
	private VBox vbox_server;

	@FXML
	void connectToServer(ActionEvent event) {
		Runnable task = () -> {
			try {
				String url = tf_url.getText().toLowerCase();
				if (url.equals("localhost")) {
					Socket socket = new Socket(url, client_port.getValue());

					conectionStatus("-fx-background-color: #20C997;", true, true, "DISCONNECT");

					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

					BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
					String msg_1 = "", msg_2 = "";

					while (!msg_1.equals("stop")) {
						msg_2 = dis.readUTF();
						setMsg2BoitClient(msg_2);
						System.out.println("Server : " + msg_2);
						if (msg_1.equals("stop")) {
							conectionStatus("-fx-background-color: #fff;", false, false, "CONNECT");
							setMsg2BoitClient(msg_2);
							dis.close();
							dos.close();
							socket.close();
						}
					}
				} else {
					System.out.println("Opps!!, Please Enter Correct URL");
				}
			} catch (UnknownHostException e) {
				System.err.println("# Unknown Host Exception : " + e.getMessage());
			} catch (IOException e) {
				System.err.println("# IO Exception : " + e.getMessage());
			}

		};
		new Thread(task).start();
	}

	/**
	 * @param msg_2
	 */
	private void setMsg2BoitClient(String msg_2) {
		Platform.runLater(() -> {
			list_client_log.getItems().add("Server : " + msg_2);
		});
	}

	private void getServiceFromServer(DataOutputStream dos, String msg_1) {
		try {
			msg_1 = tf_service.getText().toLowerCase();
			if (!msg_1.isEmpty()) {
				dos.writeUTF(msg_1);
				dos.flush();
				Platform.runLater(() -> {
					tf_service.setText("");
				});
			}
		} catch (IOException e) {
			System.err.println("#GET_SERVICE_FROM_SERVER ERROR: " + e.getMessage());
		}
	}

	private void conectionStatus(String background, boolean url, boolean port, String label) {
		Platform.runLater(() -> {
			hbox_connect.setStyle(background);
			tf_url.setDisable(url);
			client_port.setDisable(port);
			btn_connect.setText(label);
		});
	}

	@FXML
	void runServer(ActionEvent event) {
		Runnable task = () -> {
			try {

				Random rand = new Random();

				ServerSocket server = new ServerSocket(server_port.getValue());

				runStatus("-fx-background-color: #20C997;", true, "STOP");

				Socket socket = server.accept();

				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

				String msg = "";
				while (!msg.equals("stop")) {
					msg = dis.readUTF().toLowerCase();
					setMsg2BoitServer(msg);
					System.out.println("Client : " + msg);

					if (msg.equals("service-temp")) {
						String temp = String.valueOf(rand.nextInt(50)) + " C°";
						dos.writeUTF(temp);
						dos.flush();
					} else {
						dos.writeUTF("Opps!, This service is not available now!.");
						dos.flush();
					}
				}

				runStatus("-fx-background-color: white;", true, "RUN");
				dos.writeUTF("Opps!, Server is stop running.");
				dos.flush();
				dis.close();
				dos.close();
				socket.close();
				server.close();

			} catch (IOException e) {
				System.err.println("# IO Exception : " + e.getMessage());
			}

		};
		new Thread(task).start();

	}

	private void setMsg2BoitServer(String msg) {
		Platform.runLater(() -> {
			list_server_log.getItems().add("Client : " + msg);
		});
	}

	private void runStatus(String background, boolean port, String label) {
		Platform.runLater(() -> {
			hbox_server_run.setStyle(background);
			server_port.setDisable(port);
			btn_run.setText(label);
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		server_port.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, 1234));
		client_port.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, 1234));

	}

}
