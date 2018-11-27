package zxh.config;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ConsumerConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;

/**
 * api方式配置consumer端
 */
public class ApiConfig {

    /**
     * 应用配置
     * @return
     */
    public static ApplicationConfig getApplicationConfig(){
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName("dubbo-consumer");
        applicationConfig.setOwner("james");
        applicationConfig.setLogger("log4j");
        return applicationConfig;
    }

    /**
     * zookeeper注册中心
     * @return
     */
    public static RegistryConfig getZkRegistryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        //注册中心id
        registryConfig.setId("zk");
        //注册中心地址
        registryConfig.setAddress("zookeeper://192.168.3.31:2182");
        return  registryConfig;
    }

    /**
     * Multicast注册中心
     * @return
     */
    public static RegistryConfig getMultRegistryConfig(){
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setId("multicast");
        registryConfig.setAddress("multicast://224.5.6.7:1234");
        return  registryConfig;
    }

    /**
     * 协议
     * @param protocol
     * @param prot
     * @return
     */
    public static ProtocolConfig getProtocolConfig(String protocol,int prot){
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName(protocol);
        protocolConfig.setPort(prot);
        return protocolConfig;
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
     * consumer配置
     * @return
     */
    public static ConsumerConfig getConsumerConfig1(){
        ConsumerConfig consumerConfig = new ConsumerConfig();
        //应用
        consumerConfig.setApplication(getApplicationConfig());
        //注册中心
        consumerConfig.setRegistry(getZkRegistryConfig());
        return consumerConfig;
    }
}
