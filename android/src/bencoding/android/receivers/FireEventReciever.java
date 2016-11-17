package bencoding.android.receivers;

import java.util.Iterator;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiApplication;

import bencoding.android.Common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

public class FireEventReciever  extends BroadcastReceiver{

	private final static String COMPONENT_NAME = "bencoding.android.receivers.FireEventReciever";
	private final static String EVENT_NAME ="";
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String actionName = intent.getAction();
		
		Common.msgLogger("Action Name " + actionName);

		ActivityInfo info = Common.getMetaInfo(context,COMPONENT_NAME);		
		if(info == null){
			Common.msgLogger("No Meta data found, existing");
			return;
		}
		
		Common.msgLogger("Creating Bundle from Meta Data");
		//extract meda-data
		Bundle bundle = info.metaData;
		
		if(TiApplication.getInstance()!=null){
			
			KrollDict event = new KrollDict();
			event.put("actionName", actionName);
			
	        Set<String> keys = bundle.keySet();
	        Iterator<String> it = keys.iterator();
	        
	        while (it.hasNext()) {
	            String key = it.next();
	            event.put(key, bundle.get(key));
	        }
		
			TiApplication.getInstance().fireAppEvent(EVENT_NAME, event);
			
		}
	}
}
