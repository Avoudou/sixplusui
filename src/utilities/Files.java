package utilities;

import java.io.File;

public class Files {

    /** Returns true iff a file with the given fileName exists */
    public static boolean fileExists(String fileName) {
        File file = new File(fileName);
        // exists() returns true for directories as well
        return file.exists() && !file.isDirectory();
    }

}
