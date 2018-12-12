# JDK SPI机制

SPI全称Service Provider Interface，是Java提供的一套用来被第三方实现或者扩展的API，它可以用来启用框架扩展和替换组件。

## 机制介绍

SPI机制是一种将服务接口与服务实现分离以达到解耦、大大提升了程序可扩展性的机制。引入服务提供者就是引入了spi接口的实现者，通过本地的注册发现获取到具体的实现类，轻松可插拔。

在面向对象的设计里，一般推荐模块之间基于接口编程，模块之间不对实现类进行[硬编码](https://baike.baidu.com/item/%E7%A1%AC%E7%BC%96%E7%A0%81/8070173?fr=aladdin)，一旦代码涉及具体的实现类就违反了可拔插的原则。 SPI就是解决这问题的一个机制：为某个接口寻找服务实现的机制。

![](https://note.youdao.com/yws/public/resource/66b4bd894a7e8d96d8fb292b7d42402d/xmlnote/C05A793D125D4C9B9DDAAD7E230EF6CA/9538)



ps：

- 硬编码：硬编码数据通常只能通过**编辑源代码**和**重新编译**可执行文件来修改。
- 软编码（非硬编码）：软编码可以在**运行时**确定，修改



## SPI的约定

SPI要找到接口的具体实现类，必须对应关联信息。所以，JDK SPI约定在`META-INF`目录下必须存在`services`包，包里面存储接口与具体实现类的对应关系信息（以服务接口命名的文件，其内容是该接口的具体实现类）。如：`rpc.RpcExtension` 接口的实现类有`mykafka.KafkaExtension`和`rocketmq.RocketMqExtension`

则对应的约定是：

![](https://note.youdao.com/yws/public/resource/66b4bd894a7e8d96d8fb292b7d42402d/xmlnote/D5E57B3981134E129E98672792267E0B/9536)



## 应用场景

- 数据库驱动加载接口实现类的加载 JDBC加载不同类型数据库的驱动
- 日志门面接口实现类加载 SLF4J加载不同提供商的日志实现类
- 适用于接口扩展或替换框架的实现策略等等...



## 简单demo

结构如下：

![](https://note.youdao.com/yws/public/resource/66b4bd894a7e8d96d8fb292b7d42402d/xmlnote/2FB9818F331C4EA1BC29A95C46BF4F41/9540)

当然，实际情况更多的和demo不一样。如接口实现类更多的是不同厂商对服务接口的不同实现，如： 数据库驱动类Oracle\Mysql\SqlServer等等，项目中需要导入对应的驱动jar。

1、接口定义Rpc的启动框架 RpcExtension.java

```java
package rpc;

public interface RpcExtension {
	 void initRpc();
}

```

2、具体的接口实现 Kafka、RocketMq

KafkaExtension.java

```java
package mykafka;
import rpc.RpcExtension;

public class KafkaExtension implements RpcExtension {
	@Override
	public void initRpc() {
		System.out.println("init Kafka......");

	}

}
```

RocketMqExtension.java

```java
package rocketmq;
import rpc.RpcExtension;

public class RocketMqExtension implements RpcExtension {

	@Override
	public void initRpc() {
		System.out.println("init RocketMq......");

	}

}
```



3、创建约定 `META-INF\services\rpc.RpcExtension`

```text
mykafka.KafkaExtension
rocketmq.RocketMqExtension
```



4、测试，使用ServiceLoader加载接口的实现类，迭代出全部实现类。

```java
public static void main(String[] args) {
		ServiceLoader<RpcExtension> loader = ServiceLoader.load(RpcExtension.class);
		Iterator<RpcExtension> it = loader.iterator();
		while(it.hasNext()) {
			RpcExtension service = it.next();
			service.initRpc();
		}

	}
```

