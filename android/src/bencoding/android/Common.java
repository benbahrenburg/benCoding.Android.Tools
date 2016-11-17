package bencoding.android;

import bencoding.android.tools.AndroidtoolsModule;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class Common {

	private static boolean _writeToLog = true;

    public static void setDebug(boolean value){
    	_writeToLog = value;
    }
    
    public static void msgLogger(String logMessage){
    	msgLogger(logMessage,false);
    }
    public static void msgLogger(String logMessage, boolean requireLog){
    	if(_writeToLog){
    		Log.d(AndroidtoolsModule.MODULE_FULL_NAME, logMessage);
    	} else if (requireLog) {
    		Log.d(AndroidtoolsModule.MODULE_FULL_NAME, logMessage);
    	}
    }
    
	public static ActivityInfo getMetaInfo(Context context, String componentName){
		msgLogger("Getting Meta Data");
		ActivityInfo info = null;
		try {
			info = context.getPackageManager().getReceiverInfo(new ComponentName(context, componentName), PackageManager.GET_META_DATA);			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}		
		
		return info;
	}
}
