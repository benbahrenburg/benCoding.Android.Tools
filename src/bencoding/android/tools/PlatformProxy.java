package bencoding.android.tools;

import java.util.ArrayList;
import java.util.HashMap;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.IntentProxy;

import com.sun.tools.javac.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings;

@Kroll.proxy(creatableInModule=AndroidtoolsModule.class)
public class PlatformProxy  extends KrollProxy {
	public PlatformProxy()
	{
		super();		
	}
	

	@Kroll.method
	public boolean isAirplaneModeOn() {
		   return Settings.System.getInt(TiApplication.getInstance().getApplicationContext().getContentResolver(),
		           Settings.System.AIRPLANE_MODE_ON, 0) != 0;
	}
	
	@Kroll.method
	public boolean intentAvailable(IntentProxy intent) {
		if(intent == null){
			return false;
		}
			
		PackageManager packageManager = TiApplication.getInstance().getPackageManager();
		return (packageManager.queryIntentActivities(intent.getIntent(), PackageManager.MATCH_DEFAULT_ONLY).size() > 0) ;		
	}
	
	@Kroll.method
	public void restartApp()
	{
		Log.d(AndroidtoolsModule.MODULE_FULL_NAME, "Restarting app");
		Intent i = TiApplication.getInstance().getApplicationContext().getPackageManager()
	             	.getLaunchIntentForPackage( TiApplication.getInstance().getApplicationContext().getPackageName() );
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		TiApplication.getInstance().getApplicationContext().startActivity(i);		
	}
	
	@Kroll.method
	public void exitApp(){
		android.os.Process.killProcess(android.os.Process.myPid());
	}
	
	@Kroll.method
	public void killPackage(String packageName){
		ActivityManager activityManager = (ActivityManager)TiApplication.getInstance().getApplicationContext().getSystemService(TiApplication.ACTIVITY_SERVICE);
	    activityManager.killBackgroundProcesses(packageName);
	}
	
	@Kroll.method
	public void killProcess(int pid){
		android.os.Process.killProcess(pid);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Kroll.method
	public Object[] getRunningAppProcesses(){
	    ArrayList appList = new ArrayList();	    
		PackageManager pm = TiApplication.getInstance().getApplicationContext().getPackageManager();
		ActivityManager activityManager = (ActivityManager) TiApplication.getInstance().getApplicationContext().getSystemService( TiApplication.ACTIVITY_SERVICE );
	    List<RunningAppProcessInfo> procInfos = (List<RunningAppProcessInfo>) activityManager.getRunningAppProcesses();
	    for(int i = 0; i < procInfos.size(); i++){
	    	HashMap<String, Object> record = new HashMap<String, Object>(6);
	    	record.put("processName",procInfos.get(i).processName);
	    	record.put("pid", procInfos.get(i).pid);
	    	record.put("uid", procInfos.get(i).uid);
	    	
			try {
				ApplicationInfo info = pm.getApplicationInfo(procInfos.get(i).processName, PackageManager.GET_META_DATA);
				record.put("packageName", info.packageName);
				record.put("name", info.name);
				record.put("label", info.loadLabel(pm).toString());
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			appList.add(record);
	    }
	    
	    Object[] returnObject = appList.toArray(new Object[appList.size()]);
	    
	    return returnObject;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Kroll.method
	public Object[] getInstalledApps(){
		 ArrayList appList = new ArrayList();	
		final PackageManager pm = TiApplication.getInstance().getApplicationContext().getPackageManager();
		//get a list of installed apps.
		List<ApplicationInfo> packages = (List<ApplicationInfo>) pm.getInstalledApplications(PackageManager.GET_META_DATA);

		for (ApplicationInfo packageInfo : packages) {

	    	HashMap<String, Object> record = new HashMap<String, Object>(5);
	    	record.put("processName",packageInfo.processName);
	    	record.put("name", packageInfo.name);
	    	record.put("packageName", packageInfo.packageName);
	    	record.put("label", packageInfo.loadLabel(pm).toString());
	    	record.put("uid", packageInfo.uid);
			appList.add(record);		
		}		
	    Object[] returnObject = appList.toArray(new Object[appList.size()]);
	    
	    return returnObject;		
	}
}