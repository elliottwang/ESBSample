package eab.deploy.zip;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class JbiSaBuilder {
		
	public static final String DESCRIPTOR_FILE = "META-INF/jbi.xml";
	
	public static void buildSa(String outFile, HashMap<String, byte[]> saContent, String jbi) {
		ZipOutputStream zos = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(outFile));

			// write su content files
			for (Map.Entry<String, byte[]> entry : saContent.entrySet()) {
				zos.putNextEntry(new ZipEntry(entry.getKey()));
				zos.write(entry.getValue());
			}

			// write su jbi description
			zos.putNextEntry(new ZipEntry(DESCRIPTOR_FILE));
			zos.write(jbi.getBytes());

			zos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
