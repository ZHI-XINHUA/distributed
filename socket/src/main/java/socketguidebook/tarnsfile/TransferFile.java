package socketguidebook.tarnsfile;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by xh.zhi on 2018-10-19.
 *  传输图片
 */
public class TransferFile {
    public static void main(String[] args) {

    }
}

/**
 * 服务端读取客户端传送的图片
 */
class Server{
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(9092);
             Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             ) {
            byte[] bytes = new byte[2048];
            int readLength = inputStream.read(bytes);

            //创建文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(new File("c:\\newimage.png"));
            //服务器接受的流写到文件输出流中
            while(readLength!=-1){
                fileOutputStream.write(bytes,0,readLength);
                readLength = inputStream.read(bytes);
            }
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/**
 * 客户端发送图片给服务端
 */
class Client{
    public static void main(String[] args) {
        String imagePath = "c:\\diagram.png";
        byte[] inBytes = new byte[2048];
        try {
            //图片读取写入流中
            FileInputStream fileInputStream = new FileInputStream(new File(imagePath));
            int readLength = fileInputStream.read(inBytes);
            Socket socket = new Socket("localhost", 9092);
            OutputStream outputStream = socket.getOutputStream();

            while (readLength!=-1){
                outputStream.write(inBytes,0,readLength);
                readLength = fileInputStream.read(inBytes);
            }

            outputStream.close();
            fileInputStream.close();
            socket.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }





    }
}
