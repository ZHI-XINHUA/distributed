## ActiveMQ简介

[TOC]

ActiveMQ 是一个完全支持JMS1.1和J2EE 1.4规范的 JMS Provider实现。

ActiveMQ主要应用在分布式系统架构中，帮助构建高可用、高性能、可伸缩的企业级面向消息服务的系统。



### Linux安装

1. 官网下载安装包

   官网地址：<http://activemq.apache.org/>

2. 解压

   ```linux
   tar -zxvf apache-activemq-5.15.8-bin.tar.gz 
   ```

1. 进入bin目录，启动activemq

   ```linux
   ./activemq start
   ```

   停止activemq

   ```linux
   ./activemq stop
   ```

2. 测试

   访问activemq的管理页面：http://192.168.1.106:8161。activemq提供了web页面，用的是jetty容器，具体配置可查看`conf/jetty.xml`

   或者ps查看进程:`ps -ef|grep activemq`



   参考官网：http://activemq.apache.org/version-5-getting-started.html#Version5GettingStarted-StartingActiveMQStartingActiveMQ



### ActiveMQ应用场景

1. 异步消息：场景说明：用户注册后，需要发注册邮件和注册短信
2. 应用解耦：场景说明：用户下单后，订单系统需要通知库存系统。
3. 流量削峰：应用场景：秒杀活动，一般会因为流量过大，导致流量暴增，应用容易挂掉。为解决这个问题，一般需要在应用前端加入消息队列。
4. 消息通信：消息通讯是指，消息队列一般都内置了高效的通信机制，因此也可以用在纯的消息通讯。比如实现点对点消息队列，或者聊天室等。

详细查看：https://blog.csdn.net/qinweili751/article/details/80620104



## JMS



![](https://note.youdao.com/yws/public/resource/d834edea9bc9680a8db9cf3308c6ec7d/xmlnote/WEBRESOURCE405a962f84ebf1a727a12f7952531cba/9709)



### JMS基本概念

MS即[Java消息服务](http://baike.baidu.com/view/3292569.htm)（Java Message Service）应用程序接口，是一个[Java平台](http://baike.baidu.com/view/209634.htm)中关于面向[消息中间件](http://baike.baidu.com/view/3118541.htm)（MOM）的[API](http://baike.baidu.com/subview/16068/5889234.htm)，用于在两个应用程序之间，或[分布式系统](http://baike.baidu.com/view/991489.htm)中发送消息，进行异步通信。Java消息服务是一个与具体平台无关的API，绝大多数MOM提供商都对JMS提供支持（百度百科给出的概述）。我们可以简单的理解：两个应用程序之间需要进行通信，我们使用一个JMS服务，进行中间的转发，通过JMS 的使用，我们可以解除两个程序之间的耦合。



### JMS消息模型

　JMS具有两种通信模式：

1. Point-to-Point Messaging Domain （点对点）

1. Publish/Subscribe Messaging Domain （发布/订阅模式）



#### 点对点（P2P）

在点对点通信模式中，应用程序由消息队列，发送方，接收方组成。每个消息都被发送到一个特定的队列，接收者从队列中获取消息。队列保留着消息，直到他们被消费或超时。

**特点：**

- 每个消息只能有一个消费者。
- 消息的生产者和消费者之间没有时间上的相关性。无论消费者在生产者发送消息的时候是否处于运行状态，都可以提取消息。
- 接收方在接收完消息之后，需要向消息队列应答成功



1. 如果session关闭时，有一些消息已经收到，但还没有被签收，那么当消费者下次连接到相同的队列时，消息还会被签收

1. 如果用户在receive方法中设定了消息选择条件，那么不符合条件的消息会留在队列中不会被接收

1. 队列可以长久保存消息直到消息被消费者签收。消费者不需要担心因为消息丢失而时刻与jms provider保持连接状态



#### 发布/订阅模式(pub/sub)

在发布/订阅消息模型中，发布者发布一个消息，该消息通过topic传递给所有的客户端。该模式下，发布者与订阅者都是匿名的，即发布者与订阅者都不知道对方是谁。并且可以动态的发布与订阅Topic。Topic主要用于保存和传递消息，且会一直保存消息直到消息被传递给客户端。

**特点：**

- 每个消息可以有多个消费者
- 消息的生产者和消费者之间存在时间上的相关性，订阅一个主题的消费者只能消费自它订阅之后发布的消息。**JMS规范允许提供客户端创建持久订阅**





1. 订阅可以分为非持久订阅和持久订阅

1. 当所有的消息必须接收的时候，则需要用到持久订阅。反之，则用非持久订阅



#### JMS API

1. ConnectionFactory ：连接工厂
2. Connection：封装客户端与JMS provider之间的一个虚拟的连接
3. Session：生产和消费消息的一个单线程上下文； 用于创建producer、consumer、message、queue...
4. Destination：消息发送或者消息接收的目的地
5. Message Producers：消息生产者
6. Message Consumers：消息消费者
7. Message Listeners：消息监听者



#### 消息头

包含消息的识别信息和路由信息



#### 消息体

- TextMessage 文本消息

- MapMessage  map消息

- BytesMessage  字节消息

- StreamMessage   输入输出流

- ObjectMessage  可序列化对象



#### JMS可靠性机制

JMS消息之后被确认后，才会认为是被成功消费。消息的消费包含三个阶段：客户端接收消息、客户端处理消息、消息被确认

##### 事务性会话

```java
session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
```

- Session createSession(boolean Session createSession(boolean transacted, int acknowledgeMode), int acknowledgeMode)
  - transacted：true表示事务性会话
  - acknowledgeMode：答应模式，transacted=false才生效

设置为true的时候，消息会在`session.commit`以后自动签收

对应生产端，commit后消息才会添加到队列。

对应消费端，commit后消息会从队列中移除。



##### 非事务性会话

```java
session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
```

在该模式下，消息何时被确认取决于创建会话时的应答模式。

- transacted：false 表示非事务性会话

- acknowledgeMode：答应模式

  - AUTO_ACKNOWLEDGE：当客户端成功从recive方法返回以后，或者[MessageListener.onMessage] 方法成功返回以后，会话会自动确认该消息（即不用`session.commit`）

  - CLIENT_ACKNOWLEDGE：**只设置客户端**，客户端通过调用消息的`textMessage.acknowledge();`确认消息。

    ```
    在这种模式中，如果一个消息消费者消费一共是10个消息，那么消费了5个消息，然后在第5个消息通过textMessage.acknowledge()，那么之前的所有消息都会被消确认
    ```

  - DUPS_OK_ACKNOWLEDGE:延迟确认



##### 本地事物

在一个JMS客户端，可以使用本地事务来组合消息的发送和接收。JMS Session 接口提供了commit和rollback方法。

JMS Provider会缓存每个生产者当前生产的所有消息，直到commit或者rollback，commit操作将会导致事务中所有的消息被持久存储；rollback意味着JMS Provider将会清除此事务下所有的消息记录。在事务未提交之前，消息是不会被持久化存储的，也不会被消费者消费

事务提交意味着生产的所有消息都被发送。消费的所有消息都被确认；

事务回滚意味着生产的所有消息被销毁，消费的所有消息被恢复，也就是下次仍然能够接收到发送端的消息，除非消息已经过期了







https://www.cnblogs.com/jaycekon/p/6220200.html