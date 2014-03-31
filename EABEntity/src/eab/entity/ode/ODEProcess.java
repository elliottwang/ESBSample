package eab.entity.ode;

import java.io.Serializable;

public class ODEProcess implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public ODEProcess(){		
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getProcessType() {
		return processType;
	}
	
	public void setProcessType(String processType) {
		this.processType = processType;
	}
	
	public long getVersion() {
		return version;
	}
	
	public void setVersion(long version) {
		this.version = version;
	}
	
	private long id;
	private String processType;
	private long version;
}
