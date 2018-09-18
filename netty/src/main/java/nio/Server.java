package nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by xh.zhi on 2018-9-17.
 * nio server
 */
public class Server {
    public static void main(String[] args) {
        new Thread(new NioServer(9876)).start();
    }
}

class NioServer implements Runnable{
    /**多路复用器（管理通道）**/
    private Selector selector;
    /**建立读缓冲区**/
    private ByteBuffer redBuffer = ByteBuffer.allocate(1024);
    /**建立写缓冲区**/
    private ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

    public NioServer(int port){
        try{
            //1、打开多路复用器
            selector = Selector.open();
            // 2、打开ServerSocketChannel(服务器通道)
            ServerSocketChannel ssc = ServerSocketChannel.open();
            // 3、设置服务器通道为非阻塞模式
            ssc.configureBlocking(false);
            // 4、绑定监听地址InetSocketAddress
            ssc.bind(new InetSocketAddress(port));
            // 5、把服务器通道注册到多路复用器上，并且监听阻塞事件(OP_ACCEPT)
            ssc.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("NioServer init success!");

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true){
            try {
                //1、让多复用器开始监听
                this.selector.select();
                //2、轮询准备就绪的key
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();
                // 3、变量key集合
                while (keys.hasNext()){
                    // 4、获取一个选择的元素
                    SelectionKey key = keys.next();
                    // 5、 直接从容器中移除就可以了
                    keys.remove();
                    // 6、消息处理
                    processHandler(key);


                }
                System.out.println(" Rector thread  success");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    /**
     * 消息处理
     * @param key
     */
    public void processHandler(SelectionKey key){
        //无效不处理
        if(!key.isValid()) {
            return;
        }

        //如果状态为阻塞
        if(key.isAcceptable()){
            //处理新接入的请求消息
            accept(key);
        }
        //如果状态为可读
        if(key.isReadable()){
            read(key);
        }
        //如果状态为可写
        if(key.isWritable()){
            //doWrite(key);
        }
    }

    /**
     * 处理新接入的请求消息
     * @param key
     */
    private void accept(SelectionKey key){
        try {
            System.out.println("server!accept()");
            // 1、获取服务器通道 ServerSocketChannel
            ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
            // 2、执行阻塞方法
            SocketChannel sc = ssc.accept();
            // 3、设置为非阻塞
            sc.configureBlocking(false);
            //4 注册到多路复用器上，并设置读取标识
            sc.register(selector,SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取消息
     * @param key
     */
    private void read(SelectionKey key){
        System.out.println("server!read()");
        try {
            // 1、清除读缓冲区旧数据
            this.redBuffer.clear();
            //2 、获取之前注册的socket通道对象(客户端通道)
            SocketChannel socketChannel = (SocketChannel) key.channel();
            // 3、读取数据
            int readBytes = socketChannel.read(redBuffer);
            //4、如果没有数据
            if(readBytes==-1){
                //关闭资源
                selectionKeyClose(key);
                return;
            }

            //5 有数据则进行读取 读取之前需要进行复位方法(把position 和limit进行复位)
            redBuffer.flip();
            //6 根据缓冲区的数据长度创建相应大小的byte数组，接收缓冲区的数据
            byte[] bytes = new byte[redBuffer.remaining()];
            //7 接收缓冲区数据
            redBuffer.get(bytes);
            //8 打印结果
            String readMsg = new String(bytes);
            System.out.println("服务器读取客户端消息："+readMsg);


        } catch (IOException e) {
            selectionKeyClose(key);
            e.printStackTrace();
        }
    }

    /**
     * 写入消息
     * @param key
     */
    private void doWrite(SelectionKey key) {
        try {
            SocketChannel socketChannel = (SocketChannel) key.channel();

            byte[] req = "I am server".getBytes();

            this.writeBuffer.clear();
            //写入缓冲区
            writeBuffer.put(req);
            //复位
            writeBuffer.flip();
            //写入管道
            socketChannel.write(writeBuffer);

            if(!writeBuffer.hasRemaining()){
                System.out.println("服务端发送消息成功！");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭SelectionKey
     * @param key
     */
    private void selectionKeyClose(SelectionKey key){
        try{
            if (key != null) {
                key.cancel();
                if (key.channel() != null){
                    key.channel().close();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
