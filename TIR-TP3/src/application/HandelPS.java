package application;

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

public class HandelPS extends Thread {

	private Socket ps;
	private DataInputStream dis;
	private DataOutputStream dos;
	private String serviceName;
	public static final String DATAFILE = "weathers.json";
	private static Weather weather;
	
	public HandelPS(ServerSocket broker) {
		try {
			this.ps = broker.accept();
			this.dis = new DataInputStream(this.ps.getInputStream());
			this.dos = new DataOutputStream(this.ps.getOutputStream());
			setServiceName("");
			setWeather(new Weather(readDataFromJsonFile().getTempo(), readDataFromJsonFile().getHum()));
		} catch (IOException e) {
			System.err.println("#HandelPS Error: " + e.getMessage());
			System.exit(1);
		}
	}

	public static void saveDataToJsonFile(Weather w) {
		try {
			// create Gson instance
			Gson gson = new Gson();

			// create a writer
			Writer writer = Files.newBufferedWriter(Paths.get(DATAFILE));

			// convert book object to JSON file
			gson.toJson(w, writer);

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

				if (getServiceName().equals("service-auto-temp-pub")) {
					int temp = rand.nextInt(50);
					this.dos.writeInt(temp);
					this.dos.flush();
					saveDataToJsonFile(new Weather(temp, readDataFromJsonFile().getHum()));
				}
				
				if (getServiceName().equals("service-auto-hum-pub")) {
					int hum = rand.nextInt(50);
					this.dos.writeInt(hum);
					this.dos.flush();
					saveDataToJsonFile(new Weather(readDataFromJsonFile().getTempo(), hum));
				}
				
				if (getServiceName().equals("service-manu-pub")) {
					this.dos.writeInt(200);
					this.dos.flush();
				}
				
				if (getServiceName().equals("service-temporary-sub")) {
					int tempo = readDataFromJsonFile().getTempo();
					this.dos.writeInt(tempo);
					this.dos.flush();
				}
				if (getServiceName().equals("service-humidity-sub")) {
					int hum = readDataFromJsonFile().getHum();
					this.dos.writeInt(hum);
					this.dos.flush();
				}
				
				setServiceName("");
			} while (!getServiceName().equals("stop"));

			this.dos.writeUTF("Opps!, The broker is not available now!.");
			this.dos.flush();

			this.ps.close();
		} catch (IOException e) {
			System.err.println("#RunThread IO Exception Error: " + e.getMessage());
//			System.exit(1);
		}

	}

	public static Weather readDataFromJsonFile() {
		try {
			// create Gson instance
			Gson gson = new Gson();

			// create a reader
			Reader reader = Files.newBufferedReader(Paths.get(DATAFILE));

			// convert a JSON string to a User object
			setWeather(gson.fromJson(reader, Weather.class));;

			// close reader
			reader.close();

		} catch (Exception ex) {
			System.err.println("#READ_DATA_FROM_JSON_FILE_ERROR : "+ex.getMessage());
		}
		return getWeather();
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

	/**
	 * @return the weather
	 */
	public static Weather getWeather() {
		return weather;
	}

	/**
	 * @param weather the weather to set
	 */
	public static void setWeather(Weather weather) {
		HandelPS.weather = weather;
	}

}
