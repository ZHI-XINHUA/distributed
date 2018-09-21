package bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 伪异步io
 */
public class FakeAsyBlockServer {

    public static void main(String[] args) {
        server(8080);

    }

    public static void server(int port){
        ServerSocket server = null;
        try{
            //1、创建一个新的 ServerSocket，用以监听指定端口上的连接请求
            server = new ServerSocket(port);
            Socket socket = null;

            //无限循环监听客户端连接
            while(true){
                //2、将被阻塞，直到一个连接建立
                socket = server.accept();

                //3、自定义线程池处理客户端返回信息
               new ServerHandlerExecutePool(50,100).execute(new ServerHandlerTask(socket));

            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            //关闭资源
            if(server!=null){
                try {
                    server.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

/**
 * 自定义连接池
 */
class ServerHandlerExecutePool{
    private ExecutorService executorService;

    public ServerHandlerExecutePool(int maxPoolSize,int queueSize){
        /**
         public ThreadPoolExecutor(int corePoolSize,
                                    int maximumPoolSize,
                                    long keepAliveTime,
                                    TimeUnit unit,
                                    BlockingQueue<Runnable> workQueue,
                                    ThreadFactory threadFactory,
                                    RejectedExecutionHandler handler)
         **/
        //自定义连接池
        executorService = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors(),
                maxPoolSize,
                120L,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(queueSize));

    }

    /**
     * 执行连接池
     * @param task
     */
    public void execute(Runnable task){
        executorService.execute(task);
    }
}

/**
 * 服务端消息处理类
 */
class ServerHandlerTask implements Runnable{
    private Socket socket;

    public ServerHandlerTask(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        BufferedReader in = null;
        PrintWriter out = null;

        try{
            //读取客户端消息流
            in = new BufferedReader(new InputStreamReader( socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(),true);
            String body = null;


            while(true){
                //一行行读取客户端信息
                body = in.readLine();
                //body为null 客户端端信息读取完成
                if(body==null){
                    break;
                }
                System.out.println("服务器端接收到的客户端信息:"+body);

                //服务端发送消息给客户端
                out.println(new Date()+" hello client!");

            }

            // 发送消息不能写在while后面，因为这样客户端和服务网连接永远不会关闭；客户端in.readLine()阻塞，一直读取服务端信息
            //out.println(" hello client!");




        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(out!=null){
                out.close();
            }

            if(socket!=null){
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
