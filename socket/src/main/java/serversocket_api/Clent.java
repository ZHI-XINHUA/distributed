package serversocket_api;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-10-26.
 */
public class Clent {
    public static void main(String[] args) throws IOException {

      Socket socket = new Socket();
      socket.bind(new InetSocketAddress("localhost",7777));
      socket.connect(new InetSocketAddress("localhost",8888));

        socket.setKeepAlive(true);
        System.out.println("客户端端口："+socket.getLocalPort());
        System.out.println("服务端端口："+socket.getPort());

    }
}
