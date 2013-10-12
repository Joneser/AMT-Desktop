package Core;

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D;
import java.io.*;
import javax.sound.sampled.*;

import Utilities.MathUtility;
import Utilities.SoundUtility;


/**
 * 
 * @author davidjones
 *
 */
public class WavProcessor
{

	/**
	 * 
	 * @param myAudio
	 * @return
	 * @throws UnsupportedAudioFileException
	 * @throws IOException
	 * @throws LineUnavailableException
	 */
    public static float[] getData(final File myAudio) throws UnsupportedAudioFileException, IOException, LineUnavailableException
    {
        SoundUtility.signalToFile(myAudio);
        final String txtName = myAudio.getAbsolutePath().replace(".wav", ".txt");
        float[] myFFTdata = SoundUtility.fileToArray(new File(txtName));

        FloatFFT_1D fft = new FloatFFT_1D(myFFTdata.length);
        fft.realForward(myFFTdata);

        myFFTdata = MathUtility.getAbs(myFFTdata);
        final float[] power = getFftPower(myFFTdata);

        float[] testArray = new float[3500];
        System.arraycopy(power, 0, testArray, 0, testArray.length);

        return testArray;
    }

    /**
     * 
     * @param inputFFT
     * @return
     */
    public static float[] getFftPower(final float[] inputFFT) // Power = FFT.*conj(FFT)/length(FFT);
    {
        final float[] conj = MathUtility.getConj(inputFFT);
        float[] realFFT = MathUtility.removeI(inputFFT);
        float[] power = new float[realFFT.length];
        for (int i = 0; i < conj.length; i++)
        {
            power[i] = (conj[i]*realFFT[i])/conj.length;
        }
        power = MathUtility.normalise(power);

        return power;
    }

    /**
     * 
     * @param input
     * @return
     */
    public static float getEnergyDb(float[] input){
        //energy = abs(fft(currentSample)).^2;
        //energy_db = log10(energy./min(energy));
        for(int i = 0; i < input.length; i++) {
            input[i] = input[i] * input[i];
        }
        Double min = (double)MathUtility.getMin(input);
        for(int i = 0; i < input.length; i++) {
            double d = input[i];
            input[i] = (float)Math.log10(d/min);
        }

        return MathUtility.getMax(input);
    }
}