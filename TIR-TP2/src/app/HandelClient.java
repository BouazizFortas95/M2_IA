package app;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

import com.google.gson.Gson;

public class HandelClient extends Thread {

	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String serviceName;
	public static final String DATAFILE = "data.json";

	public HandelClient(ServerSocket server) {
		try {
			this.client = server.accept();
			this.dis = new DataInputStream(this.client.getInputStream());
			this.dos = new DataOutputStream(this.client.getOutputStream());
			setServiceName("");
		} catch (IOException e) {
			System.err.println("#HandelClient Error: " + e.getMessage());
			System.exit(1);
		}
	}

	public void savedDataToJsonFile(int tempo, int humi) {
		try {
			// create a map
			Weather weather = new Weather(tempo, humi);

			// create Gson instance
			Gson gson = new Gson();

			// create a writer
			Writer writer = Files.newBufferedWriter(Paths.get(DATAFILE));

			// convert book object to JSON file
			gson.toJson(weather, writer);

			// close the writer
			writer.close();

		} catch (Exception ex) {
			System.err.println("Error Save Data To Json File : " + ex.getMessage());
		}
	}

	@Override
	public void run() {
		super.run();

		try {
			Random rand = new Random();

			do {
				setServiceName(dis.readUTF().toLowerCase());

				if (getServiceName().equals("service-temp")) {
					int temp = rand.nextInt(50);
					this.dos.writeInt(temp);
					this.dos.flush();
					savedDataToJsonFile(temp, -1);
				}
				if (getServiceName().equals("service-temp-c")) {
					int tempo = readDataFromJsonFile().getTempo();
					this.dos.writeInt(tempo);
					this.dos.flush();
				}
				if (getServiceName().equals("service-hum")) {
					int hum = rand.nextInt(80);
					this.dos.writeInt(hum);
					this.dos.flush();
				}
				if (getServiceName().equals("stop")) {
					this.dos.writeUTF("OK!, Accepted your request to stop Connecting.");
					this.dos.flush();
				}
				setServiceName("");
			} while (!getServiceName().equals("stop"));

			this.dos.writeUTF("Opps!, The server is not available now!.");
			this.dos.flush();

			this.client.close();
		} catch (IOException e) {
			System.err.println("#RunThread IO Exception Error: " + e.getMessage());
//			System.exit(1);
		}

	}

	private Weather readDataFromJsonFile() {
		Weather w = null;
		try {
			// create Gson instance
			Gson gson = new Gson();

			// create a reader
			Reader reader = Files.newBufferedReader(Paths.get(DATAFILE));

			// convert a JSON string to a User object
			w = gson.fromJson(reader, Weather.class);

			// close reader
			reader.close();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return w;
	}

	/**
	 * @return the serviceName
	 */
	public String getServiceName() {
		return serviceName;
	}

	/**
	 * @param serviceName the serviceName to set
	 */
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
