/**
 * 
 */
package tp1;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author Bouaziz Fortas
 *
 */
public class Client {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			Socket socket = new Socket("localhost", 1234);
			
			DataInputStream dis = new DataInputStream(socket.getInputStream());
			DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			String msg_1 = "", msg_2 = "";
			
			while (!msg_1.equals("stop")) {
				msg_1 = bufferedReader.readLine();
				dos.writeUTF(msg_1);
				dos.flush();
				msg_2 = dis.readUTF();
				System.out.println("Server : "+msg_2);
			}
			dos.close();
			socket.close();
		} catch (UnknownHostException e) {
			System.err.println("# Unknown Host Exception : "+e.getMessage());
		} catch (IOException e) {
			System.err.println("# IO Exception : "+e.getMessage());
		}

	}

}
