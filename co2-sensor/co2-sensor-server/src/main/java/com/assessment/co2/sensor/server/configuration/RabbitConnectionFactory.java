/**
 * 
 */
package com.assessment.co2.sensor.server.configuration;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * Factory Interface for creating rabbit connection
 * @author ghosh
 *
 */
public interface RabbitConnectionFactory {
	ConnectionFactory getRabbitConnectionFactory() throws Exception;
	Connection getRabbitConnection(ConnectionFactory connectionFactory) throws IOException, TimeoutException ;
	Channel getRabbitChannel(Connection channel)throws IOException;

}
