package eab.mananger;

import java.io.File;

import eab.entity.WSDLProtocol;
import eab.entity.WSDLProtocolDao;
import eab.util.EABFile;
import eab.util.EABPath;
import eab.util.FileOperator;

public class ProtocolManager {
	
	private WSDLProtocolDao dao = new WSDLProtocolDao();
	
	public ProtocolManager(){		
	}
	
	public EABFile listAllFiles(){	
		return new EABFile(EABPath.getProtocolPath());
	}
	
	public String listAllFilesXml(){
		return  listAllFiles().toXMLElement().asXML();
	}
	
	public WSDLProtocol[] listAllProtocols(){
		return dao.listAllProtocols();
	}
	
	public boolean createFolder(String folderName, String parentPath) {
		return FileOperator.createFolder(folderName, parentPath);
	}
	
	public boolean createFile(String fileName, String parentPath) {
		if(!FileOperator.createFile(fileName, parentPath))
			return false;
		
		if(fileName.endsWith(".wsdl")){
			String protocolType = fileName.substring(0, fileName.lastIndexOf("."));
			WSDLProtocol protocol = dao.loadProtocol(protocolType);
			if(protocol == null)
				return false;
			
			protocol = new WSDLProtocol();
			protocol.setType(protocolType);
			protocol.setWsdlFilePath(parentPath + File.separator + fileName);
		}

		return true;
	}
	
	public boolean deleteFile(String path){
		if(!FileOperator.delete(path))
			return false;
		
		if(path.endsWith(".wsdl")){
			String protocolType = path.substring(path.lastIndexOf(File.separator), path.lastIndexOf("."));
			dao.deleteProtocol(protocolType);
		}
		
		return true;
	}
	
	public boolean deleteFolder(String path){
		File f = new File(path);
		if(f.isDirectory() || !f.exists())
			return false;
		
		File[] files = f.listFiles();
		
		for (File file : files) {
			if(file.isDirectory())
				deleteFolder(file.getAbsolutePath());
			else if(file.isFile())
				deleteFile(file.getAbsolutePath());
		}
		
		return true;
	}
	
	public String readFile(String path) {
		return FileOperator.read(path);
	}
	
	public boolean writeFile(String path, String data){
		return FileOperator.write(path, data);
	}
}
