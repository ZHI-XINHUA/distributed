package netty.rpc.registry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 注册中心消息处理类
 */
public class RegisterHandler extends ChannelInboundHandlerAdapter {
    /**存放注册中心注册的服务**/
    public static ConcurrentHashMap<String,Object> registryMap = new ConcurrentHashMap<String,Object>();

    /**存放class的缓冲集合**/
    private List<String> classCache = new ArrayList<String>();

    public RegisterHandler(){

    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }


    /**
     * 扫描provider类
     * @param packageName
     */
    private void scanClass(String packageName){

    }

    /**
     * 注册
     */
    private void doRegister(){

    }






















}
