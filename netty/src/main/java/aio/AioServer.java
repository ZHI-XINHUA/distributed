package aio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.*;

/**
 * Created by xh.zhi on 2018-9-19.
 */
public class AioServer {
    /**线程池**/
    private ExecutorService executorService;
    /**管道组**/
    private AsynchronousChannelGroup threadGroup;
    /**服务器通道**/
    public AsynchronousServerSocketChannel assc;

    public AioServer(int port){
        try {
            // 1、创建自定义线程池
            executorService =new ThreadPoolExecutor(
                            Runtime.getRuntime().availableProcessors(),
                            50,
                            120L,
                            TimeUnit.SECONDS,
                            new ArrayBlockingQueue<Runnable>(100));

            // 2、创建管道组，并放到线程池中
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executorService,1);
            // 3、创建服务器通道
            assc = AsynchronousServerSocketChannel.open(threadGroup);
            // 4、绑定端口，监听
            assc.bind(new InetSocketAddress(port));

            System.out.println("server start , port : " + port);

            // 5、注释，直到一个连接建立
            assc.accept(this,new ServerCompletionHandler());

            //模拟服务器一直监听客户端。一直阻塞 不让服务器停止
            Thread.sleep(Integer.MAX_VALUE);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
            new AioServer(8765);
    }
}

