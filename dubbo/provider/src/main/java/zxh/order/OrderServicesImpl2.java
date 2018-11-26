package zxh.order;

import zxh.base.DoRequest;
import zxh.base.DoResponse;

/**
 * IOrderServices 实现版本2
 */
public class OrderServicesImpl2 implements IOrderServices {
    @Override
    public DoResponse doOrder(DoRequest request) {
        System.out.println("OrderServicesImpl2》请求已进来..." + request);
        DoResponse response = new DoResponse();
        response.setCode("200");
        response.setResult("处理成功");
        response.setDemo("test");
        return response;
    }
}
