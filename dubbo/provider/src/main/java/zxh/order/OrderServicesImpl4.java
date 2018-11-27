package zxh.order;

import com.alibaba.dubbo.config.annotation.Service;
import zxh.base.DoRequest;
import zxh.base.DoResponse;

/**
 * 服务提供者实现
 */
@Service(protocol = "providerConfig1",timeout = 500) //dubbo service
public class OrderServicesImpl4 implements IOrderServices{
    @Override
    public DoResponse doOrder(DoRequest request) {
        System.out.println("OrderServicesImpl4》请求已进来..."+request);
        DoResponse response = new DoResponse();
        response.setCode("200");
        response.setResult("处理成功");
        response.setDemo("test");
        return response;
    }
}
