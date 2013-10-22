package utilities;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import core.IncProcessor;

/**
 * 
 * @author davidjones
 *
 */
public class SoundUtility
{
	/**
	 * 
	 * @param f {File} The file to which the signal data is to be written.
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 */
    public static void signalToFile(final File myFile) throws IOException, UnsupportedAudioFileException
    {
        final AudioInputStream inputStream = AudioSystem.getAudioInputStream(myFile);
        final int numBytes = inputStream.available();
        final byte[] buffer = new byte[numBytes];
        inputStream.read(buffer, 0, numBytes);

        final String newFile = myFile.getName().replace(".wav", ".txt");
        
        final BufferedWriter fileOut = new BufferedWriter(new FileWriter(new File(FileUtility.getNoteValuePath() + newFile)));
        final ByteBuffer myBB = ByteBuffer.wrap(buffer);
        myBB.order(ByteOrder.LITTLE_ENDIAN);
        int limit = myBB.limit(); // bytes in buffer
        System.out.println("Limit: " + limit);

        for ( int offset = 0; offset < limit - 5; offset++ )
        {
        	System.out.println("Offset: " + offset);
            int current = myBB.getInt(offset); // was a short before PMD - need to compare
            System.out.println("Current: " + current);
            fileOut.write(String.valueOf(current));
            fileOut.newLine();
        }

        fileOut.flush();
        fileOut.close();
        inputStream.close();
    }

    /**
     * 
     * @param array
     * @param s
     * @throws IOException
     */
    public static void arrayToFile(final float[] array, final String myString) throws IOException
    {
        final BufferedWriter fileOut = new BufferedWriter(new FileWriter(new File(myString)));
        final int arrLength = array.length;
        for(int i = 0; i < arrLength; i++)
        {
            fileOut.write(String.valueOf(array[i]));
            fileOut.newLine();
        }
        fileOut.flush();
        fileOut.close();
    }

    /**
     * 
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static float[] fileToArray(final File myFile) throws FileNotFoundException, IOException
    {
        final FileInputStream fstream = new FileInputStream(myFile);
        // Get the object of DataInputStream
        final DataInputStream inputStream = new DataInputStream(fstream);
        final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        int count = 0;
        final FileReader fileReader = new FileReader(myFile);
        final LineNumberReader lineReader = new LineNumberReader(fileReader);

        while (lineReader.readLine() != null)
        {
          count++;
        }

        float[] signalData = new float[count];

        String strLine;
        int index = 0;
        //Read File Line By Line
        while ((strLine = bufferedReader.readLine()) != null)
        {
            final float input = Float.parseFloat(strLine);
            signalData[index] = input;
            index++;
        }
        fileReader.close();
        bufferedReader.close();
        inputStream.close();
        fstream.close();
        
        return signalData;
    }

    /**
     * 
     * @param f
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static String fileToString(final File myFile) throws FileNotFoundException, IOException
    {
        String output = "";

        FileInputStream fstream = new FileInputStream(myFile);
        DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

        String strLine;
        while ((strLine = br.readLine()) != null)
        {
            output += strLine + '\n';
        }
        br.close();
        in.close();
        fstream.close();

        return output;
    }

    /**
     * 
     * @param threshold
     * @return
     */
    public static List<Integer> getNoteLengths(final int threshold)
    {
        List<Integer> lengths = new ArrayList<Integer>();

        int count = 1;
        int procSize = IncProcessor.notes.size() - 1;
        for(int i = 0; i < procSize; i++)
        {
            String[] currentElement = IncProcessor.notes.get(i).split(" ");
            String[] nextElement = IncProcessor.notes.get(i+1).split(" ");

            if(currentElement[1].equals(nextElement[1]))
            {
                IncProcessor.notes.get(i+1);
                count += Integer.parseInt(nextElement[0]) ;
                i--;
            }
            else if(!currentElement[1].equals(nextElement[1]) && count <= threshold)
            {
                IncProcessor.notes.get(i);
                count = 1;
                i--;
            }
            else if(!currentElement[1].equals(nextElement[1]) && count > threshold)
            {
                lengths.add(i, count);
                count = 1;
            }
        }
        IncProcessor.notes.remove(IncProcessor.notes.size()-1);
        return lengths;
    }
}