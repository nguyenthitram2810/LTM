package tcp;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TCPTimeServer {
	public static void main(String args[]) {
		try {
			ServerSocket ss = new ServerSocket(8000);
			System.out.println("Server is started");
			while(true) {
				Socket socket = ss.accept();
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				String time = new Date().toString();
				dos.writeUTF("Server tra lai ngay gio" + time);
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
