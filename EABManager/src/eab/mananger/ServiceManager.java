package eab.mananger;

import java.io.File;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

import eab.deploy.JbiSoapFileBuilder;
import eab.entity.EABService;
import eab.entity.EABServiceDao;
import eab.entity.WSDLProtocol;
import eab.entity.WSDLProtocolDao;
import eab.logger.dao.SoapMessageDao;
import eab.logger.dao.SoapMessageLog;
import eab.util.EABPath;

public class ServiceManager {	
	
	// a pending parameter
	private static String deployFolder = EABPath.getDeployPath();
	
	private EABServiceDao dao = new EABServiceDao();
	
	public ServiceManager(){		
	}
	
	public int getServiceCount(){
		return dao.getServiceCount(null);
	}
	
	public EABService[] listService(int pages, int maxPerPage){
		return dao.getServices(pages, maxPerPage, null);
	}
	
	public int getLogCount(String serviceType, Date startTime, Date endTime){
		return SoapMessageDao.dao.getLogCount(serviceType, startTime, endTime);
	}
	
	public SoapMessageLog[] listLogs(int pages, int maxPerPage, String serviceType, Date startTime, Date endTime){
		return SoapMessageDao.dao.getLogs(pages, maxPerPage, serviceType, startTime, endTime);
	}
	
	public boolean deployService(String serviceName, String serviceURL){
		
		// automatic recognize the service type by serviceURL + "?WSDL"
		
		return false;
	}
	
	public boolean deployService(String serviceName, String serviceURL, String type){
		EABService service = dao.loadService(serviceName);
		if(service != null)
			return false;
		
		WSDLProtocolDao wsdlDao = new WSDLProtocolDao();
		WSDLProtocol protocol = wsdlDao.loadProtocol(type);
		if(protocol == null)
			return false;
		
		String uuid = UUID.randomUUID().toString();
		service = new EABService(type, serviceName, uuid, serviceURL, getServiceWrappedURL(serviceName));
		
		JbiSoapFileBuilder builder = new JbiSoapFileBuilder(EABPath.getProtocolPath() + service.getProtocol().getWsdlFilePath(), serviceName, uuid, serviceURL);
		builder.buildJbiPackage(getServiceAssemblyFile(serviceName));
		
		dao.saveOrUpdateService(service);
		
		return true;
	}
	
	public boolean deleteService(String serviceName){
		EABService service = dao.loadService(serviceName);
		if(service == null)
			return false;
		
		File assembly = new File(getServiceAssemblyFile(serviceName));
		if(assembly.exists())
			assembly.delete();
		
		dao.deleteService(service);
		
		return true;
	}
	
	public boolean updateService(String serviceName, String serviceURL, String type){
		EABService service = dao.loadService(serviceName);
		if(service == null)
			return false;
		
		// no need for update
		if(serviceURL.equals(service.getServiceURL()) && type.equals(service.getProtocol().getType()))
			return true;
		
		WSDLProtocolDao wsdlDao = new WSDLProtocolDao();
		WSDLProtocol protocol = wsdlDao.loadProtocol(type);
		if(protocol == null)
			return false;
		
		service.setServiceURL(serviceURL);
		service.setProtocol(protocol);
		
		JbiSoapFileBuilder builder = new JbiSoapFileBuilder(protocol.getWsdlFilePath(), serviceName, service.getUniqueNamespaceByGuid(), serviceURL);
		builder.buildJbiPackage(getServiceAssemblyFile(serviceName));
		
		dao.saveOrUpdateService(service);
		
		return true;
	}
	
	protected String getServiceAssemblyFile(String serviceName) {
		return deployFolder + "servicewrap-" + serviceName + "-sa.zip";
	}
	
	protected String getServiceWrappedURL(String serviceName) {
		String hostAddress = "127.0.0.1";
		
		Enumeration<NetworkInterface> netInterfaces = null;
		Boolean bFind = false;
		
		try {
			netInterfaces = NetworkInterface.getNetworkInterfaces();
			while (netInterfaces.hasMoreElements()) {
				NetworkInterface ni = netInterfaces.nextElement();
				Enumeration<InetAddress> ips = ni.getInetAddresses();
				while (ips.hasMoreElements()) {
					InetAddress ia = ips.nextElement();
					if(ia.isSiteLocalAddress() && ia instanceof Inet4Address) {
						hostAddress = ia.getHostAddress();
						bFind = true;
						break;
					}
				}
				if(bFind)
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "http://" + hostAddress + ":807/services/" + serviceName + "/";
	}
}
