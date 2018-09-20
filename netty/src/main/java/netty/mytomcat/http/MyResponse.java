package netty.mytomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xh.zhi on 2018-9-20.
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
}
