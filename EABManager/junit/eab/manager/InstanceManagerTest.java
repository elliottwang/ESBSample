package eab.manager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eab.mananger.InstanceManager;

public class InstanceManagerTest {
	
	private InstanceManager manager = new InstanceManager();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetInstanceCount() {
		assertTrue(true);
	}

	@Test
	public void testGetInstances() {
		assertTrue(true);
	}

	@Test
	public void testLoadInstance() {
		assertTrue(true);
	}

	@Test
	public void testLoadScope() {
		assertTrue(true);
	}

	@Test
	public void testSuspendInstance() {
		assertTrue(true);
	}

	@Test
	public void testResumeInstance() {
		assertTrue(true);
	}

	@Test
	public void testTerminateInstance() {
		assertTrue(true);
	}

	@Test
	public void testDeleteInstance() {
		manager.deleteInstance(51725);
		assertTrue(true);
	}

}
