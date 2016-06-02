var cordova = require('cordova');
var exec = require('cordova/exec');

var HoneywellScannerPlugin = function() {

  this.scan = function(success_cb, error_cb){
    exec(success_cb, error_cb, "LaserBarcodeScanner", "scan", []);
  };

  this.stop = function(success_cb, error_cb){
    exec(success_cb, error_cb, "LaserBarcodeScanner", "stop", []);
  };

  this.startListen = function(success_cb, error_cb){
    exec(success_cb, error_cb, "LaserBarcodeScanner", "start", []);
  };

};

var honeywellScannerPlugin = new HoneywellScannerPlugin();
module.exports = honeywellScannerPlugin;