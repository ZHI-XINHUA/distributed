package udpsocket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by xh.zhi on 2018-8-22.
 */
public class SocketServer {
    public static void main(String[] args) throws Exception {
        server();
    }

    private static void server() throws Exception{
       /*
		 * 接收客户端发送的数据
		 */
        //1.创建服务器端DatagramSocket，指定端口
        DatagramSocket socket=new DatagramSocket(9999);
        //2.创建数据报，用于接收客户端发送的数据
        //创建字节数组，指定接收的数据包的大小
        byte[] data =new byte[1024];
        DatagramPacket packet=new DatagramPacket(data, data.length);
        //3.接收客户端发送的数据
        //此方法在接收到数据报之前会一直阻塞
        socket.receive(packet);
        //4.读取数据
        String info=new String(data, 0, packet.getLength());
        System.out.println("服务端接受数据："+info);

		/*
		 * 向客户端响应数据
		 */
        //1.定义客户端的地址、端口号、数据
        InetAddress address=packet.getAddress();
        int port=packet.getPort();
        byte[] data2="欢迎您!".getBytes();
        //2.创建数据报，包含响应的数据信息
        DatagramPacket packet2=new DatagramPacket(data2, data2.length, address, port);
        //3.响应客户端
        socket.send(packet2);
        //4.关闭资源
        socket.close();



    }
}
