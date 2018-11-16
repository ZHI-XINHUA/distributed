package zxh.order;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ReferenceConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import zxh.base.DoRequest;
import zxh.base.DoResponse;

import java.util.concurrent.TimeUnit;

/**
 * 远程调用order接口
 */
public class RemoteOrder {
    public static void main(String[] args) throws Exception {

       // System.in.read();
        for(int i=0;i<20;i++) {
            //apiConsumer();

            xmlConsumer();
        }

    }

    /**
     * xml 方式
     */
    public static void xmlConsumer(){
        //1、加载配置信息
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"consumer-order.xml"});
        context.start();
        //2、 获取远程服务代理
        IOrderServices services = (IOrderServices) context.getBean("orderServices");
        DoRequest request = new DoRequest();
        request.setName("romte server");
        DoResponse response = services.doOrder(request);
        System.out.println(response.toString());

    }

    /**
     * api方式
     */
    public static void apiConsumer(){
        // 当前应用配置
        ApplicationConfig application = new ApplicationConfig();
        application.setName("consumer-order");

        // 连接注册中心配置
        RegistryConfig registry = new RegistryConfig();
        registry.setProtocol("xml 方式实现消费");
        registry.setAddress("192.168.3.31:2182");

        // 注意：ReferenceConfig为重对象，内部封装了与注册中心的连接，以及与服务提供方的连接
        // 引用远程服务
        ReferenceConfig<IOrderServices> reference = new ReferenceConfig<IOrderServices>();
        reference.setApplication(application);// 多个注册中心可以用setRegistries()
        reference.setRegistry(registry);
        reference.setInterface(IOrderServices.class);
        reference.setVersion("1.0.0");

        // 和本地bean一样使用xxxService
        IOrderServices orderServices = reference.get();

        DoRequest request = new DoRequest();
        request.setName("api 方式实现消费");
        DoResponse response = orderServices.doOrder(request);
        System.out.println(response.toString());






    }

}
