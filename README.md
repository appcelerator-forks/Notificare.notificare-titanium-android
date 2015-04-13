# Notificare Module

## Description

Titanium module for Notificare Mobile Marketing Platform. Before start make sure you grab the DefaultTheme.bundle, Notificare.plist and NotificareTags.plist from https://github.com/Notificare/notificare-push-lib and place it in your app's assets folder. Notificare.plist is the configuration file where you should make changes to match your app's settings.

## Accessing the Notificare Module

To access this module from JavaScript, you would do the following:

	var Notificare = require("ti.notificare");

The Notificare variable is a reference to the Module object.	

## Reference

### Notificare.addEventListener

Notificare will be initialized at launch automatically. But before you can request any of the methods you must wait for the 'ready' event to be fired. Additionally you can also listen to the 'registered' event, triggered in response to Notificare.registerDevice() and the 'range' event that is fired any time iBeacons are in range.

Parameters:

- event (required) {String} A string representing the event to listen to
- callback {Function} A callback ({Object})

### Notificare.userID

Sets the userID to which any device registration will be assigned to. To take effect this must be set before the registerDevice method.

### Notificare.userName

Sets the username to which any device registration will be assigned to. To take effect this must be set before the registerDevice method.

### Notificare.registerDevice

Registers the device with Notificare. It should be used in used from the success function of Ti.Network.registerForPushNotifications.

Parameters:

- token (required) {String}

### Notificare.openNotification

Open an incoming notification. It should be used in used from the callback function of Ti.Network.registerForPushNotifications.

Parameters:

- notification {Object} The payload received of the incoming notification 

### Notificare.startLocationUpdates

Start location services. This should only be called after the event 'registered' has been fired.

Parameters:

- notification {Object} The payload received of the incoming notification 

### Notificare.addTags

Adds one or more tags to the device. This should only be called after the event 'registered' has been fired.

Parameters:

- tags [{String}]

### Notificare.removeTag

Removes a tag from the device.  This should only be called after the event 'registered' has been fired.

Parameters:

- tag {String}


## Usage

See sample app in example/app.js


## Authors

Joel Oliveira <joel@notifica.re>
Joris Verbogt <joris@notifica.re>

Copyright (c) 2015 Notificare B.V.


## License

Simplified BSD