/**
 * 
 */
package com.assessment.co2.sensor.worker.configuration;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.worker.handler.TestConfig;
import com.rabbitmq.client.AMQP.Queue.DeclareOk;
import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RabbitConfigurationTest {

	@InjectMocks
	RabbitConfiguration rabbitConfiguration;
	
	@Mock
	RabbitConnectionFactory rabbitConnectionFactory;
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
	 * Test method for {@link com.assessment.co2.sensor.worker.configuration.RabbitConfiguration#afterPropertiesSet()}.
	 * @throws Exception 
	 */
	@Test
	public void testAfterPropertiesSet() throws Exception {
		String queue="Queue";
		String value="value";
		when(env.getProperty("rabbit.queuename")).thenReturn(queue);
		ConnectionFactory connectionFactory=mock(ConnectionFactory.class);
		Connection connection=mock(Connection.class);
		Channel channel=mock(Channel.class);
		DeclareOk declareOk=mock(DeclareOk.class);
		when(rabbitConnectionFactory.getRabbitMQConnectionFactory()).thenReturn(connectionFactory);
		when(rabbitConnectionFactory.getRabbitMQConnection(connectionFactory)).thenReturn(connection);
		when(rabbitConnectionFactory.getRabbitMQChannel(connection)).thenReturn(channel);
		when(channel.queueDeclare(queue,true, false, false, null)).thenReturn(declareOk);
		
		when(channel.basicConsume(eq(queue), eq(true), any(DeliverCallback.class), any(CancelCallback.class))).thenReturn(value);
		
		
		rabbitConfiguration.afterPropertiesSet();
		
		verify(channel,times(1)).basicConsume(eq(queue), eq(true), any(DeliverCallback.class), any(CancelCallback.class));
	}

}
