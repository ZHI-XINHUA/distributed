package netty.rpc.customer.proxy;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import netty.rpc.core.msg.InvokerMsg;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by xh.zhi on 2018-9-27.
 */
public class RpcProxy {

    /**
     * 代理
     * @param clazz 接口类
     * @param <T>
     * @return
     */
    public static <T> T create(Class<?> clazz){
        MethodProxy methodProxy  = new MethodProxy(clazz);
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),new Class[]{clazz},methodProxy);
    }
}

/**
 * 动态代理
 */
class MethodProxy implements InvocationHandler{
    Class<?> clazz;

    public MethodProxy(Class<?> clazz){
        this.clazz = clazz;
    }

    /**
     *在代理实例上处理方法调用并返回结果
     * @param proxy  代理类
     * @param method 被代理的方法
     * @param args 该方法的参数数组
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //如果传入的是一个具体实现类，这忽略
        if(Object.class.equals(method.getDeclaringClass())){
            return method.invoke(this,args);
        }
        //否则远程调用服务
        return  rpcInvoke(method,args);
    }


    public Object rpcInvoke(Method method,Object[] args){
        InvokerMsg msg = new InvokerMsg();
        msg.setClassName(this.clazz.getName());
        msg.setMethodName(method.getName());
        msg.setParams(method.getParameterTypes());
        msg.setValues(args);

        final  RpcProxyHandle handle = new RpcProxyHandle();

        EventLoopGroup worksGroup = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(worksGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //解决粘包拆包
                            pipeline.addLast("FrameDecoder",new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,4,0,4));
                            pipeline.addLast("FrameEncoder",new LengthFieldPrepender(4));

                            //编解码
                            pipeline.addLast("encoder",new ObjectEncoder());
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));

                            pipeline.addLast("handle",handle);

                        }
                    });

            ChannelFuture f = b.connect("localhost",8081).sync();

            f.channel().writeAndFlush(msg).sync();

            f.channel().closeFuture().sync();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            worksGroup.shutdownGracefully();
        }

        return handle.getResult();
    }
}
