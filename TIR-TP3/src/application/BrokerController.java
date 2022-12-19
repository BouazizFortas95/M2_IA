package application;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URL;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class BrokerController implements Initializable {

	protected static boolean brokerIsRun = false;

	@FXML
	private Button btn_new_topic, btn_broker_run;

	@FXML
	private Spinner<Integer> sp_broker_port;

	@FXML
	private VBox vbox__content;

	@FXML
	private HBox hb_broker_status;

	@FXML
	private ComboBox<String> cb_topics_list;

	@FXML
	private ListView<String> lv_logs_broker;

//	protected Stage primaryStage;
	private final int brokerPort = 1234;

	protected ServerSocket broker;
	protected HandelPS handelPS;

	@FXML
	void runBrokerBTNPushed(ActionEvent event) {
		Runnable task = new Runnable() {

			@Override
			public void run() {
				if (brokerIsRun) {
					setMsg2BoitBroker("Opps!, Broker is Running and you can't stop it.");
				} else {
					try {
						broker = new ServerSocket(sp_broker_port.getValue());
						if (broker.isBound()) {
							brokerIsRun = true;
							setBrokerStatus("-fx-background-color: #20C997;", true, "Stop", true);
							setMsg2BoitBroker("Broker [ "+broker+" ] is Running.");
						}
						do {
							handelPS = new HandelPS(broker);
							handelPS.start();
						} while (brokerIsRun);
					} catch (IOException ex) {
						System.err.println("Server Error : " + ex.getMessage());
						System.exit(1);
					}
				}
			}
		};
		new Thread(task).start();
	}

	protected void setBrokerStatus(String background, boolean portStatus, String label, boolean btnStatus) {
		Platform.runLater(() -> {
			hb_broker_status.setStyle(background);
			sp_broker_port.setDisable(portStatus);
			btn_broker_run.setText(label);
			btn_broker_run.setDisable(btnStatus);
		});
	}

	protected void setMsg2BoitBroker(String msg) {
		Platform.runLater(() -> {
			lv_logs_broker.getItems().add(msg);
		});
	}
//	
//	@FXML
//	void newTopicBTNPushed(ActionEvent event) throws IOException {
//
//        FXMLLoader loader = new FXMLLoader();
//        loader.setLocation(getClass().getResource("TopicsTable.fxml"));
//        loader.load();
//        Parent root = loader.getRoot();
//        
//        Stage modal_dialog = new Stage(StageStyle.DECORATED);
//        modal_dialog.initModality(Modality.WINDOW_MODAL);
//        modal_dialog.initOwner(getPrimaryStage());
//        modal_dialog.setResizable(false);
//        Scene scene = new Scene(root);
//        
//        TopicsTableController ttc = new TopicsTableController();
//        ttc.setParentStage(modal_dialog);
//        modal_dialog.setScene(scene);
//        modal_dialog.setTitle("Topics Table Viewer");
//        modal_dialog.show();
//	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		sp_broker_port.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1024, 49151, brokerPort));

//		try {
////			Weather.initWeatherToJson();
//		} catch (IOException e) {
//			System.err.println("ErrorInitWeatherList");
//		}
	}

//	/**
//	 * @return the primaryStage
//	 */
//	public Stage getPrimaryStage() {
//		return (Stage) staockpane.getScene().getWindow();
//	}

}
