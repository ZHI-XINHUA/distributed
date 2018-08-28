package jdk;

import javax.jws.WebService;

@WebService
public class SayHelloImpl implements jdk.ISayHello {
    @Override
    public String sayHelo(String name) {
        return "hello,"+name;
    }
}
