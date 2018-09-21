package netty.mytomcat.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import netty.mytomcat.http.MyRequest;
import netty.mytomcat.http.MyResponse;
import netty.mytomcat.http.MyServlet;
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

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg instanceof HttpRequest){
            HttpRequest httpRequest = (HttpRequest) msg;
            MyRequest request = new MyRequest(ctx,httpRequest);
            MyResponse response = new MyResponse(ctx,httpRequest);

            String uri = request.getUri();
            String methodName = request.getMethod();
            System.out.println("uri="+uri+" methodName="+methodName);

          //  MyServlet servlet  =(MyServlet)Class.forName("netty.mytomcat.http.MyServlet").newInstance();
           // System.out.println(servlet);
          //  if("GET".equals(methodName)){
          //      servlet.doGet(request,response);
          //  }else{
          //      servlet.doPost(request,response);
           // }response

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
