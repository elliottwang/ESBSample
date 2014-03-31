package eab.manager;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eab.mananger.ServiceManager;

public class ServiceManagerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetServiceCount() {
		assertTrue(true);
	}

	@Test
	public void testListService() {
		assertTrue(true);
	}

	@Test
	public void testDeployService() {
		assertTrue(true);
	}

	@Test
	public void testDeleteService() {
		assertTrue(true);
	}

	@Test
	public void testUpdateService() {
		ServiceManager manager = new ServiceManager();
		manager.updateService("BC-MetadataImportService", 
				"http://172.19.52.254/TempInterface/MetadataImportService.asmx", 
				"MetadataImportService");
		assertTrue(true);
	}

	@Test
	public void testGetServiceAssemblyFile() {
		assertTrue(true);
	}

	@Test
	public void testGetServiceWrappedURL() {
		assertTrue(true);
	}

}
