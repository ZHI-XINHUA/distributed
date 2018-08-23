package demo;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Created by xh.zhi on 2018-8-23.
 */
public class HelloClient {
    public static void main(String[] args) {
        //调用地址
        String url = "rmi://localhost:8888/sayHello";
        try {
            //调用服务
            ISayHello sayHello = (ISayHello) Naming.lookup(url);
            String msg = sayHello.sayHello("lbj");
            System.out.println(msg);
        } catch (NotBoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
