package Core;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author davidjones
 *
 */
public class FileRetriever {

    public static List<String> fileNames = new ArrayList<String>();
    public static String[] children;

    /**
     * 
     * @param d
     * @return {Vector} The list of files that is to be displayed to the user in the GUI.
     */
    public static List<String> getFiles(String d) 
    {
        final File dir = new File(d);
        children = dir.list();

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(final File dir, final String name) {
                return !name.startsWith(".");
            }
        };
        children = dir.list(filter);

        fileNames = new ArrayList<String>(Arrays.asList(children));
        return fileNames;
    }
}