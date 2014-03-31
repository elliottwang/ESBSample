package eab.entity.ode;

import java.io.Serializable;

public class ODEXmlData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public long getScope() {
		return scope;
	}

	public void setScope(long scope) {
		this.scope = scope;
	}

	private long id;
	private String value;
	private String key;
	private long scope; 
}
