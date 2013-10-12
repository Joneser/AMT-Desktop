package Core;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import Utilities.MathUtility;
import Utilities.SoundUtility;
import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;

/**
 * 
 * @author davidjones
 *
 */
public class IncProcessor
{
    public static List<String> notes;

    /**
     * 
     * @param tuneEntry
     * @return
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public static String getTune(final File tuneEntry) throws IOException, UnsupportedAudioFileException
    {
        String totalTune = "";
        SoundUtility.signalToFile(tuneEntry);
        final String txtName = tuneEntry.getName().replace(".wav", ".txt");
        final float[] myData = SoundUtility.fileToArray(new File("/Users/davidjones/Automatic Music Transcription/src/examples/NoteValues/" + txtName));

        final int arrayIncrement = 4096;
        int loopGuard = 0;
        int arrayCurrent = 0;

        List<String> files = FileRetriever.getFiles("/Users/davidjones/Automatic Music Transcription/src/examples/NoteValues/");
        for(int i = 0; i < files.size(); i++)
        {
            if(files.get(i).length() > 12){
                files.remove(i);
                i--;
            }
        }
        
        float[] prevPower = new float[2048];
        float prevEnergyDb = 0;
        int noteLength = 1;
        String prevNote = "";
        while(loopGuard < (myData.length - arrayIncrement)) {
            float maxTotal = 0;
            String maxName = "";
            float[] currentSample = new float[arrayIncrement];
            System.arraycopy(myData, loopGuard, currentSample, 0, currentSample.length);

            FloatFFT_1D fft = new FloatFFT_1D(currentSample.length);
            fft.realForward(currentSample);

            currentSample = MathUtility.getAbs(currentSample);

            float[] currentPower = WavProcessor.getFftPower(currentSample);
            float currEnergyDb = WavProcessor.getEnergyDb(currentSample);
            float[] halfPower = new float[2048];

            System.arraycopy(currentPower, 0, halfPower, 0, halfPower.length);

            for(int i = 0; i < files.size(); i++)
            {
                if(files.get(i).contains(".txt"))
                {
                    float[] libArray = SoundUtility.fileToArray(new File("/Users/davidjones/Automatic Music Transcription/src/examples/NoteValues/" + files.get(i)));
                    float curTot = MathUtility.getMatchValue(halfPower, libArray);
                    if(curTot > maxTotal)
                    {
                        maxTotal = curTot;
                        maxName = files.get(i);
                    }
                }
            }

            if(maxName.equals(prevNote) && currEnergyDb < prevEnergyDb) {
                noteLength++;
                System.arraycopy(currentPower, 0, prevPower, 0, currentPower.length-1);
                prevEnergyDb = currEnergyDb;
                prevNote = maxName;
                arrayCurrent += arrayIncrement;
                loopGuard += arrayIncrement;
            }
            else {
                totalTune += "," + noteLength + " " + maxName;
                noteLength = 1;
                System.arraycopy(currentPower, 0, prevPower, 0, currentPower.length-1);
                prevNote = maxName;
                prevEnergyDb = currEnergyDb;
                arrayCurrent += arrayIncrement;
                loopGuard += arrayIncrement;
            }
        }

        notes = new ArrayList<String>(Arrays.asList(totalTune.split(",")));
        for(int i = 0; i < notes.size(); i++)
        {
            if(notes.get(i).length()<3)
            {
                notes.remove(i);
                i--;
            }
        }
        totalTune = totalTune.replaceAll(".wav.txt", "");
        System.out.println(totalTune);

        return totalTune;
    }
}