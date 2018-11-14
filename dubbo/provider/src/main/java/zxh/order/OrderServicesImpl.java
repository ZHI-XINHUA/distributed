package zxh.order;

import zxh.base.DoRequest;
import zxh.base.DoResponse;

/**
 * 服务提供者实现
 */
public class OrderServicesImpl implements IOrderServices{
    @Override
    public DoResponse doOrder(DoRequest request) {
        System.out.println("请求已进来..."+request);
        DoResponse response = new DoResponse();
        response.setCode("200");
        response.setResult("处理成功");
        response.setDemo("test");
        return response;
    }
}
