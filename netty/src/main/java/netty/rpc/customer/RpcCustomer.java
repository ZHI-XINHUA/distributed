package netty.rpc.customer;

import netty.rpc.api.IRpcHello;
import netty.rpc.customer.proxy.RpcProxy;
import netty.rpc.provider.RpcHello;

public class RpcCustomer {
    public static void main(String[] args) {
        IRpcHello hello = RpcProxy.create(IRpcHello.class);
        System.out.println(hello.sayHi("james"));
    }
}
