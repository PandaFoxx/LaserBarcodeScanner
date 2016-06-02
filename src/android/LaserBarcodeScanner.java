package com.letsap.plugins.honeywell;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.apache.cordova.CordovaActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;

import com.honeywell.decodemanager.DecodeManager;
import com.honeywell.decodemanager.barcode.DecodeResult;
import com.honeywell.decodemanager.SymbologyConfigs;
import com.honeywell.decodemanager.symbologyconfig.*;

public class LaserBarcodeScanner extends CordovaPlugin {

	private static final String LOG_TAG = "LaserBarcodeScanner";
	private static final int SCANTIMEOUT = 5000;

	DecodeManager decodeManager = null;
	BroadcastReceiver scannerReceiver = null;
	CallbackContext pluginCallbackContext = null;

	public LaserBarcodeScanner() {
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
		if (action.equals("scan")) {
			this.pluginCallbackContext = callbackContext;

			if (decodeManager == null) {
				decodeManager = new DecodeManager(((CordovaActivity)this.cordova.getActivity()), ScanResultHandler);
			}
			try {
				this.doScan();
				return true;

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		else if (action.equals("stop")) {
			if (decodeManager != null) {
				try {
					this.stopScan();
					return true;
	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			callbackContext.success("stopped");
			return true;
		}
		return false;
	}

	private Handler ScanResultHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DecodeManager.MESSAGE_DECODER_COMPLETE:
				String strDecodeResult = "";
				DecodeResult decodeResult = (DecodeResult) msg.obj;

				byte codeid = decodeResult.codeId;
				byte aimid = decodeResult.aimId;
				int iLength = decodeResult.length;
				String r = decodeResult.barcodeData;
				sendUpdate(getScannedInfo(r), false);
				pluginCallbackContext.success("done");
				break;
			case DecodeManager.MESSAGE_DECODER_FAIL: 
				break;
			case DecodeManager.MESSAGE_DECODER_READY: 
				ArrayList<java.lang.Integer> arry = decodeManager.getSymConfigActivityOpeartor().getAllSymbologyId();
				boolean b = arry.isEmpty();
				
				try {
					SymbologyConfigs symconfig = new SymbologyConfigs();
					SymbologyConfigCodeEan13 ean13 = new SymbologyConfigCodeEan13();
					ean13.enableSymbology(true);
					ean13.enableCheckTransmit(true);
					symconfig.addSymbologyConfig(ean13);
					decodeManager.setSymbologyConfigs(symconfig);
					
				} catch (RemoteException e) {
					e.printStackTrace();
				}
				break;
			default:
				super.handleMessage(msg);
				break;
			}
		}
	};

	private void doScan() throws Exception {
		try {
			decodeManager.doDecode(SCANTIMEOUT);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void stopScan() throws Exception {
		try {
			decodeManager.cancelDecode();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	private JSONObject getScannedInfo(String barcode) {
	    JSONObject obj = new JSONObject();
	    try {
	      obj.put("barcode", barcode);
	    } catch (JSONException e) {
	      Log.e(LOG_TAG, e.getMessage(), e);
	    }
	    return obj;
  	}

	private void sendUpdate(JSONObject info, boolean keepCallback) {
	    if (this.pluginCallbackContext != null) {
			PluginResult result = new PluginResult(PluginResult.Status.OK, info);
			result.setKeepCallback(keepCallback);
			this.pluginCallbackContext.sendPluginResult(result);
		}
	}

}
