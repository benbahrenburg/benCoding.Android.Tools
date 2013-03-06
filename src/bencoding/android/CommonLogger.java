package bencoding.android;

import bencoding.android.tools.AndroidtoolsModule;
import android.util.Log;

public class CommonLogger {

	private static boolean _writeToLog = false;

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
}
