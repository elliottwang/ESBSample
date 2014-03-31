package eab.entity.ode;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ODEInstanceDaoTest {

	private ODEInstanceDao dao = new ODEInstanceDao();
	
	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadInstance() {
		assertTrue(true);
	}

	@Test
	public void testGetInstanceCount() {		
		assertEquals(dao.getInstanceCount(null, (short) -1, null, null, null, null), 201);
	}

	@Test
	public void testGetInstances() {
		ODEInstance[] instances = dao.getInstances(2, 100, null, (short) -1, null, null, null, null);
		assertEquals(instances.length, 100);
	}

	@Test
	public void testGetQueryString() {
		assertTrue(true);
	}

}
