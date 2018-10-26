package socketguidebook.createweb;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by xh.zhi on 2018-10-19.
 * 使用socket创建web服务器
 */
public class WebServer {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9009);
             Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
             ) {

            //读取客户端 请求报文
           String getString = "";
           while(!"".equals(getString=bufferedReader.readLine())){
               System.out.println(getString);
           }

           //服务端响应报文
           String headStr = "HTTP/1.1 200 OK\r\n\r\n";
           String bodyStr = "<html><body><a href='http:www.baidu.com'>baidu</a></body></html>";
           OutputStream outputStream = socket.getOutputStream();
           outputStream.write(headStr.getBytes());
           outputStream.write(bodyStr.getBytes());
           outputStream.flush();
           outputStream.close();

           Thread.sleep(Integer.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
