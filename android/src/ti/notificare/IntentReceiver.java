/**
 * Notificare module for Appcelerator Titanium Mobile
 * IntentReceiver
 * @author Joel Oliveira <joel@notifica.re>
 * @copyright 2013 - 2015 Notificare B.V.
 * Please see the LICENSE included with this distribution for details.
 */
package ti.notificare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;

import re.notifica.model.NotificareBeacon;
import re.notifica.push.gcm.DefaultIntentReceiver;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;

/**
 * Intent receiver for Notificare intents
 */
public class IntentReceiver extends DefaultIntentReceiver {

	public static final String PREFS_NAME = "AppPrefsFile";
	//private static final String TAG = IntentReceiver.class.getSimpleName();
	
	@Override
	public void onNotificationReceived(String alert, String notificationId, Bundle extras) {
		// Execute default behavior, i.e., put notification in drawer
		//Log.d(TAG, " received with extra Notification" + extras.getString("mykey"));
		
		HashMap<String, Object> event = new HashMap<String, Object>();
	    event.put("notification", notificationId);
	    event.put("alert", alert);
	    event.put("extras", extras);
	    
	    NotificareTitaniumAndroidModule module = NotificareTitaniumAndroidModule.getModule();
		
		if (module != null) {
			module.fireEvent("notification", event);
		}

		super.onNotificationReceived(alert, notificationId, extras);
	}

	@Override
	public void onNotificationOpened(String alert, String notificationId, Bundle extras) {
		// Notification is in extras
		//NotificareNotification notification = extras.getParcelable(Notificare.INTENT_EXTRA_NOTIFICATION);
		//Log.d(TAG, "Notification was opened with type " + notification.getType());
		//Log.d(TAG, "Notification was opened with extra " + notification.getExtra().get("mykey"));
		// By default, open the NotificationActivity and let it handle the Notification
		super.onNotificationOpened(alert, notificationId, extras);
	}

    @Override
    public void onReady() {
    	
    	NotificareTitaniumAndroidModule module = NotificareTitaniumAndroidModule.getModule();
		
		if (module != null) {
			module.fireEvent("ready", new KrollDict());
		}

    }

    @Override
	public void onRegistrationFinished(String deviceId) {
		//Log.d(TAG, "Device was registered with GCM as device " + deviceId);
		// Register as a device for a test userID
		
    	NotificareTitaniumAndroidModule module = NotificareTitaniumAndroidModule.getModule();
		
		if (module != null) {
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("device", deviceId);
		    module.fireEvent("registration", event);
		}

	}

	@Override
	public void onActionReceived(Uri target) {
		//Log.d(TAG, "Custom action was received: " + target.toString());
		// By default, pass the target as data URI to your main activity in a launch intent
		super.onActionReceived(target);
	}
	
	@Override
	public void onRangingBeacons(List<NotificareBeacon> arg0) {
		
		NotificareTitaniumAndroidModule module = NotificareTitaniumAndroidModule.getModule();
		
		if (module != null) {
			List<Object> beacons = new ArrayList<Object>();

			for (NotificareBeacon beacon : arg0) {
				HashMap<String, String> b = new HashMap<String, String>();
                b.put("id", beacon.getBeaconId());
                b.put("name", beacon.getName());
                b.put("notification", beacon.getNotificationId());
                b.put("proximity", beacon.getProximity().toString());
                b.put("purpose", beacon.getPurpose());
                b.put("region", beacon.getRegionId());
                b.put("major", Integer.toString(beacon.getMajor()));
                b.put("minor", Integer.toString(beacon.getMinor()));
                beacons.add(b);
            }
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("beacons", beacons.toArray(new Object[beacons.size()]));
		    module.fireEvent("range", event);
		}
		
		super.onRangingBeacons(arg0);
	}
	
	@Override
	public void onLocationUpdateReceived(Location location){

		NotificareTitaniumAndroidModule module = NotificareTitaniumAndroidModule.getModule();
		
		if (module != null) {
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("latitude", location.getLatitude());
		    event.put("longitude", location.getLongitude());
		    module.fireEvent("location", event);
		}
		
		super.onLocationUpdateReceived(location);
		
	}
	
	
}
