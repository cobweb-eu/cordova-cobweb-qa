package eu.cobwebproject.qa;

import java.io.File;

import android.content.Context;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import eu.cobwebproject.qa.automaticvalidation.BlurCheckAndroid;

public class CobwebQA extends CordovaPlugin {
    //TODO: convert these to parameters
    private final int threshold = 1500;
    private final boolean debug = true;

    @Override
	public boolean execute(String action,
                           JSONArray args,
                           CallbackContext callbackContext) throws JSONException {
        if(action.equals("isBlurred")){
            try{
                Context ctx = this.cordova.getActivity().getApplicationContext(); 

                String file = Environment.getExternalStorageDirectory().toString() + "/Pictures/flower_blurred.png";
                Log.i("CobwebQA", file);

                File testImage = new File(file);
                BlurCheckAndroid lap = new BlurCheckAndroid(testImage, threshold, debug, ctx);
                lap.run();
                Log.i("CobwebQA", "---------------------------------------------------------------");
                Log.i("CobwebQA", "result:" + lap.pass);
                
                String file2 = Environment.getExternalStorageDirectory().toString() + "/Pictures/flower_sharp.jpg";
                Log.i("CobwebQA", file2);
                testImage = new File(file2);
                lap = new BlurCheckAndroid(testImage, threshold, debug,ctx);
                lap.run();
                Log.i("CobwebQA", "---------------------------------------------------------------");
                Log.i("CobwebQA", "result:" + lap.pass);
            }
            catch(Exception ex){
                Log.i("CobwebQA", ex.toString());
            }
        }
        return true;
    }
}
