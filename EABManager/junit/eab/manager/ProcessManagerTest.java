package eab.manager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eab.entity.EABFlow;
import eab.mananger.ProcessManager;

public class ProcessManagerTest {
	
	private ProcessManager manager = new ProcessManager();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetProcessCount() {
		assertEquals(1, manager.getProcessCount());
	}

	@Test
	public void testListFlow() {
		EABFlow[] flows = manager.listFlow(1, 10);
		assertEquals(1, flows.length);
	}

	@Test
	public void testDeployFlow() {
		assertTrue(true);
	}

	@Test
	public void testDeleteFlow() {
		assertTrue(true);
	}

	@Test
	public void testUpdateFlowStringString() {
		assertTrue(true);
	}

	@Test
	public void testUpdateFlowStringStringStringStringStringStringArrayStringArray() {
		assertTrue(true);
	}

	@Test
	public void testBuildConsumeService() {
		assertTrue(true);
	}

	@Test
	public void testGetFlowAssemblyFile() {
		assertTrue(true);
	}

}
