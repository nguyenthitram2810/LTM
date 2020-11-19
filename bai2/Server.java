package bai2;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public Server(Socket s) throws Exception {
		socket = s;
		System.out.println("Serving: " + socket);
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
		start();
	}
	
	public void run() {
		try {
			while(true) {
				System.out.println("....Server is waiting...");
				String str = dis.readUTF();
				if (str.equals("exit") ) break;
				System.out.println("Received: " + str);
				System.out.println("From: "+ socket);
				String ReturnData = "Chuoi Hoa: " + chuoiHoa(str) + "\nChuoi Hoa Thuong: " + chuoiHoaThuong(str) + "\nSo tu: " + demTu(str) + "\nCac nguyen am trong chuoi da cho: " + demNguyenAm(str);
				dos.writeUTF(ReturnData);
			}
			System.out.println("Disconnected with.."+socket);
		}
		catch(Exception e) {
			
		}
		finally {
			try {socket.close(); } catch(IOException e) {}
		}
	}
	
	public String chuoiHoa(String chuoi) {
		String temp = "";
		for(int i = 0; i < chuoi.length(); i++) {
			if(chuoi.charAt(i) >= 'a' && chuoi.charAt(i) <= 'z') {
				temp += (char) (chuoi.charAt(i) - 32);
			}
			else {
				temp += (char) chuoi.charAt(i);
			}
		}
		return temp;
	}
	
	public String chuoiHoaThuong(String chuoi) {
		String temp = "";
		for(int i = 0; i < chuoi.length(); i++) {
			if(chuoi.charAt(i) >= 'a' && chuoi.charAt(i) <= 'z') {
				temp += (char) (chuoi.charAt(i) - 32);
			}
			else if(chuoi.charAt(i) >= 'A' && chuoi.charAt(i) <= 'Z') {
				temp += (char) (chuoi.charAt(i) + 32);
			}
			else {
				temp += (char) chuoi.charAt(i);
			}
		}
		return temp;
	}
	
	public int demTu(String chuoi) {
		if (chuoi == null) {
            return -1;
        }
        int count = 0;
        int size = chuoi.length();
        boolean notCounted = true;
        for (int i = 0; i < size; i++) {
            if (chuoi.charAt(i) != ' ' && chuoi.charAt(i) != '\t' 
                    && chuoi.charAt(i) != '\n') {
                if(notCounted) {
                    count++;
                    notCounted = false;
                }
            } else {
                notCounted = true;
            }
        }
        return count;
	}
	
	public String demNguyenAm(String chuoi) {
		String temp = "";
		for(int i = 0; i < chuoi.length(); i++) {
			char original = chuoi.charAt(i);
			char ch = chuoiThuong(chuoi.charAt(i));
			if(ch == 'u' || ch == 'e' || ch == 'o' || ch == 'a' || ch == 'i' || ch == ' ') {
				temp += original;
			}
		}
		temp += demTanSuatNguyenAm(temp);
		return temp;
	}
	
	public String demTanSuatNguyenAm(String chuoi) {
		String temp = "";
		int counter[] = new int[256];
        int len = chuoi.length();
        for (int i = 0; i < len; i++)
            counter[chuoi.charAt(i)]++;
        char array[] = new char[chuoi.length()];
        for (int i = 0; i < len; i++) {
            if(chuoi.charAt(i) == ' ') continue;
            array[i] = chuoi.charAt(i);
            int flag = 0;
            for (int j = 0; j <= i; j++) {
                if (chuoi.charAt(i) == array[j])
                    flag++;
            }
            if (flag == 1)
            	temp += "\nSố lần xuất hiện của " + chuoi.charAt(i) + " trong chuỗi:" + counter[chuoi.charAt(i)];
        }
        return temp;
	}
	public char chuoiThuong(char chuoi) {
		if(chuoi >= 'A' && chuoi <= 'Z') {
			return (char) (chuoi + 32);
		}
		else {
			return chuoi;
		}
	}
	public static void main(String args[]) throws Exception {
		ServerSocket s = new ServerSocket(8000);
		InetAddress addrs = InetAddress.getLocalHost();
		System.out.println("TCP/Server running on: " + addrs + ", Port: " + s.getLocalPort());
		try {
			while(true) {
				Socket socket = s.accept();
				try {
					new Server(socket);
				}
				catch(IOException e) {
					socket.close();
				}
			}
		}
		finally {
			s.close();
		}
	}
}
