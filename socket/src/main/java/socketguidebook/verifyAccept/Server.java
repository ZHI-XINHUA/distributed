package socketguidebook.verifyAccept;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-10-19.
 * 验证accept是否阻塞
 */
public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9091,2);
            System.out.println("accept开始阻塞："+System.currentTimeMillis());
            //一直阻塞，直到监听到有客户端请求连接
            Socket socket = serverSocket.accept();

            System.out.println("accept阻塞结束："+System.currentTimeMillis());
            InputStream inputStream = socket.getInputStream();

            byte[] bytes = new byte[1024];
            System.out.println("read开始阻塞："+System.currentTimeMillis());
            int length = inputStream.read(bytes);
            System.out.println("read阻塞结束："+System.currentTimeMillis());

            System.out.println(new String(bytes));

            inputStream.close();
            socket.close();
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
