package netty.mytomcat.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentDecoder;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.apache.log4j.Logger;

/**
 * Created by xh.zhi on 2018-9-20.
 *  自定义tomcat
 */
public class MyTomcat {
    private static Logger LOG = Logger.getLogger(MyTomcat.class);
    /**
     * 开启tomcat
     * @param port
     */
    public void start(int port){
        //创建主
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //创建从
        EventLoopGroup  workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workerGroup).channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //服务端发送的是httpResponse，所以要使用HttpResponseEncoder进行编码
                            socketChannel.pipeline().addLast(new HttpResponseEncoder());
                            //服务端接收到的是httpRequest，所以要使用HttpRequestDecoder进行解码
                            socketChannel.pipeline().addLast(new HttpRequestDecoder());
                            //最后处理自己的逻辑
                            socketChannel.pipeline().addLast(new MyTomcatHandler());
                        }
                    }).option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            //绑定服务端口
            ChannelFuture future = bootstrap.bind(port).sync();

            System.out.println("HTTP服务已启动，监听端口:" + port);
            LOG.info("HTTP服务已启动，监听端口:" + port);

            //开始接收客户
            future.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }


    }
    public static void main(String[] args) {
        new MyTomcat().start(9876);
    }
}
