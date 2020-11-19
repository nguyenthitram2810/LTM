package xuLyChuoiUDP;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Date;

public class Server {
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
	
	public String chuoiThuong(String chuoi) {
		String temp = "";
		for(int i = 0; i < chuoi.length(); i++) {
			if(chuoi.charAt(i) >= 'A' && chuoi.charAt(i) <= 'Z') {
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
		String chuoiThuong = chuoiThuong(chuoi);
		for(int i = 0; i < chuoi.length(); i++) {
			char original = chuoi.charAt(i);
			char ch = chuoiThuong.charAt(i);
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
	
	public static void main(String args[]) throws Exception {
		Server sv = new Server();
		DatagramSocket serverSocket = new DatagramSocket(9876);
		System.out.println("Server is started");
		byte[] receiveData = new byte[1024];
		byte[] sendData = new byte[1024];
		while(true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String data = new String(receivePacket.getData());
			data = data.trim();
			String returnData = "Chuoi hoa: " + sv.chuoiHoa(data) + "\n Chuoi thuong: " + sv.chuoiThuong(data) 
								+ "\n Chuoi vua hoa vua thuong: " +  sv.chuoiHoaThuong(data) + "\n So tu: " + sv.demTu(data)
								+ "\n Nguyen am: " + sv.demNguyenAm(data);
			sendData = returnData.getBytes();
			System.out.println(sendData.length);
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
		}
	}
}
