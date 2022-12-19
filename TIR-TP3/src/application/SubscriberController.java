/**
 * 
 */
package application;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

/**
 * @author Bouaziz Fortas
 *
 */
public class SubscriberController implements Initializable {

	@FXML
	private HBox hb_data_viewer;

	@FXML
	private Button btn_broker_connect, btn_follow, btn_unfollow, btn_get_data;

	@FXML
	private TextField tf_broker_host;

	@FXML
	private Label lab_sub;

	@FXML
	private HBox hb_sub_status;

	@FXML
	private ListView<String> lv_logs_logs, lv_topics_avail, lv_topics_folloing, lv_updated_data;

	@FXML
	private Spinner<Integer> sp_broker_connect_port;

	private final int brokerPort = 1234;
	private InetAddress HOST;
	protected boolean subIsConnect = false;
	private Socket socket;

	@FXML
	void followBTNPushed(ActionEvent event) {
		try {
			String item = lv_topics_avail.getSelectionModel().getSelectedItem();
			if (!item.equals(null)) {
				lv_topics_avail.getItems().remove(item);
				lv_topics_folloing.getItems().add(item);
			}
		} catch (Exception e) {
			System.err.println("Follow Error : " + e.getMessage());
		}
	}

	@FXML
	void get_dataBTNPushed(ActionEvent event) {
		for (String serviceName : lv_topics_folloing.getItems()) {
			getServiceByName(serviceName);
//			System.out.println("service Name : "+serviceName);
		}
	}

	@FXML
	void unfollowBTNPushed(ActionEvent event) {
		try {
			String item = lv_topics_folloing.getSelectionModel().getSelectedItem();
			if (!item.equals(null)) {
				lv_topics_folloing.getItems().remove(item);
				lv_topics_avail.getItems().add(item);
			}
		} catch (Exception e) {
			System.err.println("Un Follow Error : " + e.getMessage());
		}
	}

	@FXML
	void connect2BrokerBTNPushed(ActionEvent event) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					if (!subIsConnect) {
						socket = new Socket(HOST, sp_broker_connect_port.getValue());
						if (socket.isConnected()) {
							setMsg2BoitSub("Subscriber [" + socket + "] is Connected Successful!");
							settSubscriberStatus("-fx-background-color: #20C997;", true, true, "Disconnect");
							subIsConnect = true;
							setSubscriberServicesStatus(!subIsConnect);
							setSubId(socket.getLocalPort());
						}
					} else {
						socket.close();
						if (socket.isClosed()) {
							setMsg2BoitSub("Client [" + socket + "] is Disconnected Successful!");
							settSubscriberStatus("-fx-background-color: light;", false, false, "Connect");
							subIsConnect = false;
							setSubscriberServicesStatus(subIsConnect);
							removeSubId();
						}
					}

				} catch (IOException e) {
					System.err.println("Client Connection Error : " + e.getMessage());
					System.exit(1);
				}
			}

			private void removeSubId() {
				Platform.runLater(() -> {
					lab_sub.setText("Subsriber");
					lv_topics_avail.getItems().clear();
					lv_topics_folloing.getItems().clear();
					lv_updated_data.getItems().clear();
				});
			}

			/**
			 * 
			 */
			private void setSubId(int id) {
				Platform.runLater(() -> {
					lab_sub.setText("Subsriber-" + id);
					lv_topics_avail.getItems().add("Humidity");
					lv_topics_avail.getItems().add("Temporary");
				});
			}
		};
		new Thread(task).start();
	}

	protected void setSubscriberServicesStatus(boolean serviceStatus) {
		Platform.runLater(() -> {
			hb_sub_status.setDisable(serviceStatus);
		});
	}

	protected void settSubscriberStatus(String background, boolean hostStatus, boolean portStatus, String label) {
		Platform.runLater(() -> {
			hb_sub_status.setStyle(background);
			tf_broker_host.setDisable(hostStatus);
			sp_broker_connect_port.setDisable(portStatus);
			btn_broker_connect.setText(label);
			hb_data_viewer.setDisable(!portStatus);
		});
	}

	protected void setMsg2BoitSub(String msg) {
		Platform.runLater(() -> {
			lv_logs_logs.getItems().add(msg);
		});
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sp_broker_connect_port
				.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, brokerPort));

		try {
			HOST = InetAddress.getLocalHost();
			tf_broker_host.setText(HOST.getHostAddress());
			lab_sub.setText(lab_sub.getText() + "-");
		} catch (UnknownHostException e) {
			System.err.println();
		}

	}

	private void getServiceByName(String serviceName) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				DataInputStream dis = null;
				DataOutputStream dos = null;
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());

					dos.writeUTF("service-"+serviceName.toLowerCase().toString()+"-sub");
					dos.flush();

					int serviceValue = dis.readInt();
					if (serviceName.equals("Temporary"))
						showData("Broker Temporary is : " + serviceValue + " °C");

					if (serviceName.equals("Humidity"))
						showData("Broker Humidity is : " + serviceValue + " %");

				} catch (IOException io) {
					setMsg2BoitSub("#ERROR > " + io.getMessage());
				}
			}

			private void showData(String data) {
				Platform.runLater(() -> {
					lv_updated_data.getItems().add(data);
				});
			}

		};
		new Thread(task).start();
	}
}
