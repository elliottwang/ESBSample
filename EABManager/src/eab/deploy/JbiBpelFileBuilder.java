package eab.deploy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import eab.deploy.zip.JbiSaBuilder;
import eab.deploy.zip.JbiSuBuilder;
import eab.entity.EABService;
import eab.parser.WSDLProtocolParser;
import eab.util.EABPath;

public class JbiBpelFileBuilder {
	
	public JbiBpelFileBuilder(String uniqueFlowName, String deployFile, String bpelFile, String wrapFile, EABService[] consumeServices, EABService[] provideServices){
		this.uniqueFlowName = uniqueFlowName;
		this.deployFile = deployFile;
		this.bpelFile = bpelFile;
		this.wrapFile = wrapFile;
		this.consumeServices = consumeServices;
		this.provideServices = provideServices;
	}
	
	public JbiBpelFileBuilder(){				
	}
	
	public void buildJbiPackage(String outFile){
		
		parseWsdls();
		
		init();
		
		HashMap<String, byte[]> sus = new HashMap<String, byte[]>();
		
		// build http su
		HashMap<String, String> httpSuContent = new HashMap<String, String>();
		httpSuContent.put("xbean.xml", readAndReplaceTemplate(flow_Http_XBean));
		for(int i = 0; i < this.consumeServices.length; i++){
			String content = readTemplate(flow_Wrapper);
			content = content.replaceAll("\\$ConsumerIndex\\$", String.valueOf(i + 1));
			httpSuContent.put("ServiceWrap_ConsumerService_" + String.valueOf(i + 1) + ".wsdl", replaceTemplate(content));
		}
		sus.put("serviceflow-" + this.uniqueFlowName + "-http-su.zip", 
				JbiSuBuilder.buildSu(httpSuContent, readTemplate(flow_Su_Jbi)));
		
		// build ode su
		HashMap<String, String> odeSuContent = new HashMap<String, String>();
		odeSuContent.put("deploy.xml", replaceTemplate(this.deployFile));
		odeSuContent.put(this.uniqueFlowName + ".bpel", replaceTemplate(this.bpelFile));
		odeSuContent.put(this.uniqueFlowName + "_ServiceWrap.wsdl", replaceTemplate(this.wrapFile));		
		sus.put("serviceflow-" + this.uniqueFlowName + "-ode-su.zip", 
				JbiSuBuilder.buildSu(odeSuContent, readAndReplaceTemplate(flow_Su_Jbi)));
		
		// build sa
		JbiSaBuilder.buildSa(outFile, sus, readAndReplaceTemplate(flow_Sa_Jbi));
	}
	
	protected String readAndReplaceTemplate(String filePath) {
		String contentString = readTemplate(filePath);
		
		contentString = repeatTemplete(contentString, "ConsumerIndex", this.consumeServices.length);
		contentString = repeatTemplete(contentString, "ProviderIndex", this.provideServices.length);
		
		return replaceTemplate(contentString);
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
	
	protected String repeatTemplete(String origin, String repeaterString, int count){
		StringBuilder sb = new StringBuilder(origin);		
		String startString = "$Repeat Start by " + repeaterString + "$";
		String endString = "$Repeat Stop$";
		int startIndex = 0, endIndex = 0;
		
		for(; ;){			
			startIndex = sb.indexOf(startString, endIndex);
			endIndex = sb.indexOf(endString, startIndex);
			
			if((startIndex < 0) || (endIndex < 0))
				break;
			
			String subRepeat = sb.substring(startIndex + startString.length(), endIndex);
			sb.delete(startIndex, endIndex + endString.length());
			for(int i = count; i > 0; i--){
				sb.insert(startIndex, subRepeat.replaceAll("\\$" + repeaterString + "\\$", String.valueOf(i)));
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
	
	protected void parseWsdls(){
		consumerService_ServiceNamespace = new String[this.consumeServices.length];
		consumerService_ServiceName = new String[this.consumeServices.length];
		consumerService_ServiceBinding = new String[this.consumeServices.length];
		consumerService_ServiceEndpoint = new String[this.consumeServices.length];
		consumerService_ServiceAction = new String[this.consumeServices.length];
		consumerService_Mep = new String[this.consumeServices.length];
		consumerService_AbsoluteWSDLFilePath = new String[this.consumeServices.length];
		
		providerService_UniqueNamespaceByGuid = new String[this.provideServices.length];
		providerService_AbsoluteWSDLFilePath = new String[this.provideServices.length];
		
		for(int i = 0; i < consumeServices.length; i++){
			WSDLProtocolParser parser = new WSDLProtocolParser();
			if(!parser.parse(this.consumeServices[i].getProtocol().getWsdlFilePath()))
				continue;
			
			consumerService_ServiceNamespace[i] = parser.getServiceNamespace(); 
			consumerService_ServiceName[i] = parser.getServiceName();
			consumerService_ServiceBinding[i] = parser.getServiceBindingName();
			consumerService_ServiceEndpoint[i] = parser.getServiceEndpoint();
			consumerService_ServiceAction[i] = parser.getServiceSoapAction();
			consumerService_Mep[i] = parser.isSync() ? "in-only" : "in-out";
			consumerService_AbsoluteWSDLFilePath[i] = parser.getAbsoluteWSDLFilePath();
		}
		
		for(int i = 0; i < provideServices.length; i++){
			WSDLProtocolParser parser = new WSDLProtocolParser();
			if(!parser.parse(this.provideServices[i].getProtocol().getWsdlFilePath()))
				continue;
			
			providerService_UniqueNamespaceByGuid[i] = this.provideServices[i].getUniqueNamespaceByGuid();
			providerService_AbsoluteWSDLFilePath[i] = parser.getAbsoluteWSDLFilePath();
		}
	}
		
	protected void init(){
		replaceMapping = new ArrayList<String[]>();
		replaceMapping.add(new String[]{"\\$UniqueFlowName\\$", this.uniqueFlowName});
		
		for(int i = 0; i < this.consumeServices.length; i++){
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_ServiceNamespace\\$", this.consumerService_ServiceNamespace[i]});
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_ServiceName\\$", this.consumerService_ServiceName[i]});
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_ServiceEndpoint\\$", this.consumerService_ServiceEndpoint[i]});
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_Mep\\$", this.consumerService_Mep[i]});
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_ServiceAction\\$", this.consumerService_ServiceAction[i]});
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_AbsoluteWSDLFilePath\\$", this.consumerService_AbsoluteWSDLFilePath[i]});
			replaceMapping.add(new String[]{"\\$ConsumerService_" + String.valueOf(i + 1) + "_ServiceBinding\\$", this.consumerService_ServiceBinding[i]});
		}
		
		for(int i = 0; i < this.provideServices.length; i++){
			replaceMapping.add(new String[]{"\\$ProviderService_" + String.valueOf(i + 1) + "_UniqueNamespaceByGuid\\$", this.providerService_UniqueNamespaceByGuid[i]});
			replaceMapping.add(new String[]{"\\$ProviderService_" + String.valueOf(i + 1) + "_AbsoluteWSDLFilePath\\$", this.providerService_AbsoluteWSDLFilePath[i]});
		}
		
//		$ConsumerIndex$
//		$ConsumerService_$ConsumerIndex$_ServiceNamespace$
//		$ConsumerService_$ConsumerIndex$_ServiceName$
//		$ConsumerService_$ConsumerIndex$_ServiceEndpoint$
//		$ConsumerService_$ConsumerIndex$_Mep$
//		$ConsumerService_$ConsumerIndex$_ServiceAction$
//		$ConsumerService_$ConsumerIndex$_AbsoluteWSDLFilePath$
//		$ConsumerService_$ConsumerIndex$_ServiceBinding$
//		$ProviderIndex$
//		$ProviderService_$ProviderIndex$_UniqueNamespaceByGuid$
//		$ProviderService_$ProviderIndex$_AbsoluteWSDLFilePath$
	}	
	
	public String getUniqueFlowName() {
		return uniqueFlowName;
	}

	public void setUniqueFlowName(String uniqueFlowName) {
		this.uniqueFlowName = uniqueFlowName;
	}

	public String getDeployFile() {
		return deployFile;
	}

	public void setDeployFile(String deployFile) {
		this.deployFile = deployFile;
	}

	public String getBpelFile() {
		return bpelFile;
	}

	public void setBpelFile(String bpelFile) {
		this.bpelFile = bpelFile;
	}

	public String getWrapFile() {
		return wrapFile;
	}

	public void setWrapFile(String wrapFile) {
		this.wrapFile = wrapFile;
	}

	public EABService[] getConsumeServices() {
		return consumeServices;
	}

	public void setConsumeServices(EABService[] consumeServices) {
		this.consumeServices = consumeServices;
	}

	public EABService[] getProvideServices() {
		return provideServices;
	}

	public void setProvideServices(EABService[] provideServices) {
		this.provideServices = provideServices;
	}

	private String uniqueFlowName;
	private String deployFile;
	private String bpelFile;
	private String wrapFile;
	private EABService[] consumeServices;
	private EABService[] provideServices;
	
	private String[] consumerService_ServiceNamespace;
	private String[] consumerService_ServiceName;
	private String[] consumerService_ServiceBinding;
	private String[] consumerService_ServiceEndpoint;
	private String[] consumerService_ServiceAction;
	private String[] consumerService_Mep;	
	private String[] consumerService_AbsoluteWSDLFilePath;
	private String[] providerService_UniqueNamespaceByGuid;	
	private String[] providerService_AbsoluteWSDLFilePath;	
	
	private ArrayList<String[]> replaceMapping;
	
	private static String flow_Http_XBean = EABPath.getTemplatePath() + "bpel/http-xbean.xml";
	private static String flow_Wrapper = EABPath.getTemplatePath() + "bpel/ServiceWrap_ConsumerService_$ConsumerIndex$.wsdl";
	private static String flow_Su_Jbi = EABPath.getTemplatePath() + "bpel/su-jbi.xml";
	private static String flow_Sa_Jbi = EABPath.getTemplatePath() + "bpel/sa-jbi.xml";
}
