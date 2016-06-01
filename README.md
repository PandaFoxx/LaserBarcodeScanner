Cordova Honeywell Laser Barcode Scanner Plugin
==============

Honeywell Dolphin 7800 Barcode Scanner plugin for Cordova / PhoneGap.

Please take note that this is still in BETA phase and will have issues (see known issues below).


## Supported Platforms

- Android

## How to use

### Install

	cordova plugin com.letsap.plugins.honeywell.HoneywellScanner

### Scan

	navigator.HoneywellScanner.scan(
		function(scanResult){
			// scanResult contains {barcode}
			console.log(scanResult.barcode);
		},
		function(err){
			// error callback
			console.log(err);
		}
	);

	
## Known Issues

1. EAN 13 scan only returns the first 12 digits. (2016-06-01 work in progress)