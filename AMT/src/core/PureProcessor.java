package core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sound.sampled.UnsupportedAudioFileException;

import utilities.MathUtility;
import utilities.SoundUtility;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;


/**
 *
 * @author David Jones
 */
public class PureProcessor 
{
    public static List<String> notes;
    public static String[] noteNames = {"C0", "Csharp0", "D0", "Eb0", "E0", "F0", "Fsharp0", "G0", "Gsharp0", "A0", "Bb0", "B0", "C1", "Csharp1", "D1", "Eb1", "E1", "F1", "Fsharp1", "G1", "Gsharp1", "A1", "Bb1", "B1", "C2", "Csharp2", "D2", "Eb2", "E2", "F2", "Fsharp2", "G2", "Gsharp2", "A2", "Bb2", "B2", "C3", "Csharp3", "D3", "Eb3", "E3", "F3", "Fsharp3", "G3", "Gsharp3","A3", "Bb3", "B3", "C4", "Csharp4", "D4", "Eb4", "E4", "F4", "Fsharp4", "G4", "Gsharp4", "A4", "Bb4", "B4", "C5", "Csharp5", "D5", "Eb5", "E5", "F5", "Fsharp5", "G5", "Gsharp5", "A5", "Bb5", "B5", "C6", "Csharp6", "D6", "Eb6", "E6", "F6", "Fsharp6", "G6", "Gsharp6", "A6", "Bb6", "B6", "C7", "Csharp7", "D7", "Eb7", "E7", "F7", "Fsharp7", "G7", "Gsharp7", "A7", "Bb7", "B7", "C8", "Csharp8", "D8", "Eb8"};
    public static double[] noteFrequencies = {16.35, 17.32, 18.35, 19.45, 20.60, 21.83, 23.12, 24.50, 25.96, 27.50, 29.14, 30.87, 32.70, 34.65, 36.71, 38.89, 41.20, 43.65, 46.25, 49.00, 51.91, 55.00, 58.27, 61.74, 65.41, 69.30, 73.42, 77.78, 82.41, 87.31, 92.50, 98.00, 103.83, 110.00, 116.54, 123.47, 130.81, 138.59, 146.83, 155.56, 164.81, 174.61, 185.00, 196.00, 207.65, 220.00, 233.08, 246.94, 261.63, 277.18, 293.66, 311.13, 329.63, 349.63, 369.99, 392.00, 415.30, 440.00, 466.16, 493.88, 523.25, 554.37, 587.33, 622.25, 659.26, 698.46, 739.99, 783.99, 830.61, 880.00, 932.33, 987.77, 1046.50, 1108.73, 1174.66, 1244.51, 1318.51, 1396.91, 1479.98, 1567.98, 1661.22, 1760.00, 1864.66, 1975.53, 2093.00, 2217.46, 2349.32, 2489.02, 2637.02, 2793.83, 2959.96, 3135.96, 3322.44, 3520.00, 3729.31, 3951.07, 4186.01, 4434.92, 4698.64, 4978.03};
    public final static float SAMPLING_FREQ = 44100;
    
    /**
     * 
     * @param tuneEntry
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     * @throws UnsupportedAudioFileException
     */
    public static String getTune(final File tuneEntry) throws FileNotFoundException, IOException, UnsupportedAudioFileException
    {
        String totalTune = "";
        SoundUtility.signalToFile(tuneEntry);
        final String txtName = tuneEntry.getName().replace(".wav", ".txt");
        String directoryPath = "";
        
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0) {
        	directoryPath = "C:\\Users\\David\\git\\AMT-Desktop\\AMT\\src\\notevalues\\";
        } else if(OS.indexOf("mac") >= 0) {
        	directoryPath = "/Users/davidjones/Automatic Music Transcription/src/notevalues/";
        }
        
        
        final float[] myData = SoundUtility.fileToArray(new File(directoryPath + txtName));

        final int arrayIncrement = 4096;
        int loopGuard = 0;
        int arrayCurrent = 0;

        float[] prevPower = new float[2048];
        float prevEnergyDb = 0;
        int noteLength = 1;
        String prevNote = "";
        final int dataLength = myData.length;
        
        while(loopGuard < (dataLength - arrayIncrement))
        {
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
            float index = MathUtility.getMaxIndex(halfPower);

            float fundamentalFreq = Math.round(SAMPLING_FREQ * (index) / currentPower.length);
            maxName = getMinDif(noteFrequencies, fundamentalFreq);

//          ff = round(FS*(index - 1)/length(currentFFT)); % The fundamental frequency of this section of the tune.
            if(currEnergyDb > 0)
            {
                if(maxName.equals(prevNote) && currEnergyDb < prevEnergyDb)
                {
                    noteLength++;
                }
                else
                {
                    totalTune += "," + noteLength + " " + maxName;
                    noteLength = 1;
                    System.arraycopy(currentPower, 0, prevPower, 0, currentPower.length-1);
                }
            }
            prevEnergyDb = currEnergyDb;
            prevNote = maxName;
            arrayCurrent += arrayIncrement;
            loopGuard += arrayIncrement;
        }

        notes = new ArrayList<String>(Arrays.asList(totalTune.split(",")));
        for(int i = 0; i < notes.size(); i++)
        {
            if(notes.get(i).length() < 3)
            {
                notes.remove(i);
                i--;
            }
        }
        totalTune = totalTune.replaceAll(".wav.txt", "");
        System.out.println(totalTune);
        return totalTune;
    }

    /**
     * 
     * @param frequencies
     * @param ff
     * @return
     */
    public static String getMinDif(final double[] frequencies, final float fundamentalFreq)
    {
        int index = -Integer.MAX_VALUE;
        double currentMin = Double.MAX_VALUE;
        int freqLength = frequencies.length;
        for(int i = 0; i < freqLength; i++)
        {
            double testMin = Math.abs(fundamentalFreq - frequencies[i]);
            if(testMin < currentMin)
            {
                currentMin = testMin;
                index = i;
            }
        }
        return noteNames[index];
    }
}
