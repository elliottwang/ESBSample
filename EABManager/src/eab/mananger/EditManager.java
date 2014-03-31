package eab.mananger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;

import eab.entity.EABFlowEditor;
import eab.entity.EABFlowEditorDao;
import eab.util.EABPath;

public class EditManager {
	
	private EABFlowEditorDao tempDao = new EABFlowEditorDao();
	
	public EditManager(){		
	}
	
	public String getEmptyFlowViewFile(){
		return readFile(empty_Flow_ViewFile);
	}
	
	public boolean isFlowExists(String flowName){
		return tempDao.loadFlow(flowName) != null;
	}
	
	public void saveTempFlow(String flowName, String flowViewData){		
		EABFlowEditor tempFlow = tempDao.loadFlow(flowName);
		if(tempFlow == null){
			tempFlow = new EABFlowEditor();
			tempFlow.setUniqueFlowName(flowName);
			tempFlow.setCreateTime(new Date());
			tempFlow.setLastmodifyTime(new Date());
			tempFlow.setCreateUser("");
			tempFlow.setModifyUser("");
			tempFlow.setIsDeployed(0);
			tempFlow.setDeployTime(new Date());
		}		
		tempFlow.setFlowViewFile(flowViewData);
		
		tempDao.saveOrUpdateTempFlow(tempFlow);
	}
	
	public EABFlowEditor[] loadTempFlows(){
		return tempDao.getTempFlows(1, 1000);
	}
	
	public void deployFlow(String flowName, String flowViewData){
		saveTempFlow(flowName, flowViewData);
	}
	
	protected String readFile(String filePath){
		StringBuffer sb = new StringBuffer();
		BufferedReader in = null;

		try {
			in = new BufferedReader(new FileReader(filePath));
			String s;
			while((s = in.readLine()) != null){
				sb.append(s);
				sb.append(System.getProperty("line.separator"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		
		return sb.toString();
	}	
	
	private static String empty_Flow_ViewFile = EABPath.getTemplatePath() + "flowview/EmptyFlow.xml";
}
