package eab.deploy;


import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eab.entity.EABFlow;
import eab.entity.EABFlowDao;
import eab.entity.EABService;
import eab.entity.EABServiceDao;
import eab.entity.WSDLProtocol;
import eab.entity.WSDLProtocolDao;

public class JbiBpelFileBuilderTest {
	
	private ArrayList<String[]> arrayList = new ArrayList<String[]>();
	
	protected String readFile(String filePath){
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(filePath));
			String s;
			while((s = in.readLine()) != null){
				sb.append(s);
				sb.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return sb.toString();
	}	

	@Before
	public void setUp() throws Exception {
		WSDLProtocolDao dao = new WSDLProtocolDao();
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileTransferAsyncCallBackService", "D:/ESB/protocol/FileTransferAsyncCallBackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileTransferService", "D:/ESB/protocol/FileTransferService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileVerifyAsynCallbackService", "D:/ESB/protocol/FileVerifyAsynCallbackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("FileVerifyService", "D:/ESB/protocol/FileVerifyService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("ImportAllowAsynCallbackService", "D:/ESB/protocol/ImportAllowAsynCallbackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("ImportAllowService", "D:/ESB/protocol/ImportAllowService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("MaterialImportIntegrationAsynCallbackService", "D:/ESB/protocol/MaterialImportIntegrationAsynCallbackService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("MaterialImportIntegrationService", "D:/ESB/protocol/MaterialImportIntegrationService.wsdl"));
		dao.saveOrUpdateProtocol(new WSDLProtocol("MetadataImportService", "D:/ESB/protocol/MetadataImportService.wsdl"));
		
		arrayList.add(new String[]{"D:/ESB/protocol/MaterialImportIntegrationAsynCallbackService.wsdl",
				"MAM-MaterialImportIntegrationAsynCallbackService",
				"http://172.19.52.254/TempInterface/MaterialImportIntegrationAsynCallbackService.asmx",
				"5237f1b7-f686-49b6-b180-1a269be3fcf7"});
		
		arrayList.add(new String[]{"D:/ESB/protocol/ImportAllowService.wsdl",
				"BC-ImportAllowService",
				"http://172.19.52.254/TempInterface/ImportAllowService.asmx",
				"669afaad-d54b-4950-aecf-23ce17cd9a0c"});
		
		arrayList.add(new String[]{"D:/ESB/protocol/FileTransferService.wsdl",
				"TP-FileTransferService",
				"http://172.19.52.254/TempInterface/FileTransferService.asmx",
				"ec024690-16f5-482e-9fc1-48e7ca0f22ac"});
		
		arrayList.add(new String[]{"D:/ESB/protocol/FileVerifyService.wsdl",
				"BC-FileVerifyService",
				"http://172.19.52.254/TempInterface/FileVerifyService.asmx",
				"b86ad38f-b888-45a0-b8c9-dd79ba5cdf05"});
		
		arrayList.add(new String[]{"D:/ESB/protocol/MetadataImportService.wsdl",
				"BC-MetadataImportService",
				"http://172.19.52.254/TempInterface/MetadataImportService.asmx",
				"36897309-fc7a-453b-8ea3-fb08c249a4e7"});
		
		EABServiceDao srvDao = new EABServiceDao();
		for(String[] objStrings : arrayList){
			EABService srv = new EABService(objStrings[1].split("-")[1], objStrings[1], objStrings[3], objStrings[2], 
					"http://wangfan:807/services/" + objStrings[1] + "/");
			srvDao.saveOrUpdateService(srv);
			
			JbiSoapFileBuilder builder = new JbiSoapFileBuilder(objStrings[0], 
					objStrings[1], 
					objStrings[3], 
					objStrings[2]);
			
			String packPathString = "C:/eab/deploy/servicewrap-" + builder.getUniqueServiceName() + "-sa.zip";
			builder.buildJbiPackage(packPathString);
		}
	}

	@After
	public void tearDown() throws Exception {
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testMain() {
		JbiBpelFileBuilder builder = new JbiBpelFileBuilder();
		builder.setUniqueFlowName("ImportIntegrationService");
		builder.setDeployFile(readFile("template/bpel/deploy.xml"));
		builder.setBpelFile(readFile("template/bpel/$UniqueFlowName$.bpel"));
		builder.setWrapFile(readFile("template/bpel/$UniqueFlowName$_ServiceWrap.wsdl"));
		
		EABServiceDao dao = new EABServiceDao();
		EABService[] consumerServices = new EABService[4];
		consumerServices[0] = new EABService("MaterialImportIntegrationService", 
				builder.getUniqueFlowName()+ "_" +"MaterialImportIntegrationService_1", 
				null, null,
				"http://wangfan:807/flows/" + builder.getUniqueFlowName() + "/MaterialImportIntegrationService_1/");
		consumerServices[1] = new EABService("ImportAllowAsynCallbackService", 
				builder.getUniqueFlowName()+ "_" +"ImportAllowAsynCallbackService_2", 
				null, null,
				"http://wangfan:807/flows/" + builder.getUniqueFlowName() + "/ImportAllowAsynCallbackService_2/");
		consumerServices[2] = new EABService("FileTransferAsyncCallBackService", 
				builder.getUniqueFlowName()+ "_" +"FileTransferAsyncCallBackService_3", 
				null, null,
				"http://wangfan:807/flows/" + builder.getUniqueFlowName() + "/FileTransferAsyncCallBackService_3/");
		consumerServices[3] = new EABService("FileVerifyAsynCallbackService", 
				builder.getUniqueFlowName()+ "_" +"FileVerifyAsynCallbackService_4", 
				null, null,
				"http://wangfan:807/flows/" + builder.getUniqueFlowName() + "/FileVerifyAsynCallbackService_4/");
		for(EABService service : consumerServices){
			dao.saveOrUpdateService(service);
		}
		builder.setConsumeServices(consumerServices);

		EABService[] providerServices = new EABService[5];
		providerServices[0] = dao.loadService("MAM-MaterialImportIntegrationAsynCallbackService");
		providerServices[1] = dao.loadService("BC-ImportAllowService");
		providerServices[2] = dao.loadService("TP-FileTransferService");
		providerServices[3] = dao.loadService("BC-FileVerifyService");
		providerServices[4] = dao.loadService("BC-MetadataImportService");
		builder.setProvideServices(providerServices);
		
		EABFlow flow = new EABFlow();
		flow.setUniqueFlowName(builder.getUniqueFlowName());
		
		flow.setBpelFile(builder.getBpelFile());
		flow.setDeployFile(builder.getDeployFile());
		flow.setFlowViewFile("");
		flow.setInvokeService(dao.loadService(builder.getUniqueFlowName()+ "_" + "MaterialImportIntegrationService_1"));
		flow.setCallbackService(dao.loadService("MAM-MaterialImportIntegrationAsynCallbackService"));
		HashSet consumerSet = new HashSet();
		for (EABService srv : consumerServices) {
			consumerSet.add(srv);
		}
		HashSet providerSet = new HashSet();
		for (EABService srv : providerServices) {
			providerSet.add(srv);
		}
		flow.setConsumeServices(consumerSet);
		flow.setProvideServices(providerSet);
		EABFlowDao flowDao = new EABFlowDao();
		flowDao.saveOrUpdateFlow(flow);
		
		String packPathString = "D:/ESB/deploy/serviceflow-" + builder.getUniqueFlowName() + "-sa.zip";
		builder.buildJbiPackage(packPathString);
		
		assertTrue(true);
	}
}
