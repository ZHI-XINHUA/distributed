package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    public static void main(String[] args) {
        client(8080);
    }

    /**
     * 开启客户端
     */
    public static void client(int port){
        Socket socket = null;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            //创建socket连接
            socket = new Socket("127.0.0.1",port);
            //输入流
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //输出流，客户端发送消息给服务端
            out = new PrintWriter(socket.getOutputStream(),true);
            //客户端发送消息给服务端
            out.println("hello server,i am client!");

            //读取服务器响应信息
            String reponseText = in.readLine();
            System.out.println("客户端接收到服务端的消息:"+reponseText);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(out!=null){
                out.close();
            }

            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
