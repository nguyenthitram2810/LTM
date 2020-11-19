package bai5;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JList;
import java.awt.List;

public class Client extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JButton btnSend;
	private List list;
	
	private static DataOutputStream dos;
	private static DataInputStream dis;
	
	public void GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		btnSend = new JButton("GET DATA");
		btnSend.setBounds(153, 12, 100, 38);
		contentPane.add(btnSend);
		
		list = new List();
		list.setBounds(24, 56, 398, 182);
		contentPane.add(list);
		btnSend.addActionListener(this);
		show();
	}
	
	public Client(String str) {
		super(str);
		GUI();
	}
	
	public static void main(String[] args) throws IOException {
		Client cl = new Client("Database");
		Socket client = new Socket("localhost", 8000);
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnSend) {
			try {
				String st = dis.readUTF();
				System.out.println(st);
//				String[] data = st.split("\n");
				list.add(st);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
