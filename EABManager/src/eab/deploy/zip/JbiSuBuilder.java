package eab.deploy.zip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JbiSuBuilder {

	public static final String DESCRIPTOR_FILE = "META-INF/jbi.xml";
	
	public static byte[] buildSu(HashMap<String, String> suContent, String jbi) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
		ZipOutputStream zos = null; 

		try {
			zos = new ZipOutputStream(baos);
			
			// write su content files
			for (Map.Entry<String, String> entry : suContent.entrySet()) {

				zos.putNextEntry(new ZipEntry(entry.getKey()));
				zos.write(entry.getValue().getBytes());
			}

			// write su jbi description
			zos.putNextEntry(new ZipEntry(DESCRIPTOR_FILE));
			zos.write(jbi.getBytes());

			zos.flush();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			if(zos != null){
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return baos.toByteArray();
	}
}
