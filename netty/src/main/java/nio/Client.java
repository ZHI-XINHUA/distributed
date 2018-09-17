package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;

/**
 * Created by xh.zhi on 2018-9-17.
 * nio client
 */
public class Client {

    public static void main(String[] args) {
        //创建连接的地址
        InetSocketAddress address = new InetSocketAddress(9876);
        //声明连接通道
        SocketChannel sc = null;
        //建立缓冲区
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

        try{
            // 1、打开通道（SocketChannel）
            sc = SocketChannel.open();
            // 2、连接服务器
            sc.connect(address);

            while (true){
                //定义一个字节数组，然后使用系统录入功能：
                byte[] bytes = new byte[1024];
                System.in.read(bytes);

                //把数据放到缓冲区中
                byteBuffer.put(bytes);
                //对缓冲区进行复位
                byteBuffer.flip();
                //通道写入数据
                sc.write(byteBuffer);
                //清空缓冲区数据
                byteBuffer.clear();

               // System.exit(1);
            }
        }catch (Exception e){

        }finally {
            if(sc!=null){
                try {
                    sc.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }
}
