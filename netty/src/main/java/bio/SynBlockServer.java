package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * bio 服务端
 */
public class SynBlockServer {

    public static void main(String[] args) {
        int port = 8080;
        Server(port);
    }

    /**
     * 开启服务端
     * @param port
     */
    public static void Server(int port){
        ServerSocket server = null;
        try{
            //1、创建一个新的 ServerSocket，用以监听指定端口上的连接请求
            server = new ServerSocket(port);
            Socket socket = null;

            //无限循环监听客户端连接
            while(true){
                //2、将被阻塞，直到一个连接建立
                socket = server.accept();

                //3、开启线程处理客户端返回信息
                new Thread(new ServerHandler(socket)).start();

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            if(server!=null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/**
 * 服务端消息处理类
 */
class ServerHandler implements Runnable{
    private Socket socket;

    public ServerHandler(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;


        try {
            //读取客户端消息流
            in = new BufferedReader(new InputStreamReader(  socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            String body = null;


            while(true){
                //一行行读取客户端信息
                body = in.readLine();
                //body为null 客户端端信息读取完成
                if(body==null){
                    break;
                }
                System.out.println("服务器端接收到的客户端信息:"+body);

                //服务端发送消息给客户端
                out.println(new Date()+" hello client!");

            }

            // 发送消息不能写在while后面，因为这样客户端和服务网连接永远不会关闭；客户端in.readLine()阻塞，一直读取服务端信息
            //out.println(" hello client!");




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
