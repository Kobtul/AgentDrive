package cz.agents.highway.util;

import org.junit.Test;

import javax.rmi.CORBA.Util;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;

import static org.junit.Assert.*;


public class UtilsTest {

    final String testFolder = "test";
    final String testFilePath = "test/testFile.txt";

    @Test
    public void testTestingFileExists() {
        File file = new File("src/test/resources/" + testFilePath);
        assertTrue("Testing file \"" + testFilePath + " should be in the test resources for proper " + getClass().getName() + " functionality", file.exists());
    }

    @Test
    public void testGetResourceUrl() throws Exception {
        URL url = Utils.getResourceUrl(testFilePath);
        assertNotNull(url);
        assertTrue(new File(url.toURI()).exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetResourceUrlException() throws Exception {
        Utils.getResourceFile("notExistingfolder/andfile4535.omg");
    }

    @Test
    public void testGetFileWithSuffix() throws Exception {
        File file = Utils.getFileWithSuffix(testFolder, ".txt");
        assertNotNull(file);
    }

    @Test(expected = NumberFormatException.class)
    public void testName2IDException() throws Exception {
        Utils.name2ID("car0");
    }

    @Test
    public void testName2ID() throws Exception {
        assertEquals("String \"1\" should convert to 1", Utils.name2ID("1"), 1);
    }

    @Test
    public void testGetFile() throws Exception {
        File file = Utils.getResourceFile(testFilePath);

        assertNotNull(file);
        assertTrue(file.exists());
    }

    @Test(expected = FileNotFoundException.class)
    public void testGetFileException() throws Exception {
        Utils.getResourceFile("notExistingfolder/andfile34#.omg");
    }
}