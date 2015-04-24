package ti.notificare;

import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.titanium.TiApplication;

import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import re.notifica.Notificare;
import re.notifica.NotificareCallback;
import re.notifica.NotificareError;
import re.notifica.model.NotificareNotification;
import re.notifica.push.gcm.DefaultIntentReceiver;

public class IntentReceiver extends DefaultIntentReceiver {

	public static final String PREFS_NAME = "AppPrefsFile";
	private static final String TAG = IntentReceiver.class.getSimpleName();

	@Override
	public void onNotificationReceived(String alert, String notificationId,
			Bundle extras) {
		// Execute default behavior, i.e., put notification in drawer
		//Log.d(TAG, " received with extra Notification" + extras.getString("mykey"));
		
		HashMap<String, Object> event = new HashMap<String, Object>();
	    event.put("notification", notificationId);
	    event.put("alert", alert);
	    event.put("extras", extras);
	    TiApplication appContext = TiApplication.getInstance();
    	appContext.getModuleByName("NotificareTitaniumAndroidModule").fireEvent("notification", event);
	    
		super.onNotificationReceived(alert, notificationId, extras);
	}

	@Override
	public void onNotificationOpened(String alert, String notificationId,
			Bundle extras) {
		// Notification is in extras
		//NotificareNotification notification = extras.getParcelable(Notificare.INTENT_EXTRA_NOTIFICATION);
		//Log.d(TAG, "Notification was opened with type " + notification.getType());
		//Log.d(TAG, "Notification was opened with extra " + notification.getExtra().get("mykey"));
		// By default, open the NotificationActivity and let it handle the Notification
		super.onNotificationOpened(alert, notificationId, extras);
	}

    @Override
    public void onReady() {

    	TiApplication appContext = TiApplication.getInstance();
    	appContext.getModuleByName("NotificareTitaniumAndroidModule").fireEvent("ready", new KrollDict());

    }

    @Override
	public void onRegistrationFinished(String deviceId) {
		//Log.d(TAG, "Device was registered with GCM as device " + deviceId);
		// Register as a device for a test userID
		
		TiApplication appContext = TiApplication.getInstance();
		HashMap<String, Object> event = new HashMap<String, Object>();
	    event.put("device", deviceId);
    	appContext.getModuleByName("NotificareTitaniumAndroidModule").fireEvent("registration", event);

	}

	@Override
	public void onActionReceived(Uri target) {
		//Log.d(TAG, "Custom action was received: " + target.toString());
		// By default, pass the target as data URI to your main activity in a launch intent
		super.onActionReceived(target);
	}
	
	@Override
	public void onLocationUpdateReceived(Location location){

		TiApplication appContext = TiApplication.getInstance();
		HashMap<String, Object> event = new HashMap<String, Object>();
	    event.put("latitude", location.getLatitude());
	    event.put("longitude", location.getLongitude());
    	appContext.getModuleByName("NotificareTitaniumAndroidModule").fireEvent("location", event);
		super.onLocationUpdateReceived(location);
		
	}
	
	
}
