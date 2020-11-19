package tcp;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TCPTimeClient {
	public static void main(String args[]) {
		try {
			Socket client = new Socket("localhost", 8000);
			DataInputStream dis = new DataInputStream(client.getInputStream());
			String time = dis.readUTF();
			System.out.println(time);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}