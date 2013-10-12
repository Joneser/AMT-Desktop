package Core;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author davidjones
 */
public class CorrectDisturbance {
    
	/**
	 * 
	 * @param finalAbc
	 * @return
	 */
    public static List<String> smoothTrack(final List<String> finalAbc)
    {

        List<String> smoothAbc = new ArrayList<String>();
        int currentCount = 0;
        int originalIndex = 0;
        boolean isSet = false;
        int index = 0;
        //final int finSize = finalAbc.size() - 1;
      
        for(int i = 0; i < finalAbc.size() - 1; i++)
        {
            if(finalAbc.get(i).substring(0, 1).equals(finalAbc.get(i+1).substring(0, 1)))
            {
                if(!isSet){
                    originalIndex = i;
                    isSet = true;
                    final char[] data = finalAbc.get(i).toCharArray();
                    for(int j = 0; j < data.length; j++){
                        final Character c = data[j];
                        if(Character.isDigit(c)){
                            index = j;
                            j = data.length;
                        }
                    }
                }
                currentCount += Integer.parseInt(finalAbc.get(i).substring(finalAbc.get(i).length()-1, finalAbc.get(i).length()));
            }
            else if(!finalAbc.get(i).substring(0, 1).equals(finalAbc.get(i+1).substring(0, 1))) {
                if(currentCount < 2) {
                    currentCount = 0;
                    isSet = false;
                    index = 0;
                }
                else {
                    currentCount += Integer.parseInt(finalAbc.get(i).substring(finalAbc.get(i).length()-1, finalAbc.get(i).length()));
                    smoothAbc.add(finalAbc.get(originalIndex).substring(0, index+1) + Integer.toString(currentCount));
                    currentCount = 0;
                    isSet = false;
                    index = 0;
                }
            }
            if(i == finalAbc.size() - 2 && currentCount >= 2) {
                smoothAbc.add(finalAbc.get(originalIndex).substring(0, index+1) + Integer.toString(currentCount));
                currentCount = 0;
            }
        }
        return smoothAbc;
    }
}
