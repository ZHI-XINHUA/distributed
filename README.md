# 知识简要归纳


## ActiveMQ

ActiveMQ 是一个完全支持JMS1.1和J2EE 1.4规范的 JMS Provider实现。

ActiveMQ主要应用在分布式系统架构中，帮助构建高可用、高性能、可伸缩的企业级面向消息服务的系统。



### ActiveMQ应用场景

1. 异步消息：如 用户注册后，需要发注册邮件和注册短信。
2. 应用解耦：如 用户下单后，订单系统需要通知库存系统。
3. 流量削峰：如 秒杀活动。
4. 消息通信：如 聊天室等。



### 点对点（P2P） 模型

**code:**

[生产者](https://github.com/ZHI-XINHUA/distributed/blob/master/activemq/src/main/java/zxh/provider/JmsSender.java)

[消费者](https://github.com/ZHI-XINHUA/distributed/blob/master/activemq/src/main/java/zxh/consumer/JmsReciver.java)



### 发布/订阅模式(pub/sub)模型

code:

[生产者](https://github.com/ZHI-XINHUA/distributed/blob/master/activemq/src/main/java/zxh/provider/JmsTopicSender.java)

[消费者](https://github.com/ZHI-XINHUA/distributed/blob/master/activemq/src/main/java/zxh/consumer/JmsTopicReciver.java)

[持久化订阅](https://github.com/ZHI-XINHUA/distributed/blob/master/activemq/src/main/java/zxh/consumer/JmsTopicPersistentReciver.java)


