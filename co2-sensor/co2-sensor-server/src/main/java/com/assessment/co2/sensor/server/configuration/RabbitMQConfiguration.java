package com.assessment.co2.sensor.server.configuration;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Configuration class for RabbitMQ
 * 
 * @author ghosh
 *
 */
@PropertySource(value = "classpath:/application.properties")
@Configuration
public class RabbitMQConfiguration {
	@Autowired
	Environment env;

	private static String QUEUENAME;
	private RabbitConnectionFactory rabbitConnectionFactory;

	public RabbitMQConfiguration(RabbitConnectionFactory rabbitConnectionFactory, Environment env) {
		this.rabbitConnectionFactory = rabbitConnectionFactory;
		this.env = env;
	}

	/**
	 * Creates the connection factory for the rabbitMQ
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean("rabbitconnectionfactory")
	public ConnectionFactory rabbitConnectionFactory() throws Exception {
		try {
			return rabbitConnectionFactory.getRabbitConnectionFactory();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Creates the rabbit channel
	 * 
	 * @return
	 * @throws Exception
	 */
	@Bean("rabbitchannel")
	public Channel rabbitchannel(){
		try {
			QUEUENAME = env.getProperty("rabbit.queuename");
			if (StringUtils.isBlank(QUEUENAME)) {
				throw new Exception("Mandatory rabbit MQ configuration is missing");
			}

			Connection connection = rabbitConnectionFactory.getRabbitConnection(rabbitConnectionFactory());
			Channel channel = rabbitConnectionFactory.getRabbitChannel(connection);
			channel.queueDeclare(QUEUENAME, true, false, false, null);
			return channel;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String queueName() {
		return QUEUENAME;
	}
}
