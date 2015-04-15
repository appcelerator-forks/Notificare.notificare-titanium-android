// The contents of this file will be executed before any of
// your view controllers are ever executed, including the index.
// You have access to all functionality on the `Alloy` namespace.
//
// This is a great place to do any initialization for your app
// or create any global variables/functions that you'd like to
// make available throughout your app. You can easily make things
// accessible globally by attaching them to the `Alloy.Globals`
// object. For example:
//
// Alloy.Globals.someGlobalFunction = function(){};


var notificare = require('ti.notificare');

Ti.API.info("module is => " + notificare);

var deviceToken = null;

notificare.addEventListener('ready',function(e){
	
	//For iOS
	if (Ti.Platform.name == "iPhone OS"){
		
		notificare.registerForNotifications(e);

	}

	//For Android
	if (Ti.Platform.name == "android"){
		
		notificare.enableNotifications();
		notificare.enableLocationUpdates();
		notificare.enableBeacons();
		//notificare.enableBilling();
		
	}
});

//Listen for the device registered event
//Only after this event occurs it is safe to call any other method
notificare.addEventListener('registered', function(e){
	if (Ti.Platform.name == "iPhone OS"){
		startLocationUpdates(e);
	}
	 
	 //addTags(['one','two']);
	 //openUserPreferences(e);
	 //openBeacons(e);
	 //removeTag(tag);
});

notificare.addEventListener('registration', function(e){
	
	//notificare.userID = 'testing123';
    //notificare.userName = 'Name here';
	notificare.registerDevice(e.device);

});


notificare.addEventListener('tags', function(e){
	
	if(e && e.tags && e.tags.length > 0){
		e.tags.forEach(function(tag){
			Ti.API.info("Device Tag: " + tag);
		});
	}
	
	 
});

//Fired when a transaction changes state
notificare.addEventListener('location', function(e){
	 Ti.API.info("User location changed " + e.latitude + e.longitude);
});

//Fired when a transaction changes state
notificare.addEventListener('transaction', function(e){
	 Ti.API.info(e.message + e.transaction);
});

//Only available for iOS. This is fired whenever a product's downloadable content is finished.
notificare.addEventListener('download', function(e){
	 Ti.API.info(e.message + e.download);
});

//Fired when the store is ready
notificare.addEventListener('store', function(e){
	if(e && e.products && e.products.length > 0){
		 e.products.forEach(function(product){
			Ti.API.info("Product: " + product.identifer + product.name);
		});
	}
	 //After this trigger is it safe to buy products
	 // use Notificare.buyProduct(product.identifier);
	 // To buy products
	 
});

//Fired whenever there's errors
notificare.addEventListener('errors', function(e){
	 Ti.API.info("There was an error " + e.error);
	 Ti.API.info("with message " + e.message);
});

//Fired whenever app is in foreground and in range of any of the beacons inserted in the current region
notificare.addEventListener('range', function(e){
	if(e && e.beacons && e.beacons.length > 0){
		e.beacons.forEach(function(beacon){
			Ti.API.info("Beacon: " + beacon.uuid + beacon.proximity);
		});
	}
});

//Start location updates in iOS
function startLocationUpdates(e) {
    notificare.startLocationUpdates(e);
}

//Add tags
function addTags(e) {
    notificare.addTags(e);
}

//Remove tag
function removeTag(e) {
    notificare.removeTag(e);
}

//Open Beacons View (Only in iOS)
function openBeacons(e) {
    notificare.openBeacons(e);
}
//Open User Preferences (Only in iOS)
function openUserPreferences(e) {
    notificare.openUserPreferences(e);
}