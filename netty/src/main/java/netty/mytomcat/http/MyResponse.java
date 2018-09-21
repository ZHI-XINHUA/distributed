package netty.mytomcat.http;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;



import java.util.HashMap;
import java.util.Map;

/**
 * Created by xh.zhi on 2018-9-20.
 *  自定义Response
 */
public class MyResponse {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    /**自定义影响状态**/
    private static Map<Integer,HttpResponseStatus> responseStatusMap = new HashMap<Integer,HttpResponseStatus>();

    static {
        responseStatusMap.put(200,HttpResponseStatus.OK);
        responseStatusMap.put(400,HttpResponseStatus.NOT_FOUND);
    }

    public MyResponse(ChannelHandlerContext ctx,HttpRequest request){
        this.ctx = ctx;
        this.request = request;
    }

    public void write(String outString,Integer status){
        try{
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,responseStatusMap.get(status),
                    Unpooled.wrappedBuffer(outString.getBytes("UTF-8")));

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/json");
           // response.headers().set(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
            //response.headers().set(HttpHeaderNames.EXPIRES,0);
            response.headers().addInt(HttpHeaderNames.CONTENT_LENGTH,response.content().readableBytes());
            response.headers().addInt(HttpHeaderNames.EXPIRES,0);
            if(HttpHeaderUtil.isKeepAlive(request)){
                response.headers().set(HttpHeaderNames.CONNECTION,HttpHeaderValues.KEEP_ALIVE);
            }

            ctx.write(response);
        }catch (Exception e){
            e.printStackTrace();
        }finally{
            ctx.flush();
        }
    }
}
