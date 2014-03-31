package eab.entity;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WSDLProtocolDaoTest {
	
	private WSDLProtocolDao dao = new WSDLProtocolDao();

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSaveOrUpdateProtocol() {
		dao.saveOrUpdateProtocol(new WSDLProtocol("hello", "hello.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("AsyncHello", "AsyncHello.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileTransferAsyncCallBackService", "FileTransferAsyncCallBackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileTransferService", "FileTransferService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileVerifyAsynCallbackService", "FileVerifyAsynCallbackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileVerifyService", "FileVerifyService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("ImportAllowAsynCallbackService", "ImportAllowAsynCallbackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("ImportAllowService", "ImportAllowService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("MaterialImportIntegrationAsynCallbackService", "MaterialImportIntegrationAsynCallbackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("MaterialImportIntegrationService", "MaterialImportIntegrationService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("MetadataImportService", "MetadataImportService.wsdl"));	
	}

	@Test
	public void testLoadProtocol() {
		assertTrue(true);
	}

	@Test
	public void testDeleteProtocol() {
		assertTrue(true);
	}

	@Test
	public void testListAllProtocols() {		
		WSDLProtocol[] protocols = dao.listAllProtocols();
		assertEquals(11, protocols.length);
	}

}
