package socketguidebook.verifyAccept;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-10-19.
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        try {
            System.out.println("client准备连接："+System.currentTimeMillis());
            try (Socket socket = new Socket("127.0.0.1", 9091)) {
                //两秒后写出数据
                TimeUnit.SECONDS.sleep(2);
                OutputStream outputStream = socket.getOutputStream();
                outputStream.write("hello".getBytes());
                outputStream.flush();
            }
            System.out.println("client连接结束："+System.currentTimeMillis());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
