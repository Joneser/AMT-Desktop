package core;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author davidjones
 *
 */
public class AbcManager
{
    public static Map<String, String> notes = new HashMap<String, String>();
    
    /**
     * @author davidjones 
     * 
     * This method is used to populate a Hashtable with notes of
     * the musical scale and their corresponding ABC representations.
     */
    public static void populateTable() {
        notes.put("A3", "A");
        notes.put("B3", "B");
        notes.put("Bb3", "=B");
        notes.put("C3", "C");
        notes.put("C#3", "^C");
        notes.put("D3", "D");
        notes.put("Eb3", "=E");
        notes.put("E3", "E");
        notes.put("F3", "F");
        notes.put("F#3", "^F");
        notes.put("G3", "G");
        notes.put("G#3", "^G");
        notes.put("A4", "a");
        notes.put("Bb4", "=b");
        notes.put("B4", "^b");
        notes.put("C4", "c");
        notes.put("C#4", "^c");
        notes.put("D4", "d");
        notes.put("E4", "e");
        notes.put("Eb4", "=e");
        notes.put("F4", "f");
        notes.put("F#4", "^f");
        notes.put("G4", "g");
        notes.put("G#4", "^g");
        notes.put("C5", "c'");
    }
    
    /**
     * 
     * @param input {String} The input string is 
     * @return
     */
    public static List<String> convertToAbc(final String input)
    {
        populateTable();
        List<String> finalAbc = new ArrayList<String>();
        final String[] data = input.split(",");
        List<String> proc = new ArrayList<String>(Arrays.asList(data));
        for(int i = 0; i < proc.size(); i++)
        {
            final String[] current = proc.get(i).split(" ");
            if(current.length < 2){
                proc.remove(i);
                i--;
            }
            else
            {
                final int noteLength = Integer.parseInt(current[0].replaceAll("^\\s+", ""));
                final String finalNote = current[1] + noteLength;
                finalAbc.add(finalNote);
            }
        }
        return finalAbc;
    }

    /**
     * 
     * @param abcData {Vector<String>} This is a string vector which contains all of the abc notation required for the output file.
     * @param tuneName {String} This is the name of the file to be produced as defined by the user in the GUI.
     * @throws IOException
     */
    public static void createAbcFile(final List<String> abcData, final String tuneName) throws IOException
    {
        final BufferedWriter fileOut = new BufferedWriter(new FileWriter(new File("/Users/davidjones/Automatic Music Transcription/src/examples/Media/" + tuneName + ".abc")));

        // Begin Writing File Header.
        fileOut.write("X:0\n");
        fileOut.write("T:" + tuneName + "\n");
        fileOut.write("Q:1/4=100\n");
        fileOut.write("M:2/4\n");
        fileOut.write("C:David Jones\n");
        fileOut.write("K:C\n");
        // Finish writing File Header.

        fileOut.write("|:"); // Start of first bar
        
        String n;
        String h;
        int count = 0;
        int index = 0;
        for(int i = 0; i < abcData.size(); i++)
        {
            final char[] a = abcData.get(i).toCharArray();
            for(int j = 0; j < a.length; j++){
                Character c = a[j];
                if(Character.isDigit(c)){
                    index = j;
                    j = a.length;
                }
            }
            h = abcData.get(i).substring(0, index+1);
            n = (String)notes.get(h);

            fileOut.write(n + abcData.get(i).substring(index+1, abcData.get(i).length()) + " ");

            if(i%8 == 7 && i > 0)
            {
                fileOut.write("| ");
                count++;
            }
            if(count == 8)
            {
                fileOut.write("\n");
                count = 0;
            }
        }
        fileOut.flush();
        fileOut.close();
    }
}