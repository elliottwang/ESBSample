package eab.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class EABFlow implements Serializable {

	private static final long serialVersionUID = 1L;

	public EABFlow() {		
	}
	
	public String getUniqueFlowName() {
		return uniqueFlowName;
	}
	
	public void setUniqueFlowName(String uniqueFlowName) {
		this.uniqueFlowName = uniqueFlowName;
	}
	
	public String getBpelFile() {
		return bpelFile;
	}
	
	public void setBpelFile(String bpelFile) {
		this.bpelFile = bpelFile;
	}
	
	public String getDeployFile() {
		return deployFile;
	}
	
	public void setDeployFile(String deployFile) {
		this.deployFile = deployFile;
	}
	
	public String getFlowViewFile() {
		return flowViewFile;
	}
	
	public void setFlowViewFile(String flowViewFile) {
		this.flowViewFile = flowViewFile;
	}
	
	public EABService getInvokeService() {
		return invokeService;
	}

	public void setInvokeService(EABService invokeService) {
		this.invokeService = invokeService;
	}

	public EABService getCallbackService() {
		return callbackService;
	}

	public void setCallbackService(EABService callbackService) {
		this.callbackService = callbackService;
	}

	public Set getConsumeServices() {
		return consumeServices;
	}

	public void setConsumeServices(Set consumeServices) {
		this.consumeServices = consumeServices;
	}

	public Set getProvideServices() {
		return provideServices;
	}

	public void setProvideServices(Set provideServices) {
		this.provideServices = provideServices;
	}
	
	public void addConsumeServices(EABService service){
		consumeServices.add(service);
	}
	
	public void addProvideService(EABService service){
		provideServices.add(service);
	}

	private String uniqueFlowName;
	private String bpelFile;
	private String deployFile;
	private String flowViewFile;
	private EABService invokeService;
	private EABService callbackService;
	private Set consumeServices = new HashSet();
	private Set provideServices = new HashSet();
}
