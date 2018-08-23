package demo;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by xh.zhi on 2018-8-23.
 * 远程对象的接口必须扩展Remote接口，它位于java.rmi包中。接口中所有的方法必须声明抛出RemoteException异常
 * 因为远程方法总是存在失败的可能，所以java编程语言要求每一次远程方法的调用都必须捕获RemoteException,并且指明当调用不成功时应执行的相应处理操作。
 */
public interface ISayHello extends Remote {

    public String sayHello(String name) throws RemoteException;
}
