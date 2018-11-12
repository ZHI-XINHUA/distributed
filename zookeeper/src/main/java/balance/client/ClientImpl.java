package balance.client;

import balance.server.ServerData;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Created by xh.zhi on 2018-11-12.
 */
public class ClientImpl implements Client {
    private final BalanceProvider<ServerData> provider;

    EventLoopGroup group ;
    Channel channel;

    public ClientImpl(BalanceProvider<ServerData> provider) {
        this.provider = provider;
    }

    @Override
    public void connect() throws Exception {

        ServerData serverData = provider.getBalanceItem();

        System.out.println("connecting to "+serverData.getHost()+":"+serverData.getPort()+", it's balance:"+serverData.getBalance());

        try{
            group = new NioEventLoopGroup();
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new ClientHandler());
                        }
                    });
            ChannelFuture f = b.connect(serverData.getHost(),serverData.getPort()).syncUninterruptibly();
            channel = f.channel();

            System.out.println("started success!");
        }catch (Exception e){

        }

    }

    @Override
    public void disConnect() throws Exception {
        try{

            if (channel!=null)
                channel.close().syncUninterruptibly();

            group.shutdownGracefully();
            group = null;

            System.out.println("disconnected");

        }catch(Exception e){

        }
    }
}
