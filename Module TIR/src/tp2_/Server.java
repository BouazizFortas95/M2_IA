/**
 * 
 */
package tp2_;

import java.io.BufferedReader;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

/**
 * @author Bouaziz Fortas
 *
 */
public class Server {
	private static ServerSocket server;
	private static final int PORT = 1234;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			server = new ServerSocket(PORT);
			do {
				HandelClient handelClient = new HandelClient(server);
				handelClient.start();
			} while (true);
		} catch (IOException e) {
			System.err.println("Server Error : " + e.getMessage());
		}

	}

}
