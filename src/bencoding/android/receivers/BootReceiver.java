package bencoding.android.receivers;

import java.util.Calendar;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiApplication;

import bencoding.android.Common;
import bencoding.android.services.MainMenuService;
import bencoding.android.services.WakefulIntentService;
import bencoding.android.tools.AndroidtoolsModule;
import android.R;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


public class BootReceiver  extends BroadcastReceiver{
	private final static String COMPONENT_NAME = "bencoding.android.receivers.BootReceiver";
	private final static String BOOT_TYPE_START ="RESTART";
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
			Common.msgLogger("App instance is not null.  fireAppEvent");
			KrollDict event = new KrollDict();
			event.put("type", bootType);
			TiApplication.getInstance().fireAppEvent(BOOT_TYPE, event);
		}
		
		//If bootType of start provided, startup the app
		if(bootType.equalsIgnoreCase(BOOT_TYPE_PROPERTY)){
			Common.msgLogger("bootType is PROPERTYBASED.  Startup and return.");
			bootProperty(context,bundle);
			return;
		}
		
		//If bootType of start provided, startup the app
		if(bootType.equalsIgnoreCase(BOOT_TYPE_START)){
			Common.msgLogger("bootType is RESTART.  Call openBootUp.  Send to back? " + bundle.getBoolean(START_SEND_TO_BACK, false));
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
	private void openBootUp(Context context, boolean sendToBack){

		Common.msgLogger("Called openBootUp");
		bootStartup(context);
		//Check if the app should immediately be sent to the background
		if(sendToBack){
			sendToBackground(TiApplication.getInstance());
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
			standardIntent.putExtra(AndroidtoolsModule.LAUNCHED_FROM_BOOTRECEIVER, true);
			context.startActivity(standardIntent); 		
		}else{
			Intent tiIntent = TiApplication.getInstance().getPackageManager().getLaunchIntentForPackage(TiApplication.getInstance().getApplicationContext().getPackageName());
			tiIntent.putExtra(AndroidtoolsModule.LAUNCHED_FROM_BOOTRECEIVER, true);
			tiIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			TiApplication.getInstance().startActivity(tiIntent);						
		}		
	}
	
	private void sendToBackground(Context context){
		Common.msgLogger(".sendToBackground()");
		WakefulIntentService.acquireStaticLock(context);
		Common.msgLogger(".sendToBackground():  Static wake lock acquired");
		Intent svcIntent = new Intent(context, MainMenuService.class);
		
		context.startService(svcIntent);
		Common.msgLogger(".sendToBackground():  Service Started");
	}
	
	private void notifyOnStart(Context context,String msgTitle,String msgText, int msgIcon){
		
		if(TiApplication.getInstance() == null){
			Common.msgLogger("TiApplication not available, stopping");
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
