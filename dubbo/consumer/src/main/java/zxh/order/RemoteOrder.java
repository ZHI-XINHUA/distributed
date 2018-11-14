package zxh.order;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import zxh.base.DoRequest;
import zxh.base.DoResponse;

/**
 * 远程调用order接口
 */
public class RemoteOrder {
    public static void main(String[] args) {
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
}
