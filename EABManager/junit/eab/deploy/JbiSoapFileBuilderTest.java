package eab.deploy;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class JbiSoapFileBuilderTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildJbiPackage() {
		
		ArrayList<String[]> arrayList = new ArrayList<String[]>();
		
		arrayList.add(new String[]{"C:/eab/protocol/MaterialImportIntegrationAsynCallbackService.wsdl",
				"MAM-MaterialImportIntegrationAsynCallbackService",
				"http://172.19.52.254/TempInterface/MaterialImportIntegrationAsynCallbackService.asmx",
				"5237f1b7-f686-49b6-b180-1a269be3fcf7"});
		
		arrayList.add(new String[]{"C:/eab/protocol/ImportAllowService.wsdl",
				"BC-ImportAllowService",
				"http://172.19.52.254/TempInterface/ImportAllowService.asmx",
				"669afaad-d54b-4950-aecf-23ce17cd9a0c"});
		
		arrayList.add(new String[]{"C:/eab/protocol/FileTransferService.wsdl",
				"TP-FileTransferService",
				"http://172.19.52.254/TempInterface/FileTransferService.asmx",
				"ec024690-16f5-482e-9fc1-48e7ca0f22ac"});
		
		arrayList.add(new String[]{"C:/eab/protocol/FileVerifyService.wsdl",
				"BC-FileVerifyService",
				"http://172.19.52.254/TempInterface/FileVerifyService.asmx",
				"b86ad38f-b888-45a0-b8c9-dd79ba5cdf05"});
		
		arrayList.add(new String[]{"C:/eab/protocol/MetadataImportService.wsdl",
				"BC-MetadataImportService",
				"http://172.19.52.254/TempInterface/MetadataImportService.asmx",
				"36897309-fc7a-453b-8ea3-fb08c249a4e7"});
		
		for(String[] objStrings : arrayList){
			JbiSoapFileBuilder builder = new JbiSoapFileBuilder(objStrings[0], 
					objStrings[1], 
					objStrings[3], 
					objStrings[2]);
			
			String packPathString = "C:/eab/deploy/servicewrap-" + builder.getUniqueServiceName() + "-sa.zip";
			builder.buildJbiPackage(packPathString);
		}
	
		assertTrue(true);
	}

}
