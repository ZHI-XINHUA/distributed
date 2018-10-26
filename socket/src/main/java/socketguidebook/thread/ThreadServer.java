package socketguidebook.thread;

import udpsocket.SocketServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-10-26.
 * 使用线程处理接受消息
 */
public class ThreadServer {
    public static void main(String[] args) {
        new Server().start();
    }
}


class Server{
   public void start(){
       try {
           ServerSocket serverSocket = new ServerSocket(9091);
           int i = 1;
            while (i==1){
                Socket socket = serverSocket.accept();
                new  ThreadDeal(socket).start();
            }
            serverSocket.close();
       } catch (Exception e) {
           e.printStackTrace();
       }
   }
}

class ThreadDeal extends Thread{
    Socket socket;
    public ThreadDeal(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();

            InputStreamReader reader = new InputStreamReader(inputStream);

            char[] charArray = new char[1024];
            int readLength = -1;
            while ((readLength = reader.read(charArray))!=-1){
                String s = new String(charArray,0,readLength);
                System.out.println(s);
            }

            reader.close();
            inputStream.close();
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
