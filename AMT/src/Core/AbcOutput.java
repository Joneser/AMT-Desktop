package Core;

import abc.notation.Tune;
import abc.parser.TuneParser;
import abc.ui.swing.JScoreComponent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import Utilities.SoundUtility;

/**
 * 
 * @author davidjones
 *
 */
public class AbcOutput
{
    public static JScoreComponent scoreUI = new JScoreComponent();

    /**
     * 
     * @param abc
     * @return {JScoreComponent} Returns the musical score to be displayed to the user.
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static JScoreComponent getMusic(final File abc) throws FileNotFoundException, IOException
    {
        final String myMusic = SoundUtility.fileToString(abc);
        final Tune tune = new TuneParser().parse(myMusic);
        scoreUI.setTune(tune);
        return scoreUI;
    }
}
