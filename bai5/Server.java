package bai5;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class Server extends Thread {
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	private static String DB_URL = "jdbc:mysql://localhost:3306/QlyHocVien";
    private static String USER_NAME = "root";
    private static String PASSWORD = "";

    public String getData() throws Exception {
    	try {
    		String columnName = "";
    		String returnData = "";
            // connnect to database 'testdb'
            Connection conn = getConnection(DB_URL, USER_NAME, PASSWORD);
            // crate statement
            Statement stmt = conn.createStatement();
            // get data from table 'student'
            ResultSet rs = stmt.executeQuery("select * from HOCVIEN");
            ResultSetMetaData rsmd = rs.getMetaData();
            // show data
            int col = rsmd.getColumnCount();
            for (int i = 1; i <= col; i++) {
            	columnName += rsmd.getColumnLabel(i) + " ";
            }
            returnData += columnName;
            while (rs.next()) {
            	returnData += "\n";
            	for(int j = 1; j <= col; j++) {
                    System.out.println(returnData);
            		returnData += rs.getObject(j) + " ";
            	}
            }
            // close connection
            conn.close();
            System.out.println(returnData);
            return returnData;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static Connection getConnection(String dbURL, String userName, 
            String password) {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(dbURL, userName, password);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return conn;
    }
	
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
				System.out.println("From: "+ socket);
				String ReturnData = getData();
				dos.writeUTF(ReturnData);
			}
		}
		catch(Exception e) {
			
		}
		finally {
			try {socket.close(); } catch(IOException e) {}
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
