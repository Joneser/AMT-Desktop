package Core;

import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * This class is simply used as the entry point for the application and starts up the GUI.
 * @author davidjones
 *
 */
public class MasterClass
{

	/**
	 * This method is the initialisation method for the application.
	 * 
	 * @param args
	 * @throws IOException
	 * @throws UnsupportedAudioFileException
	 * @throws LineUnavailableException
	 */
    public static void main(final String[] args) throws IOException, UnsupportedAudioFileException, LineUnavailableException
    {
        final GUI transcriber = new GUI();
        transcriber.setVisible(true);
    }
}
