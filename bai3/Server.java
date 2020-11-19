package bai3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import bai3.Client.ThreadGoiTinNhan;

import javax.swing.JTextField;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

public class Server extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSend; 
	private List list;
	
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public class ThreadGoiTinNhan extends Thread {
		public void run() {
			while (true) {// Lặp vô hạn để nhận thông báo
				try {
					System.out.print("Dang cho:");
					String dulieu = dis.readUTF();
					list.add("Client: " + dulieu);
				} catch (Exception e) {
					System.out.print(e);
				}
			}
		}
	}
	
	public void GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 12, 285, 31);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(321, 12, 89, 31);
		contentPane.add(btnSend);
		
		list = new List();
		list.setBounds(12, 73, 418, 187);
		contentPane.add(list);
		btnSend.addActionListener(this);
		show();
	}
	public static void main(String[] args) throws IOException {
		Server sv = new Server("Chat TCP Server");
		ServerSocket s = new ServerSocket(8000);
		InetAddress addrs;
		addrs = InetAddress.getLocalHost();
		System.out.println("TCP/Server running on: " + addrs + ", Port: " + s.getLocalPort());
		try {
			while(true) {
				Socket socket = s.accept();
				sv.connect(socket);
				Thread tdnhan = new Thread(sv.new ThreadGoiTinNhan());
		        tdnhan.start();
			}
		}
		finally {
			s.close();
		}
	}

	public void connect(Socket s) throws IOException {
		socket = s;
		System.out.println("Serving: " + socket);
		dos = new DataOutputStream(socket.getOutputStream());
		dis = new DataInputStream(socket.getInputStream());
	}
	public Server(String title) {
		super(title);
		GUI();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnSend) {
			try {				
				list.add("Server:" + textField.getText().toString());
				dos.writeUTF(textField.getText().toString());
				dos.flush();
				textField.setText("");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		
	}
}
