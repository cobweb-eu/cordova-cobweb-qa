package eu.cobwebproject.qa;

import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import eu.cobwebproject.qa.automaticvalidation.IImage;
import eu.cobwebproject.qa.automaticvalidation.LaplacePhotoBlurCheck;
import eu.cobwebproject.qa.automaticvalidation.LaplacePhotoBlurCheckAndroid;
import eu.cobwebproject.qa.automaticvalidation.NativeBlurCheck;

public class CobwebQA extends CordovaPlugin {

    @Override
	public boolean execute(String action,
                           JSONArray args,
                           CallbackContext callbackContext) throws JSONException {
        if(action.equals("isBlurred")){
            try{
                String file = Environment.getExternalStorageDirectory().toString() + "/Pictures/flower_blurred.png";
                Log.i("CobwebQA", file);
                File testImage = new File(file);
                //LaplacePhotoBlurCheck lap = new LaplacePhotoBlurCheckAndroid(testImage, 200);
                LaplacePhotoBlurCheck lap = new LaplacePhotoBlurCheckAndroid();
                IImage pic1 = lap.read(testImage);
                lap.getLaplaceImage(pic1);
                Log.i("CobwebQA", "---------------------------------------------------------------");
                //Log.i("CobwebQA", "result:" + lap.getPassDecision());
                
                Bitmap pic2 = BitmapFactory.decodeFile(testImage.getPath());
                new NativeBlurCheck().getLaplaceImage(pic2);
                
                /*
                String file2 = Environment.getExternalStorageDirectory().toString() + "/Pictures/flower_sharp.jpg";
                Log.i("CobwebQA", file2);
                testImage = new File(file2);
                lap = new LaplacePhotoBlurCheckAndroid(testImage, 200);
                Log.i("CobwebQA", "---------------------------------------------------------------");
                Log.i("CobwebQA", "result:" + lap.getPassDecision());
                */
            }
            catch(Exception ex){
                Log.i("CobwebQA", ex.toString());
            }
        }
        return true;
    }
}
