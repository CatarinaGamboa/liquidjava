package repair.regen.classes.socket_error;


public class Test {
    public static void main(java.lang.String[] args) throws java.io.IOException {
        int port = 5000;
        java.net.InetAddress inetAddress = java.net.InetAddress.getByName("localhost");
        java.net.Socket socket = new java.net.Socket();
        socket.bind(new java.net.InetSocketAddress(inetAddress, port));
        // socket.connect(new InetSocketAddress(inetAddress, port));
        socket.sendUrgentData(90);
        socket.close();
    }
}

