package zxh.consumer;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * 消息消费端
 * 发布/订阅（pub/sub） 模式
 */
public class JmsTopicReciver {
    private static String brokerURL = "tcp://192.168.1.106:61616";
    public static void main(String[] args) {
        //jms连接工厂
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(brokerURL);
        //jsm连接
        Connection connection = null;

        try {
            //创建连接
            connection = connectionFactory.createConnection();
            //启动
            connection.start();

            //创建会话
            Session session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);


            //创建队列,destination:目的地
            Destination destination = session.createTopic("first-topic");

            //创建消息接收者
            MessageConsumer consumer = session.createConsumer(destination);

            boolean hasMessage = true;



            while (hasMessage){

                //获取信息
                TextMessage message = (TextMessage) consumer.receive();
                if(message!=null){
                    System.out.println("收到的消息："+message.getText());

                    //提交session，消息从队列中移除
                    session.commit();
                }else {
                    hasMessage = false;
                }

            }

            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if(connection!=null) connection.close();
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
