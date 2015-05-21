/**
 * This file was auto-generated by the Titanium Module SDK helper for Android
 * Appcelerator Titanium Mobile
 * Copyright (c) 2009-2010 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the Apache Public License
 * Please see the LICENSE included with this distribution for details.
 *
 */
package ti.notificare;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;

import android.app.Activity;
import re.notifica.Notificare;
import re.notifica.NotificareCallback;
import re.notifica.NotificareError;
import re.notifica.beacon.BeaconRangingListener;
import re.notifica.billing.BillingManager;
import re.notifica.billing.BillingResult;
import re.notifica.billing.Purchase;
import re.notifica.model.NotificareBeacon;
import re.notifica.model.NotificareProduct;
import re.notifica.model.NotificareRegion;
import re.notifica.model.NotificareUser;
import re.notifica.ui.UserPreferencesActivity;
import ti.notificare.IntentReceiver;


@Kroll.module(name="NotificareTitaniumAndroid", id="ti.notificare")
public class NotificareTitaniumAndroidModule extends KrollModule implements Notificare.OnBillingReadyListener, BillingManager.OnRefreshFinishedListener, BillingManager.OnPurchaseFinishedListener
{

	// Standard Debugging variables
	private static final String LCAT = "NotificareTitaniumAndroidModule";
	private static final boolean DBG = TiConfig.LOGD;
	
	public String userID;
	public String userName;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;

	public NotificareTitaniumAndroidModule()
	{
		super(LCAT);
	}
	
	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(LCAT, "inside onAppCreate");
		Notificare.shared().launch(app);
		Notificare.shared().setIntentReceiver(IntentReceiver.class);
		
		
	}

	
	@Override
	public void onStart(Activity activity) 
	{
		Log.d(LCAT, "[MODULE LIFECYCLE EVENT] start");
		
		super.onStart(activity);
	}
	
	@Override
	public void onStop(Activity activity) 
	{
		// This method is called when the root context is stopped 
		
		Log.d(LCAT, "[MODULE LIFECYCLE EVENT] stop");
		
		super.onStop(activity);
	}
	
	@Override
	public void onPause(Activity activity) 
	{
		// This method is called when the root context is being suspended
		super.onPause(activity);		
		Log.d(LCAT, "[MODULE LIFECYCLE EVENT] pause");
		Notificare.shared().setForeground(false);
	}
	
	@Override
	public void onResume(Activity activity) 
	{		
		// This method is called when the root context is being resumed
		super.onResume(activity);		
		Log.d(LCAT, "[MODULE LIFECYCLE EVENT] resume");	
		Notificare.shared().setForeground(true);
	}
	
	@Override
	public void onDestroy(Activity activity) 
	{
		// This method is called when the root context is being destroyed
		super.onDestroy(activity);		
		Log.d(LCAT, "[MODULE LIFECYCLE EVENT] destroy");
	}
	
	
	// Methods
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
				
				NotificareTitaniumAndroidModule nModule = getModule();
				
				if(nModule != null){
					HashMap<String, Object> event = new HashMap<String, Object>();
				    event.put("device", result);
				    nModule.fireEvent("registered", event);
				}
				
		    	
				Notificare.shared().fetchDeviceTags(new NotificareCallback<List<String>>() {

					@Override
					public void onError(NotificareError error) {
						Log.e(LCAT, error.getMessage());
					}

					@Override
					public void onSuccess(List<String> tags) {

						NotificareTitaniumAndroidModule nModule = getModule();
						
						if (nModule != null) {
							HashMap<String, Object> event = new HashMap<String, Object>();
						    event.put("tags", tags.toArray(new Object[tags.size()]));
					    	nModule.fireEvent("tags", event);
						}

					}
					
				});
			}

			@Override
			public void onError(NotificareError error) {
				Log.e(LCAT, "Error registering device", error);
			}
        	
        });
		
	}
	
	@Kroll.method
	public void addTags(@Kroll.argument(optional=false, name="tags") String[] tags)
	{
		Log.i(LCAT, "Length: " + tags.length);
		ArrayList<String> tagsList = new ArrayList<String>(tags.length);
		for (String tag: tags) {
			tagsList.add(tag);
		}
		
		Notificare.shared().addDeviceTags(tagsList, new NotificareCallback<Boolean>(){

			@Override
			public void onError(NotificareError error) {
				Log.e(LCAT, error.getMessage(), error);
			}

			@Override
			public void onSuccess(Boolean success) {
				
				Notificare.shared().fetchDeviceTags(new NotificareCallback<List<String>>() {

					@Override
					public void onError(NotificareError error) {
						Log.e(LCAT, error.getMessage(), error);
					}

					@Override
					public void onSuccess(List<String> tags) {

						NotificareTitaniumAndroidModule nModule = getModule();
						
						if (nModule != null) {
							HashMap<String, Object> event = new HashMap<String, Object>();
						    event.put("tags", tags.toArray(new Object[tags.size()]));
						    nModule.fireEvent("tags", event);
						}
						
						 
					}
					
				});
				
			}

		});
	}
	
	@Kroll.method
	public void removeTag(String tag)
	{
		Notificare.shared().removeDeviceTag(tag, new NotificareCallback<Boolean>(){

			@Override
			public void onError(NotificareError error) {
				Log.e(LCAT, error.getMessage(), error);
			}

			@Override
			public void onSuccess(Boolean success) {
				
				Notificare.shared().fetchDeviceTags(new NotificareCallback<List<String>>() {

					@Override
					public void onError(NotificareError error) {
						Log.e(LCAT, error.getMessage(), error);		
					}

					@Override
					public void onSuccess(List<String> tags) {

						NotificareTitaniumAndroidModule nModule = getModule();
						
						if(nModule != null){
							HashMap<String, Object> event = new HashMap<String, Object>();
						    event.put("tags", tags.toArray(new Object[tags.size()]));
						    nModule.fireEvent("tags", event);
						}
						
						 
					}
					
				});
				
			}
			
		});
	}
	
	
	@Kroll.method
	public void clearTags()
	{
		Notificare.shared().clearDeviceTags(new NotificareCallback<Boolean>(){

			@Override
			public void onError(NotificareError error) {
				Log.e(LCAT, error.getMessage(), error);
			}

			@Override
			public void onSuccess(Boolean success) {
				
				Notificare.shared().fetchDeviceTags(new NotificareCallback<List<String>>() {

					@Override
					public void onError(NotificareError error) {
						Log.e(LCAT, error.getMessage(), error);
					}

					@Override
					public void onSuccess(List<String> tags) {

						NotificareTitaniumAndroidModule nModule = getModule();
						
						if (nModule != null) {
							HashMap<String, Object> event = new HashMap<String, Object>();
						    event.put("tags", tags.toArray(new Object[tags.size()]));
						    nModule.fireEvent("tags", event);
						}
						
						 
					}
					
				});
				
			}
			
		});
	}
	
	public static NotificareTitaniumAndroidModule getModule() {
		TiApplication appContext = TiApplication.getInstance();
		NotificareTitaniumAndroidModule nModule = (NotificareTitaniumAndroidModule)appContext.getModuleByName("NotificareTitaniumAndroidModule");
	
		if (nModule == null) {
			Log.w(LCAT,"Notificare module not currently loaded");
		}
		return nModule;
	}
	
	// Properties
	@Kroll.getProperty
	public String userID()
	{
		return this.userID;
	}
	
	public String userName()
	{
		return this.userName;
	}

	
	public void getTags(){
		Notificare.shared().fetchDeviceTags(new NotificareCallback<List<String>>() {

			@Override
			public void onError(NotificareError error) {
				Log.e(LCAT, error.getMessage(), error);
			}

			@Override
			public void onSuccess(List<String> tags) {

				NotificareTitaniumAndroidModule nModule = getModule();
				
				if (nModule != null) {
					HashMap<String, Object> event = new HashMap<String, Object>();
				    event.put("tags", tags.toArray(new Object[tags.size()]));
				    nModule.fireEvent("tags", event);
				}
				
				 
			}
			
		});
	}

	@Override
	public void onPurchaseFinished(BillingResult result, Purchase purchase) {
		
		NotificareTitaniumAndroidModule nModule = getModule();
		
		if (nModule != null) {
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("transaction", purchase);
	    	nModule.fireEvent("transaction", event);
		}	
		
	}

	@Override
	public void onRefreshFailed(NotificareError error) {
		NotificareTitaniumAndroidModule nModule = getModule();
		
		if (nModule != null) {
			HashMap<String, Object> event = new HashMap<String, Object>();
		    event.put("error", error);
		    nModule.fireEvent("errors", event);
		}

		
	}

	@Override
	public void onRefreshFinished() {
		// TODO Auto-generated method stub
		
	}
	
	
	
	@Override
	public void onBillingReady() {
		NotificareTitaniumAndroidModule nModule = getModule();
		
		if (nModule != null) {
			nModule.fireEvent("store", new KrollDict());
		}
		
	}

}

