package eab.entity.ode;

import java.io.Serializable;
import java.util.Date;

public class ODEInstance implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public ODEInstance(){		
	}
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public Date getCreatTime() {
		return creatTime;
	}
	
	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}
	
	public Date getLastActiveTime() {
		return lastActiveTime;
	}
	
	public void setLastActiveTime(Date lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}
	
	public short getPreviousState() {
		return previousState;
	}
	
	public void setPreviousState(short previousState) {
		this.previousState = previousState;
	}
	
	public short getState() {
		return state;
	}
	
	public void setState(short state) {
		this.state = state;
	}
	public ODEProcess getProcess() {
		return process;
	}
	
	public void setProcess(ODEProcess process) {
		this.process = process;
	}	

	public ODEScope getRootScope() {
		return rootScope;
	}

	public void setRootScope(ODEScope rootScope) {
		this.rootScope = rootScope;
	}

	private long id;
	private Date creatTime;
	private Date lastActiveTime;
	private short previousState;
	private short state;
	private ODEProcess process;	
	private ODEScope rootScope;
}
