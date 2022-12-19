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
public class HostAController implements Initializable {

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

	private int tempo = 0;

	@FXML
	void connectToServer(ActionEvent event) {
		Runnable task = () -> {
			try {
				String url = tf_url.getText().toLowerCase();
				if (url.equals("localhost")) {
					Socket socket = new Socket(url, client_port.getValue());
					if (socket.isConnected())
						connectionStatus("-fx-background-color: #20C997;", true, true, "DISCONNECT");

					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

					while (!tf_service.getText().equals("stop")) {
						btn_send.setOnMouseClicked(e -> {
							
							if (btn_connect.getText().equals("DISCONNECT")) {
								try {
									btnSendServiceName(socket, dis, dos);
								} catch (IOException io) {
									System.err.println("# IO Exception : " + io.getMessage());
								}
							} 
							
							if(btn_connect.getText().equals("CONNECT")) {
								setMsg2BoitClient("Opps!, Your are is not Connected.");
								try {
									socket.close();
								} catch (IOException e1) {
								}
							}
							
							
							
						});
					}

				} else {
					setMsg2BoitClient("Opps!, Please Enter Correct URL.");
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
	 * @param socket
	 * @param dis
	 * @param dos
	 * @throws IOException
	 */
	private void btnSendServiceName(Socket socket, DataInputStream dis, DataOutputStream dos)
			throws IOException {
		
		String output,  input = tf_service.getText().toLowerCase();;
		switch (input) {
		case "stop":
			socket.close();
			if (socket.isClosed()) {
				connectionStatus("-fx-background-color: #ffffff;", false, false, "CONNECT");
				tf_url.setText("");
				dos.writeUTF("stop");
				dos.flush();
				setMsg2BoitServer("STOP");
			} else {
				setMsg2BoitServer("Opps!, Socket Cilent service is not closed.");
			}
			break;
		case "service-temp":
			dos.writeInt(tempo);
			dos.flush();
			output = dis.readUTF();
			setMsg2BoitClient(output);
			break;
		case "":
			break;
		default:
			break;
		}
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

	private void connectionStatus(String background, boolean url, boolean port, String label) {
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
				Socket socket = server.accept();
				if (server.isBound())
					runStatus("-fx-background-color: #20C997;", true, "STOP");

				DataInputStream dis = new DataInputStream(socket.getInputStream());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				String input = "", output = "";

				while (!input.equals("stop")) {
					input = dis.readUTF().toLowerCase();
					setMsg2BoitServer(input);
					switch (input) {
					case "stop":
						server.close();
						if (server.isClosed())
							runStatus("-fx-background-color: #ffffff;", false, "RUN");
						else
							setMsg2BoitServer("Opps!, Socket Server service is not closed.");
						break;
					case "service-hum":
						setMsg2BoitServer(input);
						int temp = rand.nextInt(50);
						dos.writeInt(temp);
						dos.flush();
						break;
					default:
						break;
					}
					
					btn_run.setOnMouseClicked(e->{
						if (btn_run.getText().equals("RUN")) {
								runServer(event);
						} 
						
						if (btn_run.getText().equals("STOP")) {
							try {
								server.getChannel().socket().close();
								server.close();
								if (server.isClosed())
									runStatus("-fx-background-color: #ffffff;", false, "RUN");
								else
									setMsg2BoitServer("Opps!, Socket Server service is not closed.");
							} catch (IOException e1) {
								System.err.println("# IO Exception : " + e1.getMessage());
							}
						}
					});

				}
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
