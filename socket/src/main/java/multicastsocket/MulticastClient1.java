package multicastsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

/**
 * Created by xh.zhi on 2018-8-22.
 */
public class MulticastClient1 {
    public static void main(String[] args) {
        try {
            //广播地址
            InetAddress group = InetAddress.getByName("224.5.6.7");
            //端口监听广播
            MulticastSocket socket = new MulticastSocket(8888);
            //添加到组里面
            socket.joinGroup(group);

            byte[] buf = new byte[1024];
            //死循环，达到一致接收广播信息
            while(true){
                DatagramPacket msgPacket=new DatagramPacket(buf,buf.length);
                socket.receive(msgPacket);

                String msg=new String(msgPacket.getData());
                System.out.println("接收到的数据："+msg);
            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
