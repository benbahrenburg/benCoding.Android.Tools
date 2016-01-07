package bencoding.android.receivers;

import java.util.Calendar;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiApplication;

import bencoding.android.Common;
import android.R;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;


public class BootReceiver  extends BroadcastReceiver{
	private final static String COMPONENT_NAME = "bencoding.android.receivers.BootReceiver";
	private final static String BOOT_TYPE_START ="RESTART";
	private final static String START_SEND_TO_BACK="sendToBack";
	
	private final static String BOOT_TYPE_NOTIFY ="NOTIFY";
	private final static String NOTIFY_TITLE ="title";
	private final static String NOTIFY_MESSAGE = "message";
	
	private final static String BOOT_TYPE_PROPERTY ="PROPERTYBASED";
	
	/* service handling */
	private final static String BOOT_TYPE_SERVICE = "SERVICE";
	private final static String SERVICE_NAME = "serviceName";
	private final static String SERVICE_INTERVAL = "serviceInterval";
	
	private final static String PROP_ENABLED ="enabled_property_to_reference";
	private final static String PROP_BOOT_TYPE = "bootType_property_to_reference";
	private final static String PROP_SEND_TO_BACK = "sendToBack_property_to_reference";
	private final static String PROP_ICON = "icon_property_to_reference";
	private final static String PROP_TITLE = "title_property_to_reference";
	private final static String PROP_MESSAGE= "message_property_to_reference";
	private final static int APP_ID = 1234;
	private final static String BOOT_TYPE ="BOOT_TYPE";
	private final static String START_TIME_RECORD_PROPERTY="record_start_property";
	@Override
	public void onReceive(Context context, Intent intent) {

		Common.msgLogger("BootReciever onRecieve");
		
		ActivityInfo info = Common.getMetaInfo(context,COMPONENT_NAME);		
		if(info == null){
			Common.msgLogger("No Meta data found, existing");
			return;
		}
		
		Common.msgLogger("Creating Bundle from Meta Data");
		//extract meda-data
		Bundle bundle = info.metaData;

		Common.msgLogger("Checking bootType");
		String bootType = bundle.getString("bootType");
		
		//If no boot type provided, exit
		if(bootType == null){
			Common.msgLogger("no bootType found, stopping");
			return;
		}
		Common.msgLogger("Processing bootType of " + bootType);

		if(TiApplication.getInstance()!=null){
			KrollDict event = new KrollDict();
			event.put("type", bootType);
			TiApplication.getInstance().fireAppEvent(BOOT_TYPE, event);
		}
		
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
		
		if (bootType.equalsIgnoreCase(BOOT_TYPE_SERVICE)){
			String service = bundle.getString(SERVICE_NAME);
			int interval = bundle.getInt(SERVICE_INTERVAL);
			Common.msgLogger("running service: "+ service+" interval: "+interval);
			bootService(context,service,interval);
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
			writeStartDate(bundle);
		}		
	}

	private void writeStartDate(Bundle bundle){
		if(bundle.containsKey(START_TIME_RECORD_PROPERTY)){
			String recordTimeProperty = bundle.getString(START_TIME_RECORD_PROPERTY);
			if(recordTimeProperty!=null){
				TiApplication.getInstance().getAppProperties().setDouble(recordTimeProperty, Calendar.getInstance().getTimeInMillis());
			}
		}		
	}
	private void bootProperty(Context context, Bundle bundle){
		Common.msgLogger("Starting boot from proerty ");
		if(TiApplication.getInstance() == null){
			Common.msgLogger("TiApplication not available, stopping");
			return;
		}
		
		if(!bundle.containsKey(PROP_ENABLED) ||
			!bundle.containsKey(PROP_BOOT_TYPE)){
			Common.msgLogger("no bootType or prop_enabled properties found, stopping");
			return;
		}
		if((bundle.getString(PROP_ENABLED) == null)||
			(bundle.getString(PROP_BOOT_TYPE) == null)){
			Common.msgLogger("prop_bootType or prop_enabled property is null, stopping");
			return;
		}
		if(!TiApplication.getInstance().getAppProperties().getBool(bundle.getString(PROP_ENABLED),false)){
			return;
		}	
		String actionType = TiApplication.getInstance().getAppProperties().getString(bundle.getString(PROP_BOOT_TYPE),BOOT_TYPE_START);
		if(actionType.equalsIgnoreCase(BOOT_TYPE_START)){
			boolean sendToBack = TiApplication.getInstance().getAppProperties().getBool(bundle.getString(PROP_SEND_TO_BACK),false);
			openBootUp(context,sendToBack);
		}
		if(actionType.equalsIgnoreCase(BOOT_TYPE_NOTIFY)){
			propertyNotify(context,bundle);
		}				
		writeStartDate(bundle);
	}
	
	private void bootService(Context context, String bootService, int interval){
		try{
			if(bootService.startsWith(".")){
				bootService = context.getApplicationContext().getPackageName() + bootService;
			}
			Intent serviceIntent = new Intent(context,Class.forName(bootService));
	        	serviceIntent.putExtra("interval", interval*1000L); // 10 secs
	        	Common.msgLogger("Starting service "+bootService +" interval "+interval);
	        	context.startService(serviceIntent);
		} catch (Exception e) {
			Common.msgLogger(e.toString());
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
			Common.msgLogger("TiApplication not available, stopping");
			return;
		}
	
		// Instantiate a Builder object.
		NotificationCompat.Builder builder = new NotificationCompat.Builder(TiApplication.getInstance());
		// Creates an Intent for the Activity
		Intent notifyIntent = TiApplication.getInstance().getPackageManager().getLaunchIntentForPackage(TiApplication.getInstance().getPackageName());
		PendingIntent notifyPendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0);
		
		// Puts the PendingIntent into the notification builder
		builder.setContentIntent(notifyPendingIntent);
		// Notifications are issued by sending them to the
		// NotificationManager system service.
		NotificationManager mNotificationManager =
		    (NotificationManager) TiApplication.getInstance().getSystemService(Context.NOTIFICATION_SERVICE);
		// Builds an anonymous Notification object from the builder, and
		// passes it to the NotificationManager
		mNotificationManager.notify(APP_ID, builder.build());	
	}
}
