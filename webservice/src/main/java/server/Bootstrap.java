package server;

import javax.xml.ws.Endpoint;

public class Bootstrap {

    public static void main(String[] args) {
        //发布
        Endpoint.publish("http://localhost:9090/hello",new SayHelloImpl());

        System.out.println("发布成功！");
    }
}
