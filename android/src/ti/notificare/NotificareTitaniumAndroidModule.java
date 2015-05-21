/**
 * Notificare module for Appcelerator Titanium Mobile
 * @author Joel Oliveira <joel@notifica.re>
 * @copyright 2013 - 2015 Notificare B.V.
 * Please see the LICENSE included with this distribution for details.
 */
package ti.notificare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.TiApplication;

import re.notifica.Notificare;
import re.notifica.NotificareCallback;
import re.notifica.NotificareError;
import re.notifica.billing.BillingManager;
import re.notifica.billing.BillingResult;
import re.notifica.billing.Purchase;
import android.app.Activity;


@Kroll.module(name="NotificareTitaniumAndroid", id="ti.notificare")
public class NotificareTitaniumAndroidModule extends KrollModule implements Notificare.OnBillingReadyListener, BillingManager.OnRefreshFinishedListener, BillingManager.OnPurchaseFinishedListener
{

	// Standard Debugging variables
	private static final String TAG = "NotificareTitaniumAndroidModule";
	
	public String userID;
	public String userName;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public NotificareTitaniumAndroidModule()
	{
		super(TAG);
	}
	
	/*
	 * Helper methods
	 */

	/**
	 * Try and fetch the loaded module form the current running context
	 * @return
	 */
	public static NotificareTitaniumAndroidModule getModule() {
		TiApplication appContext = TiApplication.getInstance();
		NotificareTitaniumAndroidModule module = (NotificareTitaniumAndroidModule)appContext.getModuleByName("NotificareTitaniumAndroidModule");
		if (module == null) {
			Log.w(TAG,"Notificare module not currently loaded");
		}
		return module;
	}
	
	/**
	 * Fetches tags and fires an event when done
	 */
	private void fetchTags() {
		Notificare.shared().fetchDeviceTags(new NotificareCallback<List<String>>() {

			@Override
			public void onError(NotificareError error) {
				Log.e(TAG, "Error fetching tags", error);
			}

			@Override
			public void onSuccess(List<String> tags) {

				NotificareTitaniumAndroidModule module = getModule();
				
				if (module != null) {
					HashMap<String, Object> event = new HashMap<String, Object>();
				    event.put("tags", tags.toArray(new Object[tags.size()]));
				    module.fireEvent("tags", event);
				}
			}

		});
	}
	
	/*
	 *  Properties
	 */
	
	@Kroll.getProperty
	public String userID()
	{
		return this.userID;
	}
	
	public String userName()
	{
		return this.userName;
	}

	

	/*
	 * Overrides
	 */
	
	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(TAG, "[MODULE LIFECYCLE EVENT] app create");
		Notificare.shared().launch(app);
		Notificare.shared().setIntentReceiver(IntentReceiver.class);
	}

	
	@Override
	public void onStart(Activity activity) 
	{
		Log.d(TAG, "[MODULE LIFECYCLE EVENT] start");
		Notificare.shared().setForeground(true);
		super.onStart(activity);
	}
	
	@Override
	public void onStop(Activity activity) 
	{
		// This method is called when the root context is stopped 
		
		Log.d(TAG, "[MODULE LIFECYCLE EVENT] stop");
		Notificare.shared().setForeground(true);
		super.onStop(activity);
	}
	
	@Override
	public void onPause(Activity activity) 
	{
		// This method is called when the root context is being suspended
		super.onPause(activity);		
		Log.d(TAG, "[MODULE LIFECYCLE EVENT] pause");
		Notificare.shared().setForeground(false);
		super.onPause(activity);
	}
	
	@Override
	public void onResume(Activity activity) 
	{		
		// This method is called when the root context is being resumed
		super.onResume(activity);		
		Log.d(TAG, "[MODULE LIFECYCLE EVENT] resume");	
		Notificare.shared().setForeground(true);
	}
	
	@Override
	public void onDestroy(Activity activity) 
	{
		// This method is called when the root context is being destroyed
		super.onDestroy(activity);		
		Log.d(TAG, "[MODULE LIFECYCLE EVENT] destroy");
	}
	
	@Override
	public void onPurchaseFinished(BillingResult result, Purchase purchase) {
		NotificareTitaniumAndroidModule module = getModule();
		if (module != null) {
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("transaction", purchase);
		    module.fireEvent("transaction", event);
		}	
	}

	@Override
	public void onRefreshFailed(NotificareError error) {
		NotificareTitaniumAndroidModule module = getModule();
		if (module != null) {
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("error", error);
		    module.fireEvent("errors", event);
		}
	}

	@Override
	public void onRefreshFinished() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public void onBillingReady() {
		NotificareTitaniumAndroidModule module = getModule();
		if (module != null) {
			module.fireEvent("store", new KrollDict());
		}
	}
	
	
	/*
	 * Module Methods
	 */
	
	
	@Kroll.method
	public void enableNotifications()
	{
		Notificare.shared().enableNotifications();
	}

	@Kroll.method
	public void enableLocationUpdates()
	{
		Notificare.shared().enableLocationUpdates();
	}
	
	@Kroll.method
	public void enableBeacons()
	{
		Notificare.shared().enableBeacons();
	}
	
	@Kroll.method
	public void enableBilling()
	{
		Notificare.shared().enableBilling();
	}
	
	@Kroll.method
	public void registerDevice(String deviceId){
		
		Notificare.shared().registerDevice(deviceId, this.userID, this.userName, new NotificareCallback<String>() {

			@Override
			public void onSuccess(String result) {
				
				NotificareTitaniumAndroidModule module = getModule();
				if (module != null) {
					HashMap<String, Object> event = new HashMap<String, Object>();
				    event.put("device", result);
				    module.fireEvent("registered", event);
				}
		    	fetchTags();
			}

			@Override
			public void onError(NotificareError error) {
				Log.e(TAG, "Error registering device", error);
			}
        	
        });
		
	}
	
	@Kroll.method
	public void addTags(@Kroll.argument(optional=false, name="tags") String[] tags)
	{
		ArrayList<String> tagsList = new ArrayList<String>(tags.length);
		for (String tag: tags) {
			tagsList.add(tag);
		}
		
		Notificare.shared().addDeviceTags(tagsList, new NotificareCallback<Boolean>(){

			@Override
			public void onError(NotificareError error) {
				Log.e(TAG, "Error adding tags", error);
			}

			@Override
			public void onSuccess(Boolean success) {
				fetchTags();
			}

		});
	}
	
	@Kroll.method
	public void removeTag(String tag)
	{
		Notificare.shared().removeDeviceTag(tag, new NotificareCallback<Boolean>(){

			@Override
			public void onError(NotificareError error) {
				Log.e(TAG, "Error removing tag", error);
			}

			@Override
			public void onSuccess(Boolean success) {
				fetchTags();
			}
			
		});
	}
	
	
	@Kroll.method
	public void clearTags()
	{
		Notificare.shared().clearDeviceTags(new NotificareCallback<Boolean>(){

			@Override
			public void onError(NotificareError error) {
				Log.e(TAG, "Error clearing tags", error);
			}

			@Override
			public void onSuccess(Boolean success) {
				fetchTags();
			}
			
		});
	}
	
	@Kroll.method
	public void getTags() {
		fetchTags();
	}
	
}