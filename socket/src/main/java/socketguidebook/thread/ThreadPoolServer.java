package socketguidebook.thread;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xh.zhi on 2018-10-26.
 * 线程池处理消息
 */
public class ThreadPoolServer {
    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(9091);
            boolean shutdown = false;
            while(!shutdown){
                Socket socket = serverSocket.accept();
                ExecutorService executorService= Executors.newFixedThreadPool(10);
                executorService.execute(new ThreadDealMsg(socket));
            }
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 处理消息线程
 */
class ThreadDealMsg implements Runnable{

    Socket socket;

    public ThreadDealMsg(Socket socket){
        this.socket = socket;
    }
    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            int readLenth = inputStream.read(bytes);
            while(readLenth!=-1){
                System.out.println(new String(bytes,0,readLenth));
                readLenth = inputStream.read(bytes);
            }

            inputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}