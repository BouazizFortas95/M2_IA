package tp2_;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class HandelClient extends Thread{

	private Socket client;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public HandelClient(ServerSocket server) {
		try {
			this.client = server.accept();

			dis = new DataInputStream(this.client.getInputStream());
			dos = new DataOutputStream(this.client.getOutputStream());

		} catch (IOException e) {
			System.err.println("#HandelClient Error: "+e.getMessage());
			System.exit(1);
		}
	}
	
	
	@Override
	public void run() {
		super.run();
		

		try {
			Random rand = new Random();
			

			String serviceName = "";

			while (!serviceName.equals("stop")) {
				serviceName = dis.readUTF().toLowerCase();
				System.out.println("Client : " + serviceName);
				if(serviceName.equals("service-temp")) {
					String temp = String.valueOf(rand.nextInt(50)) + " C°";
					dos.writeUTF(temp);
					dos.flush();
				}else {
//				if(!msg.equals("stop")) {
//					dos.writeUTF("Opps!, This service is not available now!.");
//					dos.flush();
//				}else {
						dos.writeUTF("Opps!, Server is stop running.");
						dos.flush();
//				}
				}
			}
			dos.writeUTF("Opps!, This service is not available now!.");
			dos.flush();

			dis.close();
			dos.close();
			this.client.close();
			this.client.close();
		} catch (IOException e) {
			System.err.println("#RunThread IO Exception Error: "+e.getMessage());
			System.exit(1);
		}

	
		
	}
	
}
