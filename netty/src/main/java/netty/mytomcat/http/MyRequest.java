package netty.mytomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * Created by xh.zhi on 2018-9-20.
 */
public class MyRequest {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public MyRequest(ChannelHandlerContext cxt,HttpRequest request){
        this.ctx = cxt;
        this.request = request;
    }

    public String getUri(){
        return request.uri();
    }

    public String getMethod(){
        return request.method().name().toString();
    }

    public  Map<String,List<String>> getParameters(){
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return  decoder.parameters();
    }

    public String getParameter(String name){
        Map<String,List<String>> params = getParameters();
        List<String> list = params.get(name);
        if(list==null){
            return null;
        }
        return list.get(0);

    }
}
