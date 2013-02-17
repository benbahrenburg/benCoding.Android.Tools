package bencoding.android.tools;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.titanium.proxy.IntentProxy;

import android.content.Intent;
import android.content.pm.PackageManager;

@Kroll.proxy(creatableInModule=AndroidtoolsModule.class)
public class PlatformProxy  extends KrollProxy {
	public PlatformProxy()
	{
		super();		
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
}
