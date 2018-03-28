package cn.joe;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 用来生产消息
 * 
 * @author wanqiao
 *
 */

public class JMSConsumer {

	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER; // 默认连接用户名
	private static final String PASSWD = ActiveMQConnection.DEFAULT_PASSWORD;
	private static final String BROKERURL = ActiveMQConnection.DEFAULT_BROKER_URL; // 连接地址
	private static final int SENDNUM = 10; // 发送的消息次数

	public static void main(String[] args) {
		ConnectionFactory connectionFactory;// 连接工厂
		Connection connection = null; // 连接
		Session session; // 会话接收或发送消息的线程
		Destination destination;// 消息发送的地址
		MessageConsumer messageConsumer; // 消息发送者生产者

		// 实例化连接工厂
		connectionFactory = new ActiveMQConnectionFactory(USERNAME, PASSWD, BROKERURL);
		// 通过连接工厂获取连接
		try {
			connection = connectionFactory.createConnection();
			connection.start();
			// 创建session 是否加事务操作 消息确认的方式 (消费不需要加事务)
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			// 创建连接的消息队列
			destination = session.createQueue("FirestQueue");

			messageConsumer = session.createConsumer(destination);
			while(true){
				TextMessage receive = (TextMessage) messageConsumer.receive(10);
				if(receive != null){
					System.out.println("收到的消息 ： " + receive.getText());
				}
			}
			// 因为上面提交了事务 需要commit下

			//close 消费者连接才会断开 否则一直连着
			//session.close();
		} catch (JMSException e) {
			System.out.println(e.getMessage());
		}finally {
			/*if (connection != null){
				try {
					connection.close();
				} catch (JMSException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
			
		}
	}

}
