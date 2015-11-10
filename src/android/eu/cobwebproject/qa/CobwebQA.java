package eu.cobwebproject.qa;

import java.io.File;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;

import android.util.Log;
import eu.cobwebproject.qa.automaticvalidation.BlurCheckAndroid;

public class CobwebQA extends CordovaPlugin {
	
    @Override
	public boolean execute(String action,
                           JSONArray args,
                           final CallbackContext callbackContext) throws JSONException {
        if(action.equals("isBlurred")){
        	final String filePath = args.getString(0);
        	final int threshold = args.getInt(1);
			Log.i("CobwebQA", "Blur check on " + filePath + " with threshold " + threshold);
            	
        	cordova.getThreadPool().execute(new Runnable() {
        		@Override
        		public void run() {
        			BlurCheckAndroid lap = new BlurCheckAndroid(
        					new File(filePath),
        					threshold,
        					false,
        					cordova.getActivity().getApplicationContext());
        			lap.run();

        			Log.i("CobwebQA", "Result: " + lap.pass);
        			callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.OK, !lap.pass));
        		}
        	});
        	
            return true;
        }
        else{
        	// no such method
        	Log.w("CobwebQA", "No method '" + action + "' found");
        	return false;
        }
    }
}
