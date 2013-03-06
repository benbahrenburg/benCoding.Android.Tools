package bencoding.android.receivers;

import org.appcelerator.titanium.TiApplication;


import bencoding.android.CommonLogger;

import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;


public class BootReciever  extends BroadcastReceiver{
	private final static String COMPONENT_NAME = "bencoding.android.receivers.BootReciever";
	private final static String BOOT_TYPE_START ="START";
	private final static String START_SEND_TO_BACK="sendToBack";
	
	private final static String BOOT_TYPE_NOTIFY ="NOTIFY";
	private final static String NOTIFY_TITLE ="title";
	private final static String NOTIFY_MESSAGE = "message";
	
	private final static String BOOT_TYPE_PROPERTY ="PROPERTYBASED";
	
	private final static String PROP_ENABLED ="enabled_property_to_reference";
	private final static String PROP_BOOT_TYPE = "bootType_property_to_reference";
	private final static String PROP_SEND_TO_BACK = "sendToBack_property_to_reference";
	private final static String PROP_ICON = "icon_property_to_reference";
	private final static String PROP_TITLE = "title_property_to_reference";
	private final static String PROP_MESSAGE= "message_property_to_reference";
	private final static int APP_ID = 1234;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		CommonLogger.msgLogger("BootReciever onRecieve");
		
		ActivityInfo info = getMetaInfo(context);		
		if(info == null){
			CommonLogger.msgLogger("No Meta data found, existing");
			return;
		}
		
		CommonLogger.msgLogger("Creating Bundle from Meta Data");
		//extract meda-data
		Bundle bundle = info.metaData;

		CommonLogger.msgLogger("Checking bootType");
		String bootType = bundle.getString("bootType");
		
		//If no boot type provided, exit
		if(bootType == null){
			CommonLogger.msgLogger("no bootType found, stopping");
			return;
		}
		CommonLogger.msgLogger("Processing bootType of " + bootType);

		//If bootType of start provided, startup the app
		if(bootType.equalsIgnoreCase(BOOT_TYPE_PROPERTY)){
			bootProperty(context,bundle);
			return;
		}
		
		//If bootType of start provided, startup the app
		if(bootType.equalsIgnoreCase(BOOT_TYPE_START)){
			openBootUp(context, bundle.getBoolean(START_SEND_TO_BACK, false));
			return;
		}
		
		//If the bootType is message, build a notification
		if(bootType.equalsIgnoreCase(BOOT_TYPE_NOTIFY)){
			int msgIcon = bundle.getInt("icon", R.drawable.stat_sys_warning);
			String msgTitle = bundle.getString(NOTIFY_TITLE);
			String msgText = bundle.getString(NOTIFY_MESSAGE); 
			if((msgTitle!=null) && (msgText!=null)){
				notifyOnStart(context,msgTitle,msgText,msgIcon);
			}
			return;
		}		
	}
	private ActivityInfo getMetaInfo(Context context){
		CommonLogger.msgLogger("Getting Meta Data");
		ActivityInfo info = null;
		try {
			info = context.getPackageManager().getReceiverInfo(new ComponentName(context, COMPONENT_NAME), PackageManager.GET_META_DATA);			
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}		
		
		return info;
	}
	private void bootProperty(Context context, Bundle bundle){
		CommonLogger.msgLogger("Starting boot from proerty ");
		if(TiApplication.getInstance() == null){
			CommonLogger.msgLogger("TiApplication not available, stopping");
			return;
		}
		
		if(!bundle.containsKey(PROP_ENABLED) ||
			!bundle.containsKey(PROP_BOOT_TYPE)){
			CommonLogger.msgLogger("no bootType or prop_enabled properties found, stopping");
			return;
		}
		if((bundle.getString(PROP_ENABLED) == null)||
			(bundle.getString(PROP_BOOT_TYPE) == null)){
			CommonLogger.msgLogger("prop_bootType or prop_enabled property is null, stopping");
			return;
		}
		if(!TiApplication.getInstance().getAppProperties().getBool(bundle.getString(PROP_ENABLED),false)){
			return;
		}	
		String actionType = TiApplication.getInstance().getAppProperties().getString(bundle.getString(PROP_BOOT_TYPE),BOOT_TYPE_START);
		if(actionType.equalsIgnoreCase(BOOT_TYPE_START)){
			boolean sendToBack = TiApplication.getInstance().getAppProperties().getBool(bundle.getString(PROP_SEND_TO_BACK),false);
			openBootUp(context,sendToBack);
			return;
		}
		if(actionType.equalsIgnoreCase(BOOT_TYPE_NOTIFY)){
			propertyNotify(context,bundle);
			return;
		}		
	}
	private void openBootUp(Context context, boolean sendToBack){
		bootStartup(context);
		//Check if the app should immediately be sent to the background
		if(sendToBack){
			sendToBackground();
		}
	}
	private void propertyNotify(Context context, Bundle bundle){		
		int msgIcon = TiApplication.getInstance().getAppProperties().getInt(bundle.getString(PROP_ICON), R.drawable.stat_sys_warning);
		String msgTitle = TiApplication.getInstance().getAppProperties().getString(bundle.getString(PROP_TITLE), null);	
		String msgText = TiApplication.getInstance().getAppProperties().getString(bundle.getString(PROP_MESSAGE), null);
		if((msgTitle!=null) && (msgText!=null)){
			notifyOnStart(context,msgTitle,msgText,msgIcon);
		}	
	}
	private void bootStartup(Context context){
		if(TiApplication.getInstance() == null){
			Intent standardIntent = context.getPackageManager().getLaunchIntentForPackage(context.getApplicationContext().getPackageName());
			context.startActivity(standardIntent); 		
		}else{
			Intent tiIntent = TiApplication.getInstance().getPackageManager().getLaunchIntentForPackage(TiApplication.getInstance().getApplicationContext().getPackageName());
			tiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			TiApplication.getInstance().startActivity(tiIntent);						
		}		
	}
	
	private void sendToBackground(){
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		TiApplication.getInstance().startActivity(startMain);		
	}
	
	private void notifyOnStart(Context context,String msgTitle,String msgText, int msgIcon){
		
		if(TiApplication.getInstance() == null){
			CommonLogger.msgLogger("TiApplication not available, stopping");
			return;
		}
		
		NotificationManager notificationManager =(NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
	    Notification notifyDetails = new Notification(msgIcon,msgTitle,System.currentTimeMillis());
	    
	    Intent noti = TiApplication.getInstance().getPackageManager().getLaunchIntentForPackage(TiApplication.getInstance().getPackageName());
	    PendingIntent myIntent = PendingIntent.getActivity(context, 0, noti, 0);
	    
	    notifyDetails.setLatestEventInfo(context, msgTitle,msgText, myIntent);
	    notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
	    notifyDetails.defaults=Notification.DEFAULT_ALL;
	    
	    notificationManager.notify(APP_ID, notifyDetails);		
	}
}
