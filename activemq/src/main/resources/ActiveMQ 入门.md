# ActiveMQ 入门

## Linux安装

1. 官网下载安装包

   官网地址：<http://activemq.apache.org/>

2. 解压

   ```linux
   tar -zxvf apache-activemq-5.15.8-bin.tar.gz 
   ```


3. 进入bin目录，启动activemq

   ```linux
   ./activemq start
   ```

   停止activemq

   ```linux
   ./activemq stop
   ```

4. 测试

   访问activemq的管理页面：http://192.168.1.106:8161。activemq提供了web页面，用的是jetty容器，具体配置可查看`conf/jetty.xml`

   或者ps查看进程:`ps -ef|grep activemq`



   参考官网：http://activemq.apache.org/version-5-getting-started.html#Version5GettingStarted-StartingActiveMQStartingActiveMQ
