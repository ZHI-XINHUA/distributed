package serversocket_api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-10-26.
 */
public class Server {
    public static void main(String[] args) throws InterruptedException, IOException {
            ServerSocket serverSocket = new ServerSocket(8888);

            Socket socket = serverSocket.accept();

        System.out.println("服务端端口："+socket.getLocalPort());
        System.out.println("客户端端口："+socket.getPort());

    }
}
