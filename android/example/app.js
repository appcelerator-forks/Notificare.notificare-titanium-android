/**
 * Example app for Titanium
 */

var notificare = require('ti.notificare');

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


// this sets the background color of the master UIView (when there are no windows/tab groups on it)
Titanium.UI.setBackgroundColor('#000');

// create tab group
var tabGroup = Titanium.UI.createTabGroup();

// create base UI tab and root window

var win1 = Titanium.UI.createWindow({  
    title:'Tab 1',
    backgroundColor:'#fff'
});
var tab1 = Titanium.UI.createTab({  
    icon:'KS_nav_views.png',
    title:'Tab 1',
    window:win1
});

var label1 = Titanium.UI.createLabel({
	color:'#999',
	text:'I am Window 1',
	font:{fontSize:20,fontFamily:'Helvetica Neue'},
	textAlign:'center',
	width:'auto'
});

win1.add(label1);

//
// create controls tab and root window
//
var win2 = Titanium.UI.createWindow({  
    title:'Tab 2',
    backgroundColor:'#fff'
});
var tab2 = Titanium.UI.createTab({  
    icon:'KS_nav_ui.png',
    title:'Tab 2',
    window:win2
});

var label2 = Titanium.UI.createLabel({
	color:'#999',
	text:'I am Window 2',
	font:{fontSize:20,fontFamily:'Helvetica Neue'},
	textAlign:'center',
	width:'auto'
});

win2.add(label2);



//
//  add tabs
//
tabGroup.addTab(tab1);  
tabGroup.addTab(tab2);  

tabGroup.notificareProxy = notificare.createActivityWorker({lifecycleContainer: tabGroup});

// open tab group
tabGroup.open();
