package eab.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static eab.util.ZipCompressor.*;
import static eab.util.FileOperator.*;

import java.io.File;
import java.io.FileWriter;

/**
 * @author fangjt
 *
 */
public class ZipCompressorTest {
	
	private String fileName = "__TestZipComp.txt";
	private String folderName = "__TestArrayZipComp";
	private String fileContent = "ABCDEFGHIJKLMNOPQ";
	private String zipFileString = "__TestZipComp.txt.zip";
	private String zipFileArrayString = "__TestArrayZipComp.zip";

	/**
	 * Build file for test
	 * 
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		File file = new File(fileName);
		File folder = new File(folderName);
		File folderFile = new File(folderName + File.separator + fileName);

		if (!file.exists()) {
			file.createNewFile();
		}
		FileWriter wrt = new FileWriter(file);
		wrt.write(fileContent);
		wrt.close();

		if (!folder.exists() || !folder.isDirectory()) {
			folder.mkdir();
		}
		if (!folderFile.exists()) {
			folderFile.createNewFile();
		}
		wrt = new FileWriter(folderFile);
		wrt.write(fileContent + "MQ");
		wrt.close();
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		delete(fileName);
		delete(folderName);
		delete(zipFileString);
		delete(zipFileArrayString);
	}

	/**
	 * Test method for {@link eab.util.ZipCompressor#zip(java.lang.String[], java.lang.String)}.
	 */
	@Test
	public void testZipStringArrayString() {
		try {
			zip(new String[]{fileName, folderName}, zipFileArrayString);
		} catch (Exception e) {
			fail("Message: " + e.getMessage() + "\nTrace: " + e.getStackTrace());
		}
		assertTrue(true);
	}

	/**
	 * Test method for {@link eab.util.ZipCompressor#zip(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testZipStringString() {
		try {
			zip(fileName, zipFileString);
		} catch (Exception e) {
			fail("Message: " + e.getMessage() + "\nTrace: " + e.getStackTrace());
		}
		assertTrue(true);
	}

}
