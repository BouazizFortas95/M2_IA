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
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * @author Bouaziz Fortas
 *
 */
public class PublisherController implements Initializable {

	@FXML
	private Button btn_broker_connect;

	@FXML
	private HBox hb_pub_status;

	@FXML
	private VBox vb_service_manager;

	@FXML
	private Label lab_pub;

	@FXML
	private ListView<String> lv_logs_pub;

	@FXML
	private Spinner<Integer> sp_broker_connect_port, sp_temp, sp_hum;

	@FXML
	private RadioButton rb_auto_temp, rb_auto_hum;

	@FXML
	private TextField tf_broker_host;

	private final int brokerPort = 1234;
	private InetAddress HOST;
	protected boolean pubIsConnect = false;
	private Socket socket;

	@FXML
	void connect2BrokerBTNPushed(ActionEvent event) {
		Runnable task = new Runnable() {
			@Override
			public void run() {
				try {
					if (!pubIsConnect) {
						socket = new Socket(HOST, sp_broker_connect_port.getValue());
						if (socket.isConnected()) {
							setMsg2BoitPub("Publisher [" + socket + "] is Connected Successful!");
							settPublisherStatus("-fx-background-color: #20C997;", true, true, "Disconnect");
							pubIsConnect = true;
							setServiceManagerStatus(!pubIsConnect);
							setSubId(socket.getLocalPort());
						}
					} else {
						socket.close();
						if (socket.isClosed()) {
							setMsg2BoitPub("Publisher [" + socket + "] is Disconnected Successful!");
							settPublisherStatus("-fx-background-color: light;", false, false, "Connect");
							pubIsConnect = false;
							setServiceManagerStatus(!pubIsConnect);
							removeSubId();
						}
					}

				} catch (IOException e) {
					System.err.println("Publisher Connection Error : " + e.getMessage());
					System.exit(1);
				}
			}

			private void removeSubId() {
				Platform.runLater(() -> {
					lab_pub.setText("Publisher");
				});
			}

			/**
			 * 
			 */
			private void setSubId(int id) {
				Platform.runLater(() -> {
					lab_pub.setText("Publisher-" + id);
				});
			}
		};
		new Thread(task).start();
	}

	protected void setServiceManagerStatus(boolean smStatus) {
		Platform.runLater(() -> {
			vb_service_manager.setDisable(smStatus);
		});
	}

	protected void settPublisherStatus(String background, boolean hostStatus, boolean portStatus, String label) {
		Platform.runLater(() -> {
			hb_pub_status.setStyle(background);
			tf_broker_host.setDisable(hostStatus);
			sp_broker_connect_port.setDisable(portStatus);
			btn_broker_connect.setText(label);
			vb_service_manager.setDisable(!portStatus);
		});
	}

	protected void setMsg2BoitPub(String msg) {
		Platform.runLater(() -> {
			lv_logs_pub.getItems().add(msg);
		});
	}

	@FXML
	void updateTempoBTNPushed(ActionEvent event) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				try {
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

					if (autoTempIsSelected()) {
						String serviceName = "service-auto-temp-pub";
						dos.writeUTF(serviceName);
						dos.flush();
						int tempValue = dis.readInt();
						showData("New Temporary is : " + tempValue + " °C");
					} else {
						int tempValue = sp_temp.getValue();
						String serviceName = "service-manu-pub";
						dos.writeUTF(serviceName);
						dos.flush();
						if (dis.readInt() == 200) {
							Weather w = HandelPS.readDataFromJsonFile();
							w.setTempo(tempValue);
							HandelPS.saveDataToJsonFile(w);
							showData("New Temporary : " + tempValue + " °C updated successfull");
						}
					}

				} catch (IOException io) {
					setMsg2BoitPub("#ERROR > " + io.getMessage());
				}
			}

			private void showData(String data) {
				Platform.runLater(() -> {
					lv_logs_pub.getItems().add(data);
				});
			}

		};
		new Thread(task).start();
	}

	@FXML
	void updateHumBTNPushed(ActionEvent event) {
		Runnable task = new Runnable() {

			@Override
			public void run() {

				try {
					DataInputStream dis = new DataInputStream(socket.getInputStream());
					DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					
					if (autoHumIsSelected()) {
						String serviceName = "service-auto-hum-pub";
						dos.writeUTF(serviceName);
						dos.flush();
						int humValue = dis.readInt();
						showData("New Humidity is : " + humValue + " %");
					} else {
						int humValue = sp_hum.getValue();
						String serviceName = "service-manu-pub";
						dos.writeUTF(serviceName);
						dos.flush();
						if (dis.readInt() == 200) {
							Weather w = HandelPS.readDataFromJsonFile();
							w.setHum(humValue);
							HandelPS.saveDataToJsonFile(w);
							showData("New Humidity : " + humValue + " % updated successfull");
						}
					}
				} catch (IOException io) {
					setMsg2BoitPub("#ERROR > " + io.getMessage());
				}
			}

			private void showData(String data) {
				Platform.runLater(() -> {
					lv_logs_pub.getItems().add(data);
				});
			}

		};
		new Thread(task).start();
	}

	private void setAutoTempStatus() {
		sp_temp.setDisable(autoTempIsSelected());
	}

	private boolean autoTempIsSelected() {
		return rb_auto_temp.isSelected();
	}

	private void setAutoHumStatus() {
		sp_hum.setDisable(autoHumIsSelected());
	}

	private boolean autoHumIsSelected() {
		return rb_auto_hum.isSelected();
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		sp_broker_connect_port
				.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, brokerPort));
		sp_temp.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(-50, 50, 0));
		sp_hum.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 50));

		rb_auto_temp.setOnMouseClicked(ev -> setAutoTempStatus());
		rb_auto_hum.setOnMouseClicked(ev -> setAutoHumStatus());

		vb_service_manager.setDisable(true);
		try {
			HOST = InetAddress.getLocalHost();
			tf_broker_host.setText(HOST.getHostAddress());
			lab_pub.setText(lab_pub.getText() + "-");
		} catch (UnknownHostException e) {
			System.err.println();
		}
	}

}
