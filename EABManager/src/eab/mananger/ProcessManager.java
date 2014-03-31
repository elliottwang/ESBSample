package eab.mananger;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

import eab.deploy.JbiBpelFileBuilder;
import eab.entity.EABFlow;
import eab.entity.EABFlowDao;
import eab.entity.EABService;
import eab.entity.EABServiceDao;
import eab.entity.ode.ODEProcess;
import eab.entity.ode.ODEProcessDao;
import eab.util.EABPath;

public class ProcessManager {

	// a pending parameter
	private static String deployFolder = EABPath.getDeployPath();
	
	private EABFlowDao eabDao = new EABFlowDao();
	private ODEProcessDao odeDao = new ODEProcessDao();
	
	public ProcessManager(){		
	}
	
	public int getProcessCount(){
		return eabDao.getFlowCount();
	}
	
	public EABFlow getEABFlowFromODEProcess(String odeProcessType){
		return eabDao.loadFlow(extracted(odeProcessType).substring(extracted(odeProcessType).lastIndexOf("}") + 1));
	}
	
	public ODEProcess getODEInstanceFormEABFlow(String eabFlowType){
		EABFlow flow = eabDao.loadFlow(extracted(eabFlowType));
		return odeDao.bindProcess(flow);
	}

	private String extracted(String eabFlowType) {
		return eabFlowType;
	}
	
	public EABFlow[] listFlow(int pages, int maxPerPage){
		return eabDao.getFlows(pages, maxPerPage);
	}
	
	public boolean tinyDeploy(String flowName, String flowView){
		EABFlow flow  = eabDao.loadFlow(extracted(flowName));
		if(flow != null)
			return false;		
		
		flow = new EABFlow();

		flow.setUniqueFlowName(extracted(flowName));
		flow.setFlowViewFile(extracted(flowView));

		eabDao.saveOrUpdateFlow(flow);
		
		return true;
	}
	
	public boolean deployFlow(String flowName, 
			String bpel, 
			String wrap,
			String deploy, 
			String flowView, 
			String[] consumeTypes, 
			String[] provideServices){		
		
		EABFlow flow  = eabDao.loadFlow(extracted(flowName));
		if(flow != null)
			return false;
		
		EABServiceDao serviceDao = new EABServiceDao();
		EABService[] provides = new EABService[provideServices.length];
		for(int i = 0; i < provideServices.length; i++){
			provides[i] = serviceDao.loadService(provideServices[i]);
			if(provides[i] == null)
				return false;
		}
		
		EABService[] consumes = new EABService[consumeTypes.length];
		for(int i = 0; i < consumeTypes.length; i++){
			consumes[i] = buildConsumeService(extracted(flowName), consumeTypes[i], i);
			if(consumes[i].getProtocol() == null)
				return false;
			
			serviceDao.saveOrUpdateService(consumes[i]);
		}
		
		flow = new EABFlow();

		flow.setUniqueFlowName(extracted(flowName));
		flow.setBpelFile(extracted(bpel));
		flow.setDeployFile(extracted(deploy));
		flow.setFlowViewFile(extracted(flowView));
		flow.setInvokeService(consumes[0]);
		flow.setCallbackService(provides[0]);
		
		for (EABService service : consumes) {
			flow.addConsumeServices(service);
		}
		
		for (EABService service : provides) {
			flow.addProvideService(service);
		}
		
		JbiBpelFileBuilder builder = new JbiBpelFileBuilder(extracted(flowName), 
				extracted(deploy), 
				extracted(bpel), 
				extracted(wrap), 
				consumes, 
				provides);
		
		builder.buildJbiPackage(getFlowAssemblyFile(extracted(flowName)));
		
		eabDao.saveOrUpdateFlow(flow);
		
		return true;
	}
	
	public boolean deleteFlow(String flowName){
		EABFlow flow = eabDao.loadFlow(extracted(flowName));
		if(flow == null)
			return false;
		
		File assembly = new File(getFlowAssemblyFile(extracted(flowName)));
		if(assembly.exists())
			return assembly.delete();
		
		eabDao.deleteFlow(flow);
		
		return true;
	}
	
	public boolean updateFlow(String flowName, String flowView){
		EABFlow flow  = eabDao.loadFlow(extracted(flowName));
		if(flow == null)
			return false;
		
		if(extracted(flowView).equals(flow.getFlowViewFile()))
			return true;
		
		flow.setFlowViewFile(extracted(flowView));
		eabDao.saveOrUpdateFlow(flow);
		return true;
	}
	
	public boolean updateFlow(String flowName, 
			String bpel, 
			String wrap,
			String deploy, 
			String flowView, 
			String[] consumeTypes, 
			String[] provideServices){
		
		EABFlow flow  = eabDao.loadFlow(extracted(flowName));
		if(flow == null)
			return false;
		
		eabDao.deleteFlow(flow);
		
		return deployFlow(extracted(flowName), 
				extracted(bpel), 
				extracted(wrap), 
				extracted(deploy), 
				extracted(flowView), 
				consumeTypes, 
				provideServices);
	}
	
	protected EABService buildConsumeService(String flowName, String consumeType, int nIndex) {
		String serviceName = extracted(flowName) + "_" + extracted(consumeType) + "_" + String.valueOf(nIndex + 1);
		
		InetAddress inet = null;
		try {
			inet = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		String wrappedURL =  "http://" + inet.getHostAddress() + ":807/flows/" + 
			extracted(flowName) + "/" + extracted(consumeType) + "_" + String.valueOf(nIndex + 1) + "/";
		
		return new EABService(extracted(consumeType), extracted(serviceName), "", "", extracted(wrappedURL));
	}
	
	protected String getFlowAssemblyFile(String flowName) {
		return deployFolder + "serviceflow-" + extracted(flowName) + "-sa.zip";
	}
}
