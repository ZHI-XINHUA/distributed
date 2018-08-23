package demo;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Created by xh.zhi on 2018-8-23.
 * RMI注册表:通过JNDI发布RMI服务
 */
public class HelloServer {

    public static void main(String[] args) {
        try {
            SayHelloImpl sayHello = new SayHelloImpl();

            //创建了一个注册表
            LocateRegistry.createRegistry(8888);
            //将服务器实现绑定到注册表,绑定RMI地址与RMI服务实现类
            Naming.bind("rmi://localhost:8888/sayHello",sayHello);

        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
