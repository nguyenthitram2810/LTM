package bai2;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JList;

public class Client extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSend;
	private JLabel lblChuoi;
	private JList<String> list;
	private static DataOutputStream dos;
	private static DataInputStream dis;
	
	@SuppressWarnings("deprecation")
	public void GUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(98, 47, 175, 30);
		contentPane.add(textField);
		textField.setColumns(10);
		
		lblChuoi = new JLabel("Chuỗi: ");
		lblChuoi.setBounds(35, 46, 84, 30);
		contentPane.add(lblChuoi);
		
		btnSend = new JButton("Send");
		btnSend.setBounds(321, 49, 75, 25);
		contentPane.add(btnSend);
		btnSend.addActionListener(this);
		
		list = new JList<String>();
		list.setBounds(32, 89, 389, 162);
		contentPane.add(list);
		show();
	}
	@SuppressWarnings("resource")
	public static void main(String[] args) throws Exception {
		new Client("Xu Ly Chuoi");
		Socket client = new Socket("localhost", 8000);
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
	}

	public Client(String st) {
		super(st);
		GUI();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == btnSend) {
			try {
				dos.writeUTF(textField.getText().toString());
				dos.flush();
				System.out.println(textField.getText().toString());
				String st = dis.readUTF();
				String[] listResult = st.split("\n");
				list.setListData(listResult);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}
