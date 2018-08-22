package socketdemo;

import java.io.*;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-8-22.
 */
public class SocketClient {
    public static void main(String[] args) throws Exception {
        client1();
    }

    private static void client() throws IOException {
        Socket socket = null;
        try {
            //与服务端建立连接
            socket = new Socket("localhost",8080);

            //发送消息到服务端
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()),true);
            writer.println("hello server!");

            //读取服务端信息
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            while (true){
                String serverData = reader.readLine();
                if(serverData==null){
                    break;
                }
                System.out.println("客户接收到的数据："+serverData);
            }

            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭socket
            if(socket != null && !socket.isClosed()){
                socket.close();
            }
        }
    }

    private static void client1() throws Exception{
        // 要连接的服务端IP地址和端口
        String host = "127.0.0.1";
        int port = 55533;
        // 与服务端建立连接
        Socket socket = new Socket(host, port);
        // 建立连接后获得输出流
        OutputStream outputStream = socket.getOutputStream();
        String message = "你好  yiwangzhibujian";
        socket.getOutputStream().write(message.getBytes("UTF-8"));
        //通过shutdownOutput高速服务器已经发送完数据，后续只能接受数据
        socket.shutdownOutput();

        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        while ((len = inputStream.read(bytes)) != -1) {
            //注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len,"UTF-8"));
        }
        System.out.println("get message from server: " + sb);

        inputStream.close();
        outputStream.close();
        socket.close();

    }

}
