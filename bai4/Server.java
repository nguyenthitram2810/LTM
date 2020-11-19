package bai4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Stack;

public class Server extends Thread {
	private Socket socket;
	private DataOutputStream dos;
	private DataInputStream dis;
	
	public String handleString(String s) 
	{
		String element[] = null;
		element = processString(s);
		element = postfix(element);
		Stack <String> S = new Stack<String>();
		for (int i = 1; i < element.length; i++)
		{
            char c = element[i].charAt(0);
//            System.out.println(element[i]);
            if (!isOperator(c)) S.push(element[i]);
            else
            {
                double num = 0f;
//                System.out.println(S.toString());
                double num1 = Float.parseFloat(S.pop());
                double num2 = Float.parseFloat(S.pop());
                switch (c) {
                    case '+' : num = num2 + num1; break;
                    case '-' : num = num2 - num1; break;
                    case '*' : num = num2 * num1; break;
                    case '/' : num = num2 / num1; break;
                    default:
                        break;
                }
                S.push(Double.toString(num));
            }
        }
//		String result = "Bieu thuc postfix: "+ String.join("", element) + "\n" 
//				+ "Ket qua: " +  s + "="+S.pop();
		String result = s + "= " + S.pop();
		return result;
	}
	
	public int priority(char c)
	{        // thiet lap thu tu uu tien
        if (c == '+' || c == '-') return 1;
        else if (c == '*' || c == '/') return 2;
        else return 0;
    }
	public boolean isOperator(char c)
	{  // kiem tra xem co phai toan tu
        char operator[] = { '+', '-', '*', '/', ')', '(' };
        Arrays.sort(operator);
        if (Arrays.binarySearch(operator, c) > -1)
            return true;
        else return false;
    }
	public String[] processString(String sMath)
	{ // xu ly bieu thuc nhap vao thanh cac phan tu
        String s1 = "", elementMath[] = null;
        sMath = sMath.trim();
        sMath = sMath.replaceAll("\\s+"," "); //    bang cach loai bo whitespace bi thua
        for (int i=0; i < sMath.length(); i++){
            char c = sMath.charAt(i);//sMath.substring(i,1);
            if (!isOperator(c)) // Neu c ko phai la operator (tuc c la chu so)
                s1 = s1 + c;
            else s1 = s1 + " " + c + " ";
        }
        s1 = s1.trim();
        s1 = s1.replaceAll("\\s+"," "); //  chuan hoa s1 (loai bo whitespace thua)
        elementMath = s1.split(" "); //tach s1 thanh cac phan tu
        
        return elementMath;
    }
	public String[] postfix(String[] elementMath)
	{       
        String s1 = "", E[];
        Stack<String> S = new Stack<String>();
        for (int i=0; i<elementMath.length; i++){    // duyet cac phan tu
            char c = elementMath[i].charAt(0);  // c la ky tu dau tien cua moi phan tu
 
            if (!isOperator(c))         // neu c khong la toan tu
                s1 = s1 + " " + elementMath[i];     // xuat elem vao s1
//            	s1 = s1 + elementMath[i];
            else{                       // c la toan tu
                if (c == '(') S.push(elementMath[i]);   // c la "(" -> day phan tu vao Stack
                else{
                    if (c == ')'){          // c la ")"
                        char c1;        //duyet lai cac phan tu trong Stack
                        do{
                            c1 = S.peek().charAt(0);    // c1 la ky tu dau tien cua phan tu
                            if (c1 != '(') s1 = s1 + " " + S.peek();    // trong khi c1 != "("
                            S.pop();
                        }while (c1 != '(');
                    }
                    else{
                        while (!S.isEmpty() && priority(S.peek().charAt(0)) >= priority(c)){
                        // Stack khong rong va trong khi phan tu trong Stack co do uu tien >= phan tu hien tai
                            s1 = s1 + " " + S.peek();   // xuat phan tu trong Stack ra s1
                            S.pop();
                        }
                        S.push(elementMath[i]); //  dua phan tu hien tai vao Stack
                    }
                }
            }
        }
        while (!S.isEmpty())
        {   // Neu Stack con phan tu thi day het vao s1
            s1 = s1 + " " + S.peek();
            S.pop();
        }
        E = s1.split(" ");  //  tach s1 thanh cac phan tu
        
        return E;
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
				System.out.println("....Server is waiting...");
				String str = dis.readUTF();
				if (str.equals("exit") ) break;
				System.out.println("Received: " + str);
				System.out.println("From: "+ socket);
				dos.writeUTF(handleString(str));
			}
			System.out.println("Disconnected with.."+socket);
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
