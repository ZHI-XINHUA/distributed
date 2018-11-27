package zxh.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.ProviderConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * API方式配置provider
 */
public class ApiConfig {

    /**
     * dubbo应用配置
     * @return
     */
    public static ApplicationConfig getApplicationConfig(){
        ApplicationConfig application = new ApplicationConfig();
        //应用名称
        application.setName("dubbo-provider");
        //应用负责人
        application.setOwner("zxh");
        //日志输出方式
        application.setLogger("log4j");

        //Java代码编译器
        //application.setCompiler("");

        //环境，如：dev/test/run
        //application.setOrganization("");

        //版本
        //application.setVersion("");

        //服务监控
        //application.setMonitor("");

        //注册中心
        //application.setRegistry(null);

        //分层
        //application.setArchitecture("");

        //组织名(BU或部门)
        //application.setOrganization("");

        return application;
    }

    public static RegistryConfig getZkRegistryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        //注册中心id
        registryConfig.setId("zk");
        //注册中心地址
        registryConfig.setAddress("zookeeper://192.168.3.31:2182");
        //注册中心协议，如果setAddress地址没有协议，则可以设置协议值。（zookeeper\multicast\redis）
        //registryConfig.setProtocol("zookeeper");

        //是否默认注册中心，默认true
        //registryConfig.setDefault(true);

        //只订阅，禁止注册(在该注册中心上服务是否暴露)
        //registryConfig.setRegister(false);

        //只注册(在该注册中心上服务是否引用)
        //registryConfig.setSubscribe(false);

        //启动时检查,关闭检查。默认false
        //registryConfig.setCheck(false);

        //注册中心用户名
        //registryConfig.setUsername("test");

        //注册中心密码
        //registryConfig.setPassword("test");

        //注册中心缺省端口
        //registryConfig.setPort("2188");

        //注册中心请求超时时间(毫秒)
        //registryConfig.setTimeout(500);

        //注册中心会话超时时间(毫秒)
        //registryConfig.setSession(1000);

        //动态注册中心列表存储文件
        //registryConfig.setFile("file");

        //在该注册中心上注册是动态的还是静态的服务
        //registryConfig.setDynamic(true);

        //客户端实现
        //registryConfig.setTransporter("");

        //日后补充，暂时不理解其作用
//        registryConfig.setClient("");
//        registryConfig.setCluster("");
//        registryConfig.setGroup("");
//        registryConfig.setServer("");
//        registryConfig.setVersion("");
        return registryConfig;
    }

    /**
     * multicast注册中心
     * @return
     */
    public static RegistryConfig multRegistryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setId("multicast");
        registryConfig.setAddress("multicast://224.5.6.7:1234");
        return  registryConfig;
    }


    /**
     * dubbo协议
     * @return
     */
    public static ProtocolConfig getDubboProtocol(){
        return getProtocolConfig("dubbo",20881);
    }

    /**
     * rmi协议
     * @return
     */
    public static ProtocolConfig getRmiProtocol(){
        return getProtocolConfig("rmi",20882);
    }

    /**
     * 获取协议
     * @param protpcol
     * @param port
     * @return
     */
    private static ProtocolConfig getProtocolConfig(String protpcol,int port){
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
    public static ProviderConfig getProviderConfig1(){
        ProviderConfig providerConfig = new ProviderConfig();
        //应用
        providerConfig.setApplication(getApplicationConfig());
        //注册中心
        providerConfig.setRegistry(getZkRegistryConfig());
        //协议
        providerConfig.setProtocol(getDubboProtocol());

        return providerConfig;

    }
}
