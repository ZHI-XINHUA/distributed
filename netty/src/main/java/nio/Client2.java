package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * Created by xh.zhi on 2018-9-17.
 */
public class Client2 {
    public static void main(String[] args) {
        new Thread(new NioClient(null,9876)).start();
    }
}

class NioClient implements Runnable{
    /**ip**/
    private String host;
    /**通信端口**/
    private int port;
    /**多路复用器（管理通道）**/
    private Selector selector;
    /**客户端通道**/
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public NioClient(String host,int port){
        this.host = host == null ? "127.0.0.1" : host;
        this.port = port;
        try{
            // 1、打开多路复用器
            selector = Selector.open();
            // 2、打开SocketChannel(客户端通道)
            socketChannel = SocketChannel.open();
            // 3、设置为非阻塞
            socketChannel.configureBlocking(false);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {

        try {
            // 1、连接
            //如果直接连接成功，则注册到多路复用器上，发送请求消息，读应答
            if(socketChannel.connect(new InetSocketAddress(host,port))){
                //注册，监听读事件
                socketChannel.register(selector, SelectionKey.OP_READ);
                // 写入消息
                doWrite(socketChannel);
            }else{
                //注册建立连接事件
                socketChannel.register(selector,SelectionKey.OP_CONNECT);
            }

            while(!stop){
                //2、让多复用器开始监听
                selector.select();
                //3、轮询准备就绪的key
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                // 4、变量key集合
                while (keys.hasNext()){
                    // 5、获取一个选择的元素
                    SelectionKey key = keys.next();
                    // 5、 直接从容器中移除就可以了
                    keys.remove();
                    // 6、消息处理
                    processHandler(key);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 消息处理
     * @param key
     */
    private void processHandler(SelectionKey key) throws IOException {
        //连接不成功
        if(!key.isValid()){
            return;
        }

        SocketChannel socketChannel = (SocketChannel) key.channel();

        if(key.isConnectable()){
            //连接成功
            if(socketChannel.finishConnect()){
                socketChannel.register(selector, SelectionKey.OP_READ);
                doWrite(socketChannel);
            }else{
                //退出
                System.exit(1);
            }
        }

        if (key.isReadable()) {
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            int readBytes = socketChannel.read(readBuffer);
            if (readBytes > 0) {
                readBuffer.flip();
                byte[] bytes = new byte[readBuffer.remaining()];
                readBuffer.get(bytes);
                String body = new String(bytes, "UTF-8");
                System.out.println("客户端接受服务端返回数据: " + body);
                this.stop = true;
            } else if (readBytes < 0) {
                // 对端链路关闭
                key.cancel();
                socketChannel.close();
            } else{
                ; // 读到0字节，忽略
            }

        }


    }

    /**
     * 写入消息
     * @param socketChannel
     */
    private void doWrite(SocketChannel socketChannel) {
        try {
            byte[] req = "I am client".getBytes();
            ByteBuffer buffer = ByteBuffer.allocate(req.length);
            //写入缓冲区
            buffer.put(req);
            //复位
            buffer.flip();
            //写入管道
            socketChannel.write(buffer);

            if(!buffer.hasRemaining()){
                System.out.println("客户端发送消息成功！");
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
