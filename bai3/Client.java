package bai3;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.List;
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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;

public class Client extends JFrame implements ActionListener {

	private JPanel contentPane;
	private JTextField textField;
	private JButton btnSend;

	private static DataOutputStream dos;
	private static DataInputStream dis;
	private List list;

	public class ThreadGoiTinNhan extends Thread {
		public void run() {
			while (true) {// Lặp vô hạn để nhận thông báo
				try {
					System.out.print("Dang cho:");
					String dulieu = dis.readUTF();
					list.add("Server: " + dulieu);
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
		textField.setBounds(33, 12, 279, 35);
		contentPane.add(textField);
		textField.setColumns(10);

		btnSend = new JButton("Send");
		btnSend.setBounds(340, 14, 84, 30);
		contentPane.add(btnSend);
		
		list = new List();
		list.setBounds(33, 74, 391, 186);
		contentPane.add(list);
		btnSend.addActionListener(this);
		show();
	}

	public Client(String st) {
		super(st);
		GUI();
	}

	public static void main(String[] args) throws Exception {
		Client s = new Client("Chat TCP Client");
		Socket client = new Socket("localhost", 8000);
		dos = new DataOutputStream(client.getOutputStream());
		dis = new DataInputStream(client.getInputStream());
		Thread tdnhan = new Thread(s.new ThreadGoiTinNhan());
        tdnhan.start();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnSend) {
			try {
				list.add("Client:" + textField.getText().toString());
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
