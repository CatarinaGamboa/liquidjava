package part3_liquidJava.arrayDeque;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Test3 {

	public static void main(String[] args) throws IOException {
		int port = 5000;
		InetAddress inetAddress = InetAddress.getByName("localhost");    
		
		Socket socket = new Socket();
		socket.bind(new InetSocketAddress(inetAddress, port));
            //socket.connect(new InetSocketAddress(inetAddress, port)); //error missing this line
		socket.sendUrgentData(90);
		socket.close();
	}
}
