package eab.entity;

import java.io.Serializable;

public class WSDLProtocol implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public WSDLProtocol() {		
	}
	
	public WSDLProtocol(String type, String wsdlFilePath) {	
		this.type = type;
		this.wsdlFilePath = wsdlFilePath;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}
	
	public String getWsdlFilePath() {
		return wsdlFilePath;
	}
	
	public void setWsdlFilePath(String wsdlFilePath) {
		this.wsdlFilePath = wsdlFilePath;
	}
	
	private String type;
	private String wsdlFilePath;	
}
