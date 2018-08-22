package socketdemo;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-8-22.
 * socket双向通信 server端
 */
public class SocketServer {

    public static void main(String[] args) throws Exception{
        server();
    }

    private  static void server() throws IOException {
        //端口
        int port = 8888;
        ServerSocket server = null;

        try {
            // 监听指定的端口
            server = new ServerSocket(port);
            //阻塞，等待一个接收请求
            Socket socket = server.accept();

            //读取数据
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //读取客户端发送过来的消息
            String clientData=reader.readLine();

            System.out.println("服务端接收到的数据："+clientData);

            //发送数据给客户端
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println("hello client!");
            writer.flush();

            reader.close();
            writer.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭server
            if(server!=null && !server.isClosed()){
                server.close();
            }

        }

    }

    private static  void server1() throws Exception{
        // 监听指定的端口
        int port = 55533;
        ServerSocket server = new ServerSocket(port);

        // server将一直等待连接的到来
        System.out.println("server将一直等待连接的到来");
        Socket socket = server.accept();
        // 建立好连接后，从socket中获取输入流，并建立缓冲区进行读取
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        int len;
        StringBuilder sb = new StringBuilder();
        //只有当客户端关闭它的输出流的时候，服务端才能取得结尾的-1
        while ((len = inputStream.read(bytes)) != -1) {
            // 注意指定编码格式，发送方和接收方一定要统一，建议使用UTF-8
            sb.append(new String(bytes, 0, len, "UTF-8"));
        }
        System.out.println("get message from client: " + sb);

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("Hello Client,I get the message.".getBytes("UTF-8"));

        inputStream.close();
        outputStream.close();
        socket.close();
        server.close();
    }
}
