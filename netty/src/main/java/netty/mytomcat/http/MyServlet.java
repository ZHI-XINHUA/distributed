package netty.mytomcat.http;

/**
 * Created by xh.zhi on 2018-9-20.
 *  自定义servlet
 */
public abstract  class MyServlet {
    public void doGet(MyRequest request,MyResponse response) {}

    public void doPost(MyRequest request,MyResponse response){};
}
