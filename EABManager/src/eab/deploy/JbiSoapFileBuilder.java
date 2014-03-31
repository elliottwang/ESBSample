package eab.deploy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import eab.deploy.zip.JbiSaBuilder;
import eab.deploy.zip.JbiSuBuilder;
import eab.parser.WSDLProtocolParser;
import eab.util.EABPath;

public class JbiSoapFileBuilder {
	
	public JbiSoapFileBuilder(String wsdlFile, String serviceName, String namespaceGuid, String url){	
		this.wsdlFilePath = wsdlFile;
		this.uniqueServiceName = serviceName;
		this.uniqueNamespaceByGuid = namespaceGuid;
		this.serviceURL = url;		
	}
	
	public JbiSoapFileBuilder(){		
	}
	
	public void buildJbiPackage(String outFile){
		
		this.parser = new WSDLProtocolParser();
		this.parser.parse(this.wsdlFilePath);
		
		init();
		
		if(this.parser.isSync()){
			buildSyncPackage(outFile);
		}
		else{
			buildAsyncPackage(outFile);
		}
	}
	
	protected void buildSyncPackage(String outFile){
		HashMap<String, byte[]> sus = new HashMap<String, byte[]>();
		
		// build http su
		HashMap<String, String> httpSuContent = new HashMap<String, String>();
		httpSuContent.put("xbean.xml", readAndReplaceTemplate(sync_Http_XBean));
		httpSuContent.put("ServiceWrap.wsdl", readAndReplaceTemplate(sync_ServiceWrap));
		sus.put("servicewrap-" + this.uniqueServiceName + "-http-su.zip", 
				JbiSuBuilder.buildSu(httpSuContent, readAndReplaceTemplate(sync_Su_Jbi)));
		
		// build eip su
		HashMap<String, String> eipSuContent = new HashMap<String, String>();
		eipSuContent.put("xbean.xml", readAndReplaceTemplate(sync_Eip_XBean));
		sus.put("servicewrap-" + this.uniqueServiceName + "-eip-su.zip", 
				JbiSuBuilder.buildSu(eipSuContent, readAndReplaceTemplate(sync_Su_Jbi)));
		
		// build sa
		JbiSaBuilder.buildSa(outFile, sus, readAndReplaceTemplate(sync_Sa_Jbi));
	}
	
	protected void buildAsyncPackage(String outFile){
		HashMap<String, byte[]> sus = new HashMap<String, byte[]>();
		
		// build http su
		HashMap<String, String> httpSuContent = new HashMap<String, String>();
		httpSuContent.put("xbean.xml", readAndReplaceTemplate(async_Http_XBean));
		httpSuContent.put("ServiceWrap.wsdl", readAndReplaceTemplate(async_ServiceWrap));
		sus.put("servicewrap-" + this.uniqueServiceName + "-http-su.zip", 
				JbiSuBuilder.buildSu(httpSuContent, readAndReplaceTemplate(async_Su_Jbi)));
		
		// build eip su
		HashMap<String, String> eipSuContent = new HashMap<String, String>();
		eipSuContent.put("xbean.xml", readAndReplaceTemplate(async_Eip_XBean));
		sus.put("servicewrap-" + this.uniqueServiceName + "-eip-su.zip", 
				JbiSuBuilder.buildSu(eipSuContent, readAndReplaceTemplate(async_Su_Jbi)));
		
		// build bean su
		HashMap<String, String> beanSuContent = new HashMap<String, String>();
		beanSuContent.put("xbean.xml", readAndReplaceTemplate(async_Bean_XBean));
		sus.put("servicewrap-" + this.uniqueServiceName + "-bean-su.zip", 
				JbiSuBuilder.buildSu(beanSuContent, readAndReplaceTemplate(async_Su_Jbi)));
		
		// build sa
		JbiSaBuilder.buildSa(outFile, sus, readAndReplaceTemplate(async_Sa_Jbi));
	}
	
	protected String readAndReplaceTemplate(String filePath) {
		return replaceTemplate(readTemplate(filePath));
	}
	
	protected String readTemplate(String filePath){
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
	
	protected String replaceTemplate(String origin){
		for(String[] pair : replaceMapping){
			origin = origin.replaceAll(pair[0], pair[1]);
		}
		
		return origin;
	}
	
	protected void init(){
		replaceMapping = new ArrayList<String[]>();
		replaceMapping.add(new String[]{"\\$UniqueServiceName\\$", this.uniqueServiceName});
		replaceMapping.add(new String[]{"\\$UniqueNamespaceByGuid\\$", this.uniqueNamespaceByGuid});
		replaceMapping.add(new String[]{"\\$ServiceNamespace\\$", this.parser.getServiceNamespace()});
		replaceMapping.add(new String[]{"\\$ServiceName\\$", this.parser.getServiceName()});
		replaceMapping.add(new String[]{"\\$ServiceEndpoint\\$", this.parser.getServiceEndpoint()});
		replaceMapping.add(new String[]{"\\$ServiceAddress\\$", this.serviceURL});
		replaceMapping.add(new String[]{"\\$AbsoluteWSDLFilePath\\$", this.parser.getAbsoluteWSDLFilePath()});
		replaceMapping.add(new String[]{"\\$ServiceSoapAction\\$", this.parser.getServiceSoapAction()});
		replaceMapping.add(new String[]{"\\$ServiceBinding\\$", this.parser.getServiceBindingName()});

//		$UniqueServiceName$
//		$UniqueNamespaceByGuid$
//		$ServiceNamespace$
//		$ServiceName$
//		$ServiceEndpoint$
//		$ServiceAddress$
//		$AbsoluteWSDLFilePath$
//		$ServiceSoapAction$
//		$ServiceBinding$
	}

	public String getWsdlFilePath() {
		return wsdlFilePath;
	}

	public void setWsdlFilePath(String wsdlFilePath) {
		this.wsdlFilePath = wsdlFilePath;
	}

	public String getUniqueServiceName() {
		return uniqueServiceName;
	}

	public void setUniqueServiceName(String uniqueServiceName) {
		this.uniqueServiceName = uniqueServiceName;
	}

	public String getUniqueNamespaceByGuid() {
		return uniqueNamespaceByGuid;
	}

	public void setUniqueNamespaceByGuid(String uniqueNamespaceByGuid) {
		this.uniqueNamespaceByGuid = uniqueNamespaceByGuid;
	}

	public String getServiceURL() {
		return serviceURL;
	}

	public void setServiceURL(String serviceURL) {
		this.serviceURL = serviceURL;
	}
	
	private WSDLProtocolParser parser;

	private String wsdlFilePath;
	private String uniqueServiceName;
	private String uniqueNamespaceByGuid;
	private String serviceURL;
	
	private ArrayList<String[]> replaceMapping;
	
	private static String sync_Http_XBean = EABPath.getTemplatePath() + "service/sync/http-xbean.xml";
	private static String sync_ServiceWrap = EABPath.getTemplatePath() + "service/sync/ServiceWrap.wsdl";
	private static String sync_Eip_XBean = EABPath.getTemplatePath() + "service/sync/eip-xbean.xml";
	private static String sync_Su_Jbi = EABPath.getTemplatePath() + "service/sync/su-jbi.xml";
	private static String sync_Sa_Jbi = EABPath.getTemplatePath() + "service/sync/sa-jbi.xml";
	
	private static String async_Http_XBean = EABPath.getTemplatePath() + "service/async/http-xbean.xml";
	private static String async_ServiceWrap = EABPath.getTemplatePath() + "service/async/ServiceWrap.wsdl";
	private static String async_Eip_XBean = EABPath.getTemplatePath() + "service/async/eip-xbean.xml";
	private static String async_Bean_XBean = EABPath.getTemplatePath() + "service/async/bean-xbean.xml";
	private static String async_Su_Jbi = EABPath.getTemplatePath() + "service/async/su-jbi.xml";
	private static String async_Sa_Jbi = EABPath.getTemplatePath() + "service/async/sa-jbi.xml";
}
