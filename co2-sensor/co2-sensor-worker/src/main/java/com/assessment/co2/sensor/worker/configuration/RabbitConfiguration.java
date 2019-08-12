/**
 * 
 */
package com.assessment.co2.sensor.worker.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * Configuration class for rabbit
 * 
 * @author ghosh
 *
 */
@PropertySource(value = "classpath:/workerapplication.properties")
@Component
public class RabbitConfiguration implements InitializingBean {
	private DeliverCallback sensorMessageHandler;

	private Environment env;
	private RabbitConnectionFactory rabbitConnectionfactory;

	@Autowired
	public RabbitConfiguration(DeliverCallback sensorMessageHandle, Environment env,
			RabbitConnectionFactory rabbitConnectionfactory) {
		this.sensorMessageHandler = sensorMessageHandle;
		this.env = env;
		this.rabbitConnectionfactory = rabbitConnectionfactory;
	}

	/**
	 * Creates the connection with rabbitMQ and receives the call back from rabbit
	 * when ever there is message in the queue
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		try {
			ConnectionFactory factory = rabbitConnectionfactory.getRabbitMQConnectionFactory();
			String rabbitQueue = env.getProperty("rabbit.queuename");
			if (StringUtils.isBlank(rabbitQueue)) {
				throw new Exception("Mandatory rabbit MQ configuration is missing");
			}

			final Connection connection = rabbitConnectionfactory.getRabbitMQConnection(factory);
			final Channel channel = rabbitConnectionfactory.getRabbitMQChannel(connection);

			channel.queueDeclare(rabbitQueue, true, false, false, null);

			channel.basicQos(1);
			channel.basicConsume(rabbitQueue, true, sensorMessageHandler, consumerTag -> {
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
