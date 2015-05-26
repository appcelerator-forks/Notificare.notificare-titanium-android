package ti.notificare;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiBaseActivity;
import org.appcelerator.titanium.TiLifecycle.OnActivityResultEvent;
import org.appcelerator.titanium.TiLifecycle.OnInstanceStateEvent;

import re.notifica.Notificare;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


@Kroll.proxy(creatableInModule=NotificareTitaniumAndroidModule.class)
public class ActivityWorkerProxy extends KrollProxy implements OnActivityResultEvent, OnInstanceStateEvent
{
	private static final String TAG = "NotificareProxy";
	
	private NotificareTitaniumAndroidModule module;

	// Constructor
	public ActivityWorkerProxy()
	{
		super();
		module = NotificareTitaniumAndroidModule.getModule();
	}

	// Handle creation options
	@Override
	public void handleCreationDict(KrollDict options)
	{
		super.handleCreationDict(options);
	}
	
	@Override
	public void initActivity(Activity activity) {}
	
	@Override
	public void onCreate(Activity activity, Bundle savedInstanceState) {
		Log.d(TAG, "onCreate called for notificare proxy");
		((TiBaseActivity) activity).addOnInstanceStateEventListener(this);
		((TiBaseActivity) activity).addOnActivityResultListener(this);
	}
	
	@Override
	public void onResume(Activity activity){
		Log.d(TAG, "Notificare proxy onResume");
		Notificare.shared().setForeground(true);
		Notificare.shared().getEventLogger().logStartSession();
	}
	
	@Override
	public void onPause(Activity activity) {
		Log.d(TAG, "Notificare proxy onPause");
		Notificare.shared().setForeground(false);
		Notificare.shared().getEventLogger().logEndSession();
	}

	@Override
	public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
		Log.d(TAG, "Notificare proxy onActivityResult");
	}

	@Override
	public void onDestroy(Activity activity) {
		Log.d(TAG, "Notificare proxy onDestroy");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		Log.d(TAG, "Notificare proxy onSaveInstanceState");
	}

	@Override
	public void onRestoreInstanceState(Bundle inState) {
		
	}
}	