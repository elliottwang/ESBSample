package eab.logger.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

public class SoapMessageLog implements Serializable {	

	private static final long serialVersionUID = 1L;
	
	public SoapMessageLog(){		
	}
	
	public SoapMessageLog(String inMsg, String outMsg, String type, int resultCode){
		this.id = UUID.randomUUID().toString();
		this.invokeTime = new Date();
		this.type = type;
		this.inMessage = inMsg;
		this.outMessage = outMsg;
		this.result = resultCode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getInvokeTime() {
		return invokeTime;
	}

	public void setInvokeTime(Date invokeTime) {
		this.invokeTime = invokeTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getInMessage() {
		return inMessage;
	}

	public void setInMessage(String inMessage) {
		this.inMessage = inMessage;
	}

	public String getOutMessage() {
		return outMessage;
	}

	public void setOutMessage(String outMessage) {
		this.outMessage = outMessage;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	private String id;
	private Date invokeTime;
	private String type;
	private String inMessage;
	private String outMessage;
	private int result;

}
