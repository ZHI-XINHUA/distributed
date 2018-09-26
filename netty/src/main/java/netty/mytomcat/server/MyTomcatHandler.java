package netty.mytomcat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import netty.mytomcat.http.MyRequest;
import netty.mytomcat.http.MyResponse;
import netty.mytomcat.http.MyServlet;
import netty.mytomcat.util.CustomConfig;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Created by xh.zhi on 2018-9-20.
 */
public class MyTomcatHandler extends ChannelInboundHandlerAdapter {

    private static Logger LOG = Logger.getLogger(MyTomcatHandler.class);

    private static Map<Pattern,Class<?>> servletMapping = new HashMap<Pattern, Class<?>>();

    static {
        CustomConfig.load("web.properties");
        for(String key :CustomConfig.getKeys()){
            if(key.startsWith("servlet")){
                String name = key.replaceFirst("servlet.", "");
                if(name.indexOf(".") != -1){
                    name = name.substring(0,name.indexOf("."));
                }else{
                    continue;
                }
                String pattern = CustomConfig.getString("servlet." + name + ".urlPattern");
                pattern = pattern.replaceAll("\\*", ".*");
                String className = CustomConfig.getString("servlet." + name + ".className");
                if(!servletMapping.containsKey(pattern)){
                    try {
                        servletMapping.put(Pattern.compile(pattern), Class.forName(className));
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) msg;
            MyRequest request = new MyRequest(ctx,httpRequest);
            MyResponse response = new MyResponse(ctx,httpRequest);

            String uri = request.getUri();
            String method = request.getMethod();
            LOG.info(String.format("Uri:%s method %s", uri, method));

            //请求路径匹配
            boolean hasPattern = false;

            for (Map.Entry<Pattern, Class<?>> entry : servletMapping.entrySet()) {
                if (entry.getKey().matcher(uri).matches()) {
                    MyServlet servlet = (MyServlet)entry.getValue().newInstance();
                    if("get".equalsIgnoreCase(method)){
                        servlet.doGet(request, response);
                    }else{
                        servlet.doPost(request, response);
                    }
                    hasPattern = true;
                }
            }

            if(!hasPattern){
                String out = String.format("404 NotFound URL%s for method %s", uri,method);
                response.write(out,404);
                return;
            }

        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }
}
