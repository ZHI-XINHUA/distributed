package netty.rpc.provider;

import netty.rpc.api.IRpcHello;

public class RpcHello implements IRpcHello {
    @Override
    public String sayHi(String name) {
        return "hi:"+name;
    }

}
