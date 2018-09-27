package netty.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import org.apache.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 模拟注册中心
 */
public class RpcRegister {
    private static Logger LOG = Logger.getLogger(RpcRegister.class);
    /**存放注册中心注册的服务**/
   // public static ConcurrentHashMap<String,Object> registryMap;

    /**
     * 开启服务
     * @param port
     */
    public void start(int port){
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try{
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss,worker)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();

                            //解决粘包拆包
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            pipeline.addLast(new LengthFieldPrepender(4));

                            //编解码
                            pipeline.addLast("encoder",new ObjectEncoder());
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            //自己的业务逻辑
                            pipeline.addLast(new RegisterHandler());
                        }
                    });

            ChannelFuture f = b.bind(port).sync();

            LOG.info("register start success!!!");

            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worker.shutdownGracefully();
            boss.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
            new RpcRegister().start(8081);
    }
}
