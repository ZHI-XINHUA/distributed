package zxh.config;

import com.alibaba.dubbo.config.ServiceConfig;
import zxh.order.IOrderServices;
import zxh.order.OrderServicesImpl;

/**
 * Created by xh.zhi on 2018-11-27.
 */
public class ExportServer {

    public void apiExportService(){
        //实现类
        IOrderServices services = new OrderServicesImpl();

        ServiceConfig<IOrderServices> serviceConfig = new ServiceConfig<IOrderServices>();
        //设置provider的配置
        serviceConfig.setProvider(ApiConfig.getProviderConfig1());
        //设置版本
        serviceConfig.setVersion("1.0.0");
        //接口
        serviceConfig.setInterface(IOrderServices.class);
        //实现类
        serviceConfig.setRef(services);

        //暴露接口
        serviceConfig.export();
    }
}
