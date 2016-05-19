Cordova Honeywell Laser Barcode Scanner Plugin
==============

Honeywell Dolphin 7800 Barcode Scanner plugin for Cordova / PhoneGap.

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
