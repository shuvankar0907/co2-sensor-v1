/**
 * 
 */
package com.assessment.co2.sensor.worker.configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ghosh
 *
 */
public interface RabbitConnectionFactory {
	ConnectionFactory getRabbitMQConnectionFactory() throws Exception ;
	Connection getRabbitMQConnection(ConnectionFactory connectionFactory) throws IOException, TimeoutException ;
	Channel getRabbitMQChannel(Connection channel)throws IOException;

}
