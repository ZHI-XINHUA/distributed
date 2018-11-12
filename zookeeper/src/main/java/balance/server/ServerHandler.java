package balance.server;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * server handler处理器
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {//ChannelInboundHandlerAdapter

    private final BalanceUpdateProvider balanceUpdater;

    private static final Integer BALANCE_STEP = 1;

    public ServerHandler(DefaultBalanceUpdateProvider defaultBalanceUpdateProvider) {
        balanceUpdater = defaultBalanceUpdateProvider;
    }


    //当 Channel 处于活动状态时被调用；Channel 已经连接/绑定并且已经就绪
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(" client connect...");
        balanceUpdater.addBalance(BALANCE_STEP);
    }

    //当 Channel 离开活动状态并且不再连接它的远程节点时被调用
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("client disconnect...");
        balanceUpdater.reduceBalance(BALANCE_STEP);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
