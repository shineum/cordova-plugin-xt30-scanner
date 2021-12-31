package com.shineum;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.janam.device.XT30.scanner.DecodeResult;
import com.janam.device.XT30.scanner.Notifications;
import com.janam.device.XT30.scanner.ResultMode;
import com.janam.device.XT30.scanner.ScanManager;
import com.janam.device.XT30.scanner.SymbologySettings;
import com.janam.device.XT30.scanner.TriggerMode;
import com.janam.device.XT30.scanner.ScanSettings;
import com.janam.device.XT30.view.KeyEvent;

public class XT30Scanner extends CordovaPlugin {
    private static CallbackContext callbackContext = null;

    private ScanManager scanManager = new ScanManager();
    private ResultMode resultMode  = ResultMode.INTENT;
    private TriggerMode triggerMode = TriggerMode.ONESHOT;
    private ScanIntentReceiver scanIntentReceiver = null;

    private Activity getActivity() { return this.cordova.getActivity(); }

    private void resumeScanner() {
		scanManager.openScanner();
		enableScanIntentReceiver(true);
    }

    private void initScanner() {
		resumeScanner();
        configureScanSettings();
    }

    @Override
    protected void pluginInitialize() {
        super.pluginInitialize();
        initScanner();
    }    

    @Override
    public void onPause(boolean multitasking) {
		super.onPause(multitasking);
		enableScanIntentReceiver(false);
		scanManager.closeScanner();
    }

    @Override
    public void onResume(boolean multitasking) {
		super.onResume(multitasking);
        resumeScanner();
    }

    // @Override
    // public void onStart() {
    // }

    // @Override
    // public void onStop() {
    // }

    // @Override
    // public void onDestroy() {
    // }

    //
    void enableScanIntentReceiver(boolean enable)
    {
        if (enable && resultMode == ResultMode.INTENT)
        {
            if (scanIntentReceiver == null)
            {
                scanIntentReceiver = new ScanIntentReceiver();
                IntentFilter filter = new IntentFilter();
                filter.addAction(scanManager.decodeIntentNames.getAction());
                getActivity().registerReceiver(scanIntentReceiver, filter);
            }
        }
        else
        {
            if (scanIntentReceiver != null)
                getActivity().unregisterReceiver(scanIntentReceiver);
            scanIntentReceiver = null;
        }
    }

	// Setup the scanner
	void configureScanSettings()
	{
		scanManager.setResultMode(resultMode);

		ScanSettings scanSettings = scanManager.getScanSettings();
		scanSettings.read();

		scanSettings.triggerMode = triggerMode;
		scanSettings.triggerTimeout = 10000; // 10 sec
		scanSettings.aimerOn = true;
		scanSettings.illumOn = true;

		Notifications notifications = scanSettings.getNotifications();

		notifications.vibrate = true;
		notifications.beep = true;

		SymbologySettings symbologySettings = scanSettings.getSymbologySettings();
		symbologySettings.enableAll(true);
		scanSettings.write();
	}    

	void setBeep(boolean isBeep)
	{
		ScanSettings scanSettings = scanManager.getScanSettings();
		scanSettings.read();

		Notifications notifications = scanSettings.getNotifications();
		notifications.beep = isBeep;

        scanSettings.write();
    }

	void setTriggerMode(TriggerMode triggerMode)
	{
        this.triggerMode = triggerMode;

		ScanSettings scanSettings = scanManager.getScanSettings();
		scanSettings.read();

        scanSettings.triggerMode = this.triggerMode;

        scanSettings.write();
    }

	// when using ResultMode.INTENT, use a BroadcastHandler
	private class ScanIntentReceiver extends BroadcastReceiver
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
            //
            if (callbackContext == null) return;

			DecodeResult decodeResult = scanManager.getDecodeResult(intent);
			if (decodeResult == null) {
                callbackContext.error("DECODE_ERROR");
			} else {
                JSONObject json = new JSONObject();
                try {
                    json.put("result", decodeResult.toString());
                    json.put("type", decodeResult.symName);
                } catch (Exception e) {}
                callbackContext.success(json);
            }
		}
	}

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {

        if (action.equals("set")) {

            this.callbackContext = callbackContext;

        } else if (action.equals("scan")) {

            scanManager.setSoftTrigger(true);

        } else if (action.equals("cancel")) {

            if (this.callbackContext != null) {
                this.callbackContext.error("USER_CANCEL");
                this.callbackContext = null;
            }
            scanManager.setSoftTrigger(false);

        } else if (action.equals("beep")) {

            setBeep(args.getInt(0) == 1);

        } else if (action.equals("triggerMode")) {

            switch(args.getInt(0)) {
                case 0:
                    setTriggerMode(TriggerMode.PULSE);
                    break;
                case 1:
                    setTriggerMode(TriggerMode.CONTINUOUS);
                    break;
                case 2:
                    setTriggerMode(TriggerMode.ONESHOT);
                    break;
            }
            
        } else {

            return false;

        }

        return true;
    }
}
