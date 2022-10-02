/**
 * 
 */
package tp1;

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

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			// create server-socket object for sever-side
			ServerSocket server = new ServerSocket(1234);

			// create socket object for opening connection
			Socket socket = server.accept();

			// create data-input-stream object
			DataInputStream dis = new DataInputStream(socket.getInputStream());

			// read data-output-stream object
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			// Key words in connection
			String msg_1 = "";
			Random rand = new Random();

			while (!msg_1.equals("stop")) {
				msg_1 = dis.readUTF().toLowerCase();
				if(msg_1.equals("service-temp")) {
					System.out.println("Client : " + msg_1);
					String temp = String.valueOf(rand.nextInt(50)) + " C�";
					dos.writeUTF(temp);
					dos.flush();
				}else {
					if(!msg_1.equals("stop")) {
						dos.writeUTF("Opps!, The service is not available.");
						dos.flush();
					}else {
						dos.writeUTF("Opps!, Server is stop running.");
						dos.flush();
					}
				}
			}

			dis.close();
			dos.close();
			socket.close();
			server.close();

		} catch (IOException e) {
			System.err.println("# IO Exception : " + e.getMessage());
		}

	}

}
