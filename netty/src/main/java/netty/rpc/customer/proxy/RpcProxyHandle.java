package netty.rpc.customer.proxy;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.rpc.core.msg.InvokerMsg;

/**
 * Created by xh.zhi on 2018-9-27.
 */
public class RpcProxyHandle extends ChannelInboundHandlerAdapter {
    private Object result;

    /**
     * 获取结构
     * @return
     */
    public Object getResult(){
        return result;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        this.result = msg;
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
