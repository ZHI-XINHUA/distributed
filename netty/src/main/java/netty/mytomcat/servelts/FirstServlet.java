package netty.mytomcat.servelts;

import com.alibaba.fastjson.JSON;
import netty.mytomcat.http.MyRequest;
import netty.mytomcat.http.MyResponse;
import netty.mytomcat.http.MyServlet;

/**
 * Created by xh.zhi on 2018-9-21.
 */
public class FirstServlet extends MyServlet {
    @Override
    public void doGet(MyRequest request, MyResponse response) {
        doPost(request,response);
    }

    @Override
    public void doPost(MyRequest request, MyResponse response){
        String str = JSON.toJSONString(request.getParameters(),true);
        response.write(str,200);
    };
}
