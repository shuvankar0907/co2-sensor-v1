/**
 * 
 */
package com.assessment.co2.sensor.domain.business;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.assessment.co2.sensor.domain.business.Rule;
import com.assessment.co2.sensor.domain.business.RuleContext;
import com.assessment.co2.sensor.domain.model.Sensor;
import com.assessment.co2.sensor.domain.model.Status;
import com.assessment.co2.sensor.domain.repository.SensorStatusRepository;
import com.assessment.co2.sensor.domain.repository.integration.TestConfig;

/**
 * @author ghosh
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class RuleTest {
	
	private final int BELOW_RANGE=1800;
	private final int ABOVE_RANGE=2200;
	private final int PERMITTED_RANGE=2000;

	@Mock
    RuleContext ruleContext;
	
	@Mock 
	SensorStatusRepository sensorStatusRepository;
	
	@Mock
	@Autowired
	RuleContextFactory ruleContextFactory;
	
	@InjectMocks
	RuleImpl rule;
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
	 * Test method for {@link com.assessment.co2.sensor.domain.domain.business.Rule#executeRule(com.assessment.co2.sensor.domain.domain.business.RuleContext)}.
	 */
	@Test
	public void testExecuteRuleCurrentStatusOKAndNewLevelisBelowRange() {
		//RuleContext ruleContext
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		//sensor.setStatus(Status.OK);
		sensor.setLevel(1800);
		
		Sensor sensorMock=mock(Sensor.class);
		
		//RuleContext ruleContext=new ;
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.OK);
		when(ruleContext.getNewCo2level()).thenReturn(BELOW_RANGE);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.OK);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.OK, ruleContext1.getNewSensor().getStatus());
		
	}
	
	/**
	 * Test method for {@link com.assessment.co2.sensor.domain.domain.business.Rule#executeRule(com.assessment.co2.sensor.domain.domain.business.RuleContext)}.
	 */
	@Test
	public void testExecuteRuleCurrentStatusOKAndNewLevelisAboveRange() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		//sensor.setStatus(Status.WARN);
		sensor.setLevel(2800);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.OK);
		when(ruleContext.getNewCo2level()).thenReturn(ABOVE_RANGE);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.WARN);
		RuleContext context1=rule.executeRule(sensor);
		assertEquals(Status.WARN, context1.getNewSensor().getStatus());
//		verify(this.ruleContext, times(0)).saveAlert(Status.ALERT);
//		verify(this.ruleContext, times(1)).saveNewLevel(Status.WARN);
	}
	
	@Test
	public void testExecuteRuleCurrentStatusOKAndNewLevelisEqualToPermittedRange() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2000);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.OK);
		when(ruleContext.getNewCo2level()).thenReturn(PERMITTED_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.OK);
		
		RuleContext context1=rule.executeRule(sensor);
		assertEquals(Status.OK, context1.getNewSensor().getStatus());
		
	}
	
	@Test
	public void testExecuteRuleCurrentStatusWARNAndNewLevelisBELOWRange() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(1900);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.WARN);
		when(ruleContext.getNewCo2level()).thenReturn(BELOW_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.OK);
		
		RuleContext context1=rule.executeRule(sensor);
		assertEquals(Status.OK, context1.getNewSensor().getStatus());
	
	}
	
	@Test
	public void testExecuteRuleCurrentStatusWARNAndNewLevelisEqualToPermittedLevel() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2000);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.WARN);
		when(ruleContext.getNewCo2level()).thenReturn(PERMITTED_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.OK);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		
		
		assertEquals(Status.OK, ruleContext1.getNewSensor().getStatus());
		
	}
	
	@Test
	public void testExecuteRuleCurrentStatusWARNAndNewLevelisAboveRangeButNotConsecutive() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2100);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		int[] previousReadings= {BELOW_RANGE,ABOVE_RANGE};
		when(ruleContext.getPreviousXreadings()).thenReturn(previousReadings);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.WARN);
		when(ruleContext.getNewCo2level()).thenReturn(ABOVE_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.WARN);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.WARN, ruleContext1.getNewSensor().getStatus());
		
		
	}
	
	@Test
	public void testExecuteRuleCurrentStatusWARNAndNewLevelisAboveRangeAndConsecutive() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2100);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		
		int[] previousReadings= {ABOVE_RANGE,ABOVE_RANGE};
		when(ruleContext.getPreviousXreadings()).thenReturn(previousReadings);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.WARN);
		when(ruleContext.getNewCo2level()).thenReturn(ABOVE_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.ALERT);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.ALERT, ruleContext1.getNewSensor().getStatus());
		
	}
	//  Alert
	@Test
	public void testExecuteRuleCurrentStatusALERTAndNewLevelisAboveRange() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2100);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.ALERT);
		when(ruleContext.getNewCo2level()).thenReturn(ABOVE_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.ALERT);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.ALERT, ruleContext1.getNewSensor().getStatus());
		
	}
	
	@Test
	public void testExecuteRuleCurrentStatusALERTAndNewLevelisEqualToPermittedLevel() {
		
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2100);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.ALERT);
		when(ruleContext.getNewCo2level()).thenReturn(PERMITTED_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.ALERT);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.ALERT, ruleContext1.getNewSensor().getStatus());
		
	}
	
	@Test
	public void testExecuteRuleCurrentStatusALERTAndNewLevelisBelowRangeANDConsecutiveValueAboveRange() {
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2100);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		
		int[] previousReadings= {ABOVE_RANGE,ABOVE_RANGE};
		when(ruleContext.getPreviousXreadings()).thenReturn(previousReadings);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.ALERT);
		when(ruleContext.getNewCo2level()).thenReturn(BELOW_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.ALERT);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.ALERT, ruleContext1.getNewSensor().getStatus());

	}
	
	@Test
	public void testExecuteRuleCurrentStatusALERTAndNewLevelisBelowRangeANDConsecutiveValueBelowRange() {
		
		UUID sensorId=UUID.randomUUID();
		Sensor sensor=new Sensor();
		sensor.setSensorId(sensorId);
		sensor.setLevel(2100);
		
		when(ruleContextFactory.getRuleContext(sensor)).thenReturn(ruleContext);
		
		int[] previousReadings= {BELOW_RANGE,BELOW_RANGE};
		when(ruleContext.getPreviousXreadings()).thenReturn(previousReadings);
		when(ruleContext.getCurrentSensorStatus()).thenReturn(Status.ALERT);
		when(ruleContext.getNewCo2level()).thenReturn(BELOW_RANGE);
		
		Sensor sensorMock=mock(Sensor.class);
		when(ruleContext.getNewSensor()).thenReturn(sensorMock);
		when(sensorMock.getStatus()).thenReturn(Status.OK);
		
		RuleContext ruleContext1=rule.executeRule(sensor);
		assertEquals(Status.OK, ruleContext1.getNewSensor().getStatus());
	}

}
