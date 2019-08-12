/**
 * 
 */
package com.assessment.co2.sensor.worker.configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.core.env.Environment;

/**
 * @author ghosh
 *
 */
@Component
public class RabbitConnectionFactoryImpl implements RabbitConnectionFactory {

	Environment env;

	public RabbitConnectionFactoryImpl(Environment env) {
		this.env = env;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.assessment.co2.sensor.worker.configuration.RabbitConnectionFactory#
	 * getRabbitMQConnectionFactory()
	 */
	@Override
	public ConnectionFactory getRabbitMQConnectionFactory() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		String host = env.getProperty("rabbit.host");
		String port = env.getProperty("rabbit.port");
		String userName = env.getProperty("rabbit.username");
		String password = env.getProperty("rabbit.password");
		if (StringUtils.isBlank(host) | StringUtils.isBlank(port)) {
			throw new Exception("Mandatory rabbit MQ configuration is missing");
		}
		factory.setHost(host);
		factory.setPort(Integer.parseInt(port));
		if (!StringUtils.isBlank(userName)) {
			factory.setUsername(userName);
		}
		if (!StringUtils.isBlank(password)) {
			factory.setPassword(password);
		}
		return factory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.assessment.co2.sensor.worker.configuration.RabbitConnectionFactory#
	 * getRabbitMQConnection(com.rabbitmq.client.ConnectionFactory)
	 */
	@Override
	public Connection getRabbitMQConnection(ConnectionFactory connectionFactory) throws IOException, TimeoutException {
		return connectionFactory.newConnection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.assessment.co2.sensor.worker.configuration.RabbitConnectionFactory#
	 * getRabbitMQChannel(com.rabbitmq.client.Channel)
	 */
	@Override
	public Channel getRabbitMQChannel(Connection connection) throws IOException {
		return connection.createChannel();
	}

}
