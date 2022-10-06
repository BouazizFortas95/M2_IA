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

			Random rand = new Random();
			
			ServerSocket server = new ServerSocket(1234);

			Socket socket = server.accept();

			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());

			String msg = "";

			while (!msg.equals("stop")) {
				msg = dis.readUTF().toLowerCase();
				System.out.println("Client : " + msg);
				if(msg.equals("service-temp")) {
					String temp = String.valueOf(rand.nextInt(50)) + " C°";
					dos.writeUTF(temp);
					dos.flush();
				}else {
					if(!msg.equals("stop")) {
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
