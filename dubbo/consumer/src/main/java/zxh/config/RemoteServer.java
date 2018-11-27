package zxh.config;

import com.alibaba.dubbo.config.ReferenceConfig;
import zxh.base.DoRequest;
import zxh.base.DoResponse;
import zxh.order.IOrderServices;

/**
 * Created by xh.zhi on 2018-11-27.
 */
public class RemoteServer {

    public void remoteService(){
        ReferenceConfig<IOrderServices> referenceConfig = new ReferenceConfig<>();
        //consumer配置
        referenceConfig.setConsumer(ApiConfig.getConsumerConfig1());
        //版本
        referenceConfig.setVersion("1.0.0");
        //接口
        referenceConfig.setInterface(IOrderServices.class);

        //调用dubbo服务
        IOrderServices orderServices = referenceConfig.get();
        DoRequest request = new DoRequest();
        request.setName("api方式》远程调用服务...");
        DoResponse response = orderServices.doOrder(request);
        System.out.println(response.toString());

    }
}
