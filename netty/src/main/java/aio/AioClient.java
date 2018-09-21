package aio;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ExecutionException;

/**
 * Created by xh.zhi on 2018-9-19.
 * aio client
 */
public class AioClient {
    public static void main(String[] args) throws InterruptedException {
        //创建连接三个客户端
        InitClient c1 = new InitClient();
        c1.connect();

        InitClient c2 = new InitClient();
        c2.connect();
        InitClient c3 = new InitClient();
        c3.connect();

        /**
         * 开始客户端
         */
        new Thread(c1, "c1").start();
        new Thread(c2, "c2").start();
        new Thread(c3, "c3").start();

        Thread.sleep(1000);

        c1.write("c1 aaa");
        c2.write("c2 bbbb");
        c3.write("c3 ccccc");

        String a = "d";
        String b="c";
        a.compareTo(b);

        TreeMap<String,String> treeMap = new TreeMap<String, String>();
        treeMap.put("d","d");
    }
}

class InitClient implements Runnable{
    /** ip **/
    private String address = "127.0.0.1";
    /** 端口**/
    private int port = 8765;
    /**客户端通道**/
    private AsynchronousSocketChannel asc;

    public InitClient(){
        try {
            //打开通道
            asc = AsynchronousSocketChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 与服务器建立连接
     */
    public void connect(){
        asc.connect(new InetSocketAddress(address,port));
    }

    /**
     * 读取消息
     */
    public void read(){
        // 建立读缓冲区
        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        try {
            // 管道读取数据到读缓冲区中
            asc.read(readBuffer).get();
            // 复位
            readBuffer.flip();
            // 获取缓冲区数据
            byte[] respByte = new byte[readBuffer.remaining()];
            readBuffer.get(respByte);
            System.out.println(new String(respByte,"utf-8").trim());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void write(String requestMsg){
        try {
            // 写入数据到管道
            asc.write(ByteBuffer.wrap(requestMsg.getBytes())).get();

            //读取服务器响应数据
            read();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }
}
