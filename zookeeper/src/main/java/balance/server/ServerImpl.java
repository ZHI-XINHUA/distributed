package balance.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.serialize.SerializableSerializer;

/**
 * 服务器类，使用netty与客户端通信
 */
public class ServerImpl implements Server {

    private static  final int SESSION_TIME_OUT = 10000;

    private static final  int CONNECT_TIME_OUT = 10000;


    /**绑定标识**/
    private volatile boolean binded = false;

    /**注册pervicer**/
    private RegistProvicer registProvicer;

    /**服务器节点**/
    private String serversPath;

    /**服务器信息**/
    private ServerData serverData;

    /**zk客户端**/
    private ZkClient zkClient;

    /**当前服务的节点路径**/
    private String currentServerPath;

    public ServerImpl(String host,String serversPath, ServerData serverData){
        this.registProvicer = new DefaultRegistProvider();
        this.serverData = serverData;
        this.serversPath = serversPath;
        //创建zk客户端
        zkClient  =  new ZkClient(host,SESSION_TIME_OUT,CONNECT_TIME_OUT,new SerializableSerializer());
    }

    public void registMe(){
        //注册到zookeeper
        try {
            String mePath = serversPath.concat("/").concat(serverData.getPort().toString());
            this.registProvicer.regist(new ZooKeeperRegistContext(mePath,zkClient,serverData));
            this.currentServerPath = mePath;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void bind() {
        //已经绑定就不再绑定
        if(binded){
            return;
        }
        System.out.println(serverData.getHost()+":"+serverData.getPort()+" bind....");

        //注册自己
        registMe();

        //netty bind
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG,1024)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new ServerHandler(new DefaultBalanceUpdateProvider(currentServerPath,zkClient)));
                    }
                });

        try {
            ChannelFuture channelFuture = bootstrap.bind(serverData.getPort()).sync();
            binded = true;
            System.out.println(serverData.getPort()+":binded...");
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
