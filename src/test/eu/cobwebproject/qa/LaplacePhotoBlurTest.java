package eu.cobwebproject.qa;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.junit.Test;

import eu.cobwebproject.qa.automaticvalidation.LaplacePhotoBlurCheck;
import eu.cobwebproject.qa.automaticvalidation.LaplacePhotoBlurCheckAndroid;
import junit.framework.TestCase;

public class LaplacePhotoBlurTest extends TestCase{

    /**
     * Test a blurred image fails LaplacePhotoBlurCheck
     */
    @Test
    public void testBlurred() {
        URL url = this.getClass().getResource("flower_blurred.png");

        try{
            File testImage = new File(url.toURI());
            LaplacePhotoBlurCheck lap = new LaplacePhotoBlurCheckAndroid(testImage, 200);
            assertFalse(lap.getPassDecision());
        }
        catch(URISyntaxException ex){
            fail(ex.getMessage());
        }
    }

    /**
     * Test a sharp image passes LaplacePhotoBlurCheck
     */
    @Test
    public void testSharp() {
        URL url = this.getClass().getResource("flower_sharp.jpg");      

        try{
            File testImage = new File(url.toURI());
            LaplacePhotoBlurCheck lap = new LaplacePhotoBlurCheckAndroid(testImage, 200);
            assertTrue(lap.getPassDecision());
        }
        catch(URISyntaxException ex){
            fail(ex.getMessage());
        }
    }
}
