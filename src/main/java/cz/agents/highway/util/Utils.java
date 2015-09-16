package cz.agents.highway.util;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;


public class Utils {

    private final static Logger logger = Logger.getLogger(Utils.class);


    public static URL getResourceUrl(String resourcePath) throws FileNotFoundException {

        logger.trace("input path: " + resourcePath);
        URL url = Utils.class.getClassLoader().getResource(resourcePath);
        logger.trace("output url: " + url);

        if (url == null) {
            throw new FileNotFoundException("File in: " + resourcePath + " not found in the resources");
        }

        return url;
    }

    public static File getFileWithSuffix(String resourceFolderPath, String suffix) throws FileNotFoundException {
        File folder = getResourceFile(resourceFolderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            for (File f : files) {
                if (f.getName().endsWith(suffix)) {
                    return new File(f.getAbsolutePath());
                }
            }
        }
        return null;
    }

    // this might get more complex regarding OpenDS config
    public static int name2ID(String name) {
        return Integer.parseInt(name);
    }

    public static File getResourceFile(String path) throws FileNotFoundException {
        URL url = getResourceUrl(path);
        File file = new File(url.getPath());
        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }


}
