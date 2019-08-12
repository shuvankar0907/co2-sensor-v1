/**
 * 
 */
package com.assessment.co2.sensor.worker.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.worker.handler.TestConfig;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RabbitConnectionFactoryImplTest {
	@InjectMocks
	RabbitConnectionFactoryImpl rabbitConnectionfactoryImpl;
	@Mock
	Environment env;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.worker.configuration.RabbitConnectionFactoryImpl#getRabbitMQConnectionFactory()}.
	 * @throws Exception 
	 */
	@Test
	public void testGetRabbitMQConnectionFactory() throws Exception {
		String host="host";
		String port="2345";
		String userName="username";
		String password="password";
		when(env.getProperty("rabbit.host")).thenReturn(host);
		when(env.getProperty("rabbit.port")).thenReturn(port);
		when(env.getProperty("rabbit.username")).thenReturn(userName);
		when(env.getProperty("rabbit.password")).thenReturn(password);
		ConnectionFactory connectionFactory=rabbitConnectionfactoryImpl.getRabbitMQConnectionFactory();
		assertNotNull(connectionFactory);
		assertEquals(host, connectionFactory.getHost());
		assertEquals(Integer.parseInt(port), connectionFactory.getPort());
		assertEquals(userName, connectionFactory.getUsername());
		assertEquals(password, connectionFactory.getPassword());
		
	}
	@Test(expected = Exception.class)
	public void testGetRabbitMQConnectionFactoryHostNotPresent() throws Exception {
		String host="";
		String port="2345";
		String userName="username";
		String password="password";
		when(env.getProperty("rabbit.host")).thenReturn(host);
		when(env.getProperty("rabbit.port")).thenReturn(port);
		when(env.getProperty("rabbit.username")).thenReturn(userName);
		when(env.getProperty("rabbit.password")).thenReturn(password);
		rabbitConnectionfactoryImpl.getRabbitMQConnectionFactory();
		
	}
	@Test(expected = Exception.class)
	public void testGetRabbitMQConnectionFactoryPortNotPresent() throws Exception {
		String host="host";
		String port="";
		String userName="username";
		String password="password";
		when(env.getProperty("rabbit.host")).thenReturn(host);
		when(env.getProperty("rabbit.port")).thenReturn(port);
		when(env.getProperty("rabbit.username")).thenReturn(userName);
		when(env.getProperty("rabbit.password")).thenReturn(password);
		rabbitConnectionfactoryImpl.getRabbitMQConnectionFactory();
		
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.worker.configuration.RabbitConnectionFactoryImpl#getRabbitMQConnection(com.rabbitmq.client.ConnectionFactory)}.
	 * @throws TimeoutException 
	 * @throws IOException 
	 */
	@Test
	public void testGetRabbitMQConnection() throws IOException, TimeoutException {
		ConnectionFactory connectionFactory=mock(ConnectionFactory.class);
		Connection connection=mock(Connection.class);
		when(connectionFactory.newConnection()).thenReturn(connection);
		
		Connection actualConnection=rabbitConnectionfactoryImpl.getRabbitMQConnection(connectionFactory);
		assertEquals(connection, actualConnection);
	}

	/**
	 * Test method for {@link com.assessment.co2.sensor.worker.configuration.RabbitConnectionFactoryImpl#getRabbitMQChannel(com.rabbitmq.client.Connection)}.
	 * @throws IOException 
	 */
	@Test
	public void testGetRabbitMQChannel() throws IOException {
		Connection connection=mock(Connection.class);
		Channel channel=mock(Channel.class);
		when(connection.createChannel()).thenReturn(channel);
		Channel actualChannel=rabbitConnectionfactoryImpl.getRabbitMQChannel(connection);
		assertEquals(channel, actualChannel);
	}

}
