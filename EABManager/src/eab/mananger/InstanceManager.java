package eab.mananger;

import java.rmi.RemoteException;
import java.util.Date;

import org.apache.www.ode.pmapi.InstanceManagementPortTypeProxy;
import org.apache.www.ode.pmapi.ManagementFault;
import org.apache.www.ode.pmapi.types.TActivityInfo;
import org.apache.www.ode.pmapi.types.TInstanceInfo;
import org.apache.www.ode.pmapi.types.TScopeInfo;
import org.apache.www.ode.pmapi.types.TScopeRef;

import eab.entity.EABFlowDao;
import eab.entity.ode.ODEInstance;
import eab.entity.ode.ODEInstanceDao;
import eab.entity.ode.ODEProcess;
import eab.entity.ode.ODEProcessDao;
import eab.entity.ode.ODEScope;
import eab.entity.ode.ODEScopeDao;

public class InstanceManager {
	
	private final static String instanceManagementAddress = "http://localhost:807/Workflow/InstanceManagement/";
	
	private InstanceManagementPortTypeProxy proxy = 
		new InstanceManagementPortTypeProxy(instanceManagementAddress);
	
	private ODEInstanceDao dao = new ODEInstanceDao();	
	
	public InstanceManager(){		
	}
	
	public int getInstanceCount(String flowType, 
			short state, 
			Date createTimeMin, 
			Date creatTimeMax, 
			Date lastActiveTimeMin,
			Date lastActiveTimeMax){		
		return dao.getInstanceCount(getProcess(flowType), 
				state, 
				createTimeMin, 
				creatTimeMax, 
				lastActiveTimeMin, 
				lastActiveTimeMax);
	}
	
	public ODEInstance[] getInstances(int pages, 
			int maxPerPage, 
			String flowType, 
			short state, 
			Date createTimeMin, 
			Date creatTimeMax, 
			Date lastActiveTimeMin,
			Date lastActiveTimeMax){
		return dao.getInstances(pages, 
				maxPerPage, 
				getProcess(flowType), 
				state, 
				createTimeMin, 
				creatTimeMax, 
				lastActiveTimeMin, 
				lastActiveTimeMax);
	}
	
	public ODEInstance loadInstance(long iid){
		return dao.loadInstance(iid);
	}
	
	public ODEScope loadScope(long sid){
		ODEScopeDao scopeDao = new ODEScopeDao();
		return scopeDao.loadScope(sid);
	}
	
	public void suspendInstance(long iid) {
		try {
			synchronized (proxy) {
				proxy.suspend(iid);
			}			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void resumeInstance(long iid) {
		try {
			synchronized (proxy) {
				proxy.resume(iid);
			}			
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void terminateInstance(long iid) {
		try {
			synchronized (proxy) {
				proxy.terminate(iid);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void deleteInstance(long iid) {
		try {
			synchronized (proxy) {
				proxy.delete("iid=" + String.valueOf(iid));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void recoverActivity(long iid, String activityName, String action){
		try {
			TInstanceInfo info = null;
			synchronized (proxy) {
				info = proxy.getInstanceInfo(iid);
			}
			recoverActivity(iid, getAidByName(info.getRootScope().getSiid(), activityName), action);
		} catch (ManagementFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}		
	}
	
	public void recoverActivity(long iid, long aid, String action){
		try {		
			synchronized (proxy) {
				proxy.recoverActivity(iid, aid, action);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public long getAidByName(String sid, String activityName){	
		try {
			TScopeInfo scopeInfo = null;
			synchronized (proxy) {
				scopeInfo = proxy.getScopeInfoWithActivity(Long.valueOf(sid), true);
			}			
			TActivityInfo[] activityInfos = scopeInfo.getActivities();
			for (TActivityInfo activityInfo : activityInfos) {
				if(activityInfo.getName() == activityName)
					return Long.valueOf(activityInfo.getAiid());
			}
			
			long childRet = -1;
			for (TScopeRef ref : scopeInfo.getChildren()) {
				childRet = getAidByName(ref.getSiid(), activityName);
				if(childRet > 0)
					return childRet;
			}
		} catch (ManagementFault e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
				
		return -1;
	}
	
	protected ODEProcess getProcess(String flowType) {		
		if(flowType == null || flowType.length() <= 0)
			return null;
		
		EABFlowDao flowDao = new EABFlowDao();
		ODEProcessDao processDao = new ODEProcessDao();
		
		return processDao.bindProcess(flowDao.loadFlow(flowType));
	}
}
