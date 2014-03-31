package eab.entity.ode;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eab.entity.EABFlow;
import eab.entity.EABFlowDao;

public class ODEProcessDaoTest {
	
	private ODEProcessDao dao = new ODEProcessDao();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testLoadProcess() {
		assertTrue(true);
	}

	@Test
	public void testGetProcessCount() {
		assertEquals(1, dao.getProcessCount());
	}

	@Test
	public void testGetProcesses() {
		ODEProcess[] processes = dao.getProcesses(1, 10);
		assertEquals(1, processes.length);
	}
	
	@Test
	public void testBindProcess() {
		EABFlowDao flowDao = new EABFlowDao();
		EABFlow[] flows = flowDao.getFlows(1, 10);
		ODEProcess[] processes = new ODEProcess[flows.length];
		for(int i = 0; i < flows.length; i++){
			processes[i] = dao.bindProcess(flows[i]);
			assertEquals(flows[i].getUniqueFlowName(), processes[i].getProcessType().substring(processes[i].getProcessType().lastIndexOf("}") + 1));
		}
	}
}
