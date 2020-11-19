package bai4;

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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTable;
import java.awt.List;

public class Client extends JFrame implements ActionListener{

	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSend;
	private JList list;
	
	private static DataOutputStream dos;
	private static DataInputStream dis;
	
	
	public void GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(12, 4, 281, 40);
		contentPane.add(textField);
		textField.setColumns(10);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(310, 7, 118, 33);
		contentPane.add(btnSend);
		
		list = new JList();
		list.setBounds(23, 60, 392, 173);
		contentPane.add(list);
		btnSend.addActionListener(this);
		show();
	}
	
	public Client(String st) {
		super(st);
		GUI();
	}
	
	public static void main(String[] args) throws IOException {
		new Client("Tinh toan");
		Socket client = new Socket("localhost", 8000);
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnSend) {
			try {
				dos.writeUTF(textField.getText().toString());
				dos.flush();
				String[] st = { dis.readUTF() };
				list.setListData(st);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
