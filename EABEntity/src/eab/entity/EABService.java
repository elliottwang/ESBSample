package eab.entity;

import java.io.Serializable;

public class EABService implements Serializable{

	private static final long serialVersionUID = 1L;	
	
	public EABService(){		
	}
	
	public EABService(String type, String name, String uuid, String url, String wrappedServiceURL){
		this.setProtocol(WSDLProtocolDao.dao.loadProtocol(type));
		this.uniqueServiceName = name;
		this.uniqueNamespaceByGuid = uuid;
		this.serviceURL = url;
		this.wrappedServiceURL = wrappedServiceURL;
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
	
	public String getWrappedServiceURL() {
		return wrappedServiceURL;
	}

	public void setWrappedServiceURL(String wrappedServiceURL) {
		this.wrappedServiceURL = wrappedServiceURL;
	}

	public WSDLProtocol getProtocol() {
		return protocol;
	}

	public void setProtocol(WSDLProtocol protocol) {
		this.protocol = protocol;
	}

	private String uniqueServiceName;
	private String uniqueNamespaceByGuid;
	private String serviceURL;
	private String wrappedServiceURL;
	private WSDLProtocol protocol;
}
