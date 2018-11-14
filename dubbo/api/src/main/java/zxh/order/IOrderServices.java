package zxh.order;

import zxh.base.DoRequest;
import zxh.base.DoResponse;

/**
 * 服务提供者暴露的接口api
 */
public interface IOrderServices {
    /**
     * 下单
     * @param request
     * @return
     */
    DoResponse doOrder(DoRequest request);
}
