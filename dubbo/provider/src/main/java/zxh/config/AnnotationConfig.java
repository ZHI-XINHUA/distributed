package zxh.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 注解配置
 */
@Configuration   //注解配置
public class AnnotationConfig {

    /**
     * 应用服务
     * @return
     */
    @Bean //bena
   public ApplicationConfig applicationConfig(){
       ApplicationConfig applicationConfig = new ApplicationConfig();
       applicationConfig.setName("dubbo-provider");
       applicationConfig.setOwner("zhx");
       applicationConfig.setLogger("log4j");
       return applicationConfig;
   }

    /**
     * zookeeper注册中心
     * @return
     */
    @Bean("zkRegistryConfig")
    public  RegistryConfig zkRegistryConfig() {
        RegistryConfig registryConfig = new RegistryConfig();
        //注册中心id
        registryConfig.setId("zk");
        //注册中心地址
        registryConfig.setAddress("zookeeper://192.168.3.31:2182");
        return registryConfig;
    }

    /**
     * multicast注册中心
     * @return
     */
    @Bean("multRegistry")
    public  RegistryConfig multRegistryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setId("multicast");
        registryConfig.setAddress("multicast://224.5.6.7:1234");
        return  registryConfig;
    }


    /**
     * dubbo协议
     * @return
     */
    @Bean("dubboProtocol")
    public  ProtocolConfig getDubboProtocol(){
        return getProtocolConfig("dubbo",20881);
    }

    /**
     * rmi协议
     * @return
     */
    @Bean("rmiProtocol")
    public  ProtocolConfig getRmiProtocol(){
        return getProtocolConfig("rmi",20882);
    }

    /**
     * 获取协议
     * @param protpcol
     * @param port
     * @return
     */
    private  ProtocolConfig getProtocolConfig(String protpcol,int port){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        // 服务协议
        protocolConfig.setName(protpcol);
        //服务端口
        protocolConfig.setPort(port);

        // 服务IP地址(多网卡时使用)
        //protocolConfig.setHost("");

        // 上下文路径
        //protocolConfig.setContextpath("");

        // 线程池类型
        //protocolConfig.setThreadpool("");

        // 线程池大小(固定大小)
        //protocolConfig.setThreads(1);

        // IO线程池大小(固定大小)
        //protocolConfig.setIothreads(1);

        //线程池队列大小
        //protocolConfig.setQueues(1);

        // 最大接收连接数
        //protocolConfig.setAccepts(1);

        // 协议编码
        //protocolConfig.setCodec("");

        // 序列化方式
        // protocolConfig.setSerialization("");

        // 字符集
        //protocolConfig.setCharset("");

        // 最大请求数据长度
        //protocolConfig.setPayload(1);

        // 缓存区大小
        //protocolConfig.setBuffer(100);

        // 心跳间隔
        //protocolConfig.setHeartbeat(100);

        // 访问日志
        //protocolConfig.setAccesslog("");

        // 网络传输方式
        //protocolConfig.setTransporter("");

        // 信息交换方式
        //protocolConfig.setExchanger("");

        // 信息线程模型派发方式
        //protocolConfig.setDispatcher("");

        // 对称网络组网方式
        //protocolConfig.setNetworker("");

        // 服务器端实现
        //protocolConfig.setServer("");

        // 客户端实现
        //protocolConfig.setClient("");

        // 支持的telnet命令，多个命令用逗号分隔
        //protocolConfig.setTelnet("");

        // 命令行提示符
        // protocolConfig.setPrompt("");

        // status检查
        // protocolConfig.setStatus("");

        // 是否注册
        //protocolConfig.setRegister(true);

        return protocolConfig;

    }

    /**
     * provider服务
     * @return
     */
    @Bean("providerConfig1")
    public  ProviderConfig getProviderConfig1(){
        ProviderConfig providerConfig = new ProviderConfig();
        //应用
        providerConfig.setApplication(applicationConfig());
        //注册中心
        providerConfig.setRegistry(zkRegistryConfig());
        //协议
        providerConfig.setProtocol(getDubboProtocol());

        return providerConfig;

    }
}
