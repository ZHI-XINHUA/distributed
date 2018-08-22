package multicastsocket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

/**
 * Created by xh.zhi on 2018-8-22.
 */
public class MulticastServer {
    public static void main(String[] args) {

        InetAddress group;
        try {
            //地址段：224.0.0.0 - 239.255.255.255
            group = InetAddress.getByName("224.5.6.7");

            MulticastSocket socket = new MulticastSocket();

            //重要的事情说三遍，广播三次
            for(int i=0;i<3;i++){
                String data="紧急通知！";
                byte[] bytes = data.getBytes();
                //发送消息
                socket.send(new DatagramPacket(bytes,bytes.length,group,8888));

                TimeUnit.SECONDS.sleep(2);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
