package eab.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EABServiceDaoTest {

	private EABServiceDao dao = new EABServiceDao();
	
	@Before
	public void setUp() throws Exception {
		dao.saveOrUpdateService(new EABService());
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveOrUpdateService() {
		assertTrue(true);
	}

	@Test
	public void testLoadService() {
		assertTrue(true);
	}

	@Test
	public void testDeleteService() {
		assertTrue(true);
	}

	@Test
	public void testGetServiceCount() {
		assertEquals(9, dao.getServiceCount(null));
		assertEquals(1, dao.getServiceCount("ImportAllowService"));
	}

	@Test
	public void testGetServices() {
		EABService[] services = dao.getServices(2, 5, null);
		assertEquals(4, services.length);
		
		services = dao.getServices(1, 10, "MaterialImportIntegrationAsynCallbackService");
		assertEquals(1, services.length);
	}

}
