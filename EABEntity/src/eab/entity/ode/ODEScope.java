package eab.entity.ode;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unchecked")
public class ODEScope implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getState() {
		return state;
	}
	
	public void setState(String state) {
		this.state = state;
	}	

	public ODEScope getParent() {
		return parent;
	}

	public void setParent(ODEScope parent) {
		this.parent = parent;
	}

	public Set getSubScopes() {
		return subScopes;
	}

	public void setSubScopes(Set subScopes) {
		this.subScopes = subScopes;
	}

	public Set getDatas() {
		return datas;
	}

	public void setDatas(Set datas) {
		this.datas = datas;
	}
	
	private long id;
	private String name;
	private String state;
	private ODEScope parent;
	private Set subScopes = new HashSet();
	private Set datas = new HashSet();
}
