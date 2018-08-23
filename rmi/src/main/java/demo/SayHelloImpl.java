package demo;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by xh.zhi on 2018-8-23.
 * 扩展UnicastRemoteObject，这个类的构造器使得它的对象可供远程访问。
 */
public class SayHelloImpl extends UnicastRemoteObject implements  ISayHello {


    protected SayHelloImpl() throws RemoteException {
    }

    @Override
    public String sayHello(String name) throws RemoteException {
        return " hello "+name;
    }
}
