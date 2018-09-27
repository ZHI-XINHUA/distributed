package netty.rpc.registry;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.rpc.core.msg.InvokerMsg;

import java.io.File;
import java.net.URL;
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
        System.out.println("RegisterHandler");
        //扫描类
        scanClass("netty.rpc.provider");
        //注册，把扫描到的类实例 放到map中
        doRegister();
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Object result = new Object();
        InvokerMsg request = (InvokerMsg) msg;

        //如果请求的服务已注册
        if(registryMap.containsKey(request.getClassName())){
            //获取注册的类
            Object clazz = registryMap.get(request.getClassName());
            //调用服务
            result = clazz.getClass().getMethod(request.getMethodName(),request.getParams()).invoke(clazz,request.getValues());

        }
        ctx.writeAndFlush(result);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


    /**
     * 扫描provider类
     * @param packageName
     */
    private void scanClass(String packageName){
        //读取扫描的包
        URL url = this.getClass().getClassLoader().getResource(packageName.replace(".","/"));

        File dir = new File(url.getFile());
        for(File file : dir.listFiles()){
            //如果file是目录，则继续遍历改目录下的文件
            if(file.isDirectory()){
                scanClass(packageName+"."+file.getName());
            }else{//否则获取全路径文件
                classCache.add(packageName+"."+file.getName().replace(".class","").trim());
            }
        }
    }

    /**
     * 注册
     * 把扫描到的类实例 放到map中，这就是模拟注册过程
     * 注册的服务名字 是接口名称
     *  约定优于配置
     */
    private void doRegister(){
        //如果没有扫描到类，这没有可注册的
        if(classCache.size()==0){
            return;
        }

        for(String classPath :classCache){
            try {
                Class<?> clazz = Class.forName(classPath);
                //获取接口名称
                Class<?> interfaceClass = clazz.getInterfaces()[0];
                registryMap.put(interfaceClass.getName(),clazz.newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }






















}
