package utilities;

public class FileUtility {
	
	/**
	 * 
	 * @param _floatArray
	 * @return {Array} A float array where all of the contents of the input have been changed changed to their absolute value.
	 */
    public static String getNoteValuePath() {
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
        	return "C:\\Users\\David\\git\\AMT-Desktop\\AMT\\src\\notevalues\\";
        } else if(OS.indexOf("mac") >= 0) {
        	return "/Users/davidjones/Automatic Music Transcription/src/notevalues/";
        } else {
        	return "";
        }
    }
    
	/**
	 * 
	 * @param _floatArray
	 * @return {Array} A float array where all of the contents of the input have been changed changed to their absolute value.
	 */
    public static String getTestMediaPath() {        
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
        	return "C:\\Users\\David\\git\\AMT-Desktop\\AMT\\src\\testmedia\\";
        } else if(OS.indexOf("mac") >= 0) {
        	return "/Users/davidjones/Automatic Music Transcription/src/testmedia/";
        } else {
        	return "";
        }
    }

}
