package netty.mytomcat.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;

import java.util.List;
import java.util.Map;

/**
 * Created by xh.zhi on 2018-9-20.
 *  自定义Request
 */
public class MyRequest {
    private ChannelHandlerContext ctx;
    private HttpRequest request;

    public MyRequest(ChannelHandlerContext cxt,HttpRequest request){
        this.ctx = cxt;
        this.request = request;
    }

    /**
     * 获取uri
     * @return
     */
    public String getUri(){
        return request.uri();
    }

    /**
     * 获取方法名
     * @return
     */
    public String getMethod(){
        return request.method().name().toString();
    }

    /**
     * 获取参数
     * @return
     */
    public  Map<String,List<String>> getParameters(){
        QueryStringDecoder decoder = new QueryStringDecoder(request.uri());
        return  decoder.parameters();
    }

    /**
     * 获取参数
     * @param name
     * @return
     */
    public String getParameter(String name){
        Map<String,List<String>> params = getParameters();
        List<String> list = params.get(name);
        if(list==null){
            return null;
        }
        return list.get(0);

    }
}
