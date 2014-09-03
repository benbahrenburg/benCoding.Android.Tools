package bencoding.android.services;

import android.content.Context;
import android.content.Intent;
import bencoding.android.Common;

import org.appcelerator.titanium.TiApplication;

public class MainMenuService extends WakefulIntentService{
	private Context _context = null;
	public static final String ACTIVITY_CONTEXT_PARAM = "ACTIVITYCONTEXT";
	
	public MainMenuService() {
		super("MainMenuService");
	}
	
	@Override
	protected void onHandleIntent(Intent intent)
	{
		// Make the context whatever the current TiApplication is.
		if (TiApplication.getInstance() != null)
			_context = TiApplication.getInstance();

		if (_context == null)
		{
			Common.msgLogger("No context. Returning");
			return;
		}
		
		Common.msgLogger(".onHandleIntent()");
		Common.msgLogger(".onHandleIntent().  About to start activity.");
		Intent startMain = new Intent(Intent.ACTION_MAIN);
		startMain.addCategory(Intent.CATEGORY_HOME);
		startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		_context.startActivity(startMain);
		Common.msgLogger(".onHandleIntent().  Call super.onHandleIntent().");
		super.onHandleIntent(intent);
	}
}
