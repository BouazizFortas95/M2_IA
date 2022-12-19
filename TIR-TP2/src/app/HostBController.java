package app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class HostBController implements Initializable {

	@FXML
	private Button btn_client_connect, btn_client_gs, btn_server_run;

	@FXML
	private ComboBox<String> cb_client_services;

	@FXML
	private HBox hb_client_logs, hb_server_status, hb_client_services;

	@FXML
	private ListView<String> lv_client_logs, lv_server_logs;

	@FXML
	private Spinner<Integer> sp_client_port, sp_server_port;

	@FXML
	private TextField tf_client_host;

	private final int serverPort = 1235, clientPort = 1234;
	private static ServerSocket server;
	private InetAddress HOST;
	private List<String> services_list = new ArrayList<String>();
	private boolean serverIsRun = false, clientIsConnect = false;
	private Socket socket;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sp_server_port.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, serverPort));
		sp_client_port.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, clientPort));

		// Initialize available services
		services_list.add("service-hum");
		services_list.add("stop");

		cb_client_services.getItems().addAll(services_list);

		try {
			HOST = InetAddress.getLocalHost();
			tf_client_host.setText(HOST.getHostAddress());
		} catch (UnknownHostException e) {
			System.err.println();
		}

		setClientServicesStatus(!clientIsConnect);

	}

	@FXML
	private void connectClient() {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					if (!clientIsConnect) {
						socket = new Socket(HOST, sp_client_port.getValue());
						if (socket.isConnected()) {
							setMsg2BoitClient("Client is Connected Successful!");
							settClientStatus("-fx-background-color: #20C997;", true, true, "Disconnect");
							clientIsConnect = true;
							setClientServicesStatus(!clientIsConnect);
						}
					} else {
						socket.close();
						if (socket.isClosed()) {
							setMsg2BoitClient("Client is Disconnected Successful!");
							settClientStatus("-fx-background-color: light;", false, false, "Connect");
							clientIsConnect = false;
							setClientServicesStatus(!clientIsConnect);
						}
					}

				} catch (IOException e) {
					System.err.println("Client Connection Error : " + e.getMessage());
					System.exit(1);
				}
			}
		};
		new Thread(task).start();
	}

	protected void settClientStatus(String background, boolean hostStatus, boolean portStatus, String label) {
		Platform.runLater(() -> {
			hb_client_logs.setStyle(background);
			tf_client_host.setDisable(hostStatus);
			sp_client_port.setDisable(portStatus);
			btn_client_connect.setText(label);
		});
	}

	/**
	 * @param msg
	 */
	protected void setMsg2BoitClient(String msg) {
		Platform.runLater(() -> {
			lv_client_logs.getItems().add(msg);
		});
	}

	@FXML
	private void getServiceByName(ActionEvent event) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				DataInputStream dis = null;
				DataOutputStream dos = null;
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
				} catch (IOException io) {
					setMsg2BoitClient("#ERROR > " + io.getMessage());
				}

				String serviceName = "";
				if (clientIsConnect) {
					if (!serviceName.equals("stop")) {

						try {
							serviceName = cb_client_services.getSelectionModel().getSelectedItem();
							dos.writeUTF(serviceName.toString());
							dos.flush();
							int serviceValue = dis.readInt();

							if (serviceName.equals("service-hum"))
								setMsg2BoitClient("Server : " + serviceValue + " °%");
							if (serviceName.equals("stop")) {
								clientIsConnect = false;
								settClientStatus("-fx-background-color: light;", false, false, "Connect");
								setMsg2BoitClient("Client is Disconnected Successful!");
								setClientServicesStatus(!clientIsConnect);
							}
						} catch (IOException e) {
							setMsg2BoitClient("#ERROR > " + e.getMessage());
						}
					}
				} else {

				}
			}

		};
		new Thread(task).start();
	}

	protected void setClientServicesStatus(boolean serviceStatus) {
		Platform.runLater(() -> {
			hb_client_services.setDisable(serviceStatus);
		});
	}

	@FXML
	private void runServer(ActionEvent event) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				if (serverIsRun) {
					setMsg2BoitServer("Opps!, Server B is Running and you can't stop it.");
				} else {
					try {
						server = new ServerSocket(sp_server_port.getValue());
						if (server.isBound()) {
							serverIsRun = true;
							setServerStatus("-fx-background-color: #20C997;", true, "Stop", true);
							setMsg2BoitServer("Server B is Running.");
						}
						do {
							HandelClient handelClient = new HandelClient(server);
							handelClient.start();

						} while (serverIsRun);
					} catch (IOException ex) {
						System.err.println("Server B Error : " + ex.getMessage());
						System.exit(1);
					}
				}

			}
		};
		new Thread(task).start();
	}

	private void setServerStatus(String background, boolean portStatus, String label, boolean btnStatus) {
		Platform.runLater(() -> {
			hb_server_status.setStyle(background);
			sp_server_port.setDisable(portStatus);
			btn_server_run.setText(label);
			btn_server_run.setDisable(btnStatus);
		});
	}

	/**
	 * @param msg
	 */
	public void setMsg2BoitServer(String msg) {
		Platform.runLater(() -> {
			lv_server_logs.getItems().add(msg);
		});
	}

}
