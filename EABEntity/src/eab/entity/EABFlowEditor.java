package eab.entity;

import java.io.Serializable;
import java.util.Date;

public class EABFlowEditor implements Serializable {

	private static final long serialVersionUID = 1L;

	public EABFlowEditor() {		
	}
	
	public String getUniqueFlowName() {
		return uniqueFlowName;
	}
	
	public void setUniqueFlowName(String uniqueFlowName) {
		this.uniqueFlowName = uniqueFlowName;
	}
	
	public String getFlowViewFile() {
		return flowViewFile;
	}
	
	public void setFlowViewFile(String flowViewFile) {
		this.flowViewFile = flowViewFile;
	}
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getLastmodifyTime() {
		return lastmodifyTime;
	}

	public void setLastmodifyTime(Date lastmodifyTime) {
		this.lastmodifyTime = lastmodifyTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getModifyUser() {
		return modifyUser;
	}

	public void setModifyUser(String modifyUser) {
		this.modifyUser = modifyUser;
	}

	public int getIsDeployed() {
		return isDeployed;
	}

	public void setIsDeployed(int isDeployed) {
		this.isDeployed = isDeployed;
	}

	public Date getDeployTime() {
		return deployTime;
	}

	public void setDeployTime(Date deployTime) {
		this.deployTime = deployTime;
	}

	private String uniqueFlowName;
	private String flowViewFile;
	private Date createTime;
	private Date lastmodifyTime;
	private String createUser;
	private String modifyUser;
	private int isDeployed;
	private Date deployTime;
}
