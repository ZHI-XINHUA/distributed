package server;

import javax.jws.WebService;

@WebService
public class SayHelloImpl implements  ISayHello {
    @Override
    public String sayHelo(String name) {
        return "hello,"+name;
    }
}
