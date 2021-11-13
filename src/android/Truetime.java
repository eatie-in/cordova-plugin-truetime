package com.vishnu.truetime;

import android.util.Log;

import com.instacart.library.truetime.TrueTime;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.apache.cordova.CordovaWebView;
import org.apache.cordova.LOG;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * This class echoes a string called from JavaScript.
 */
public class Truetime extends CordovaPlugin {

  private static final String TAG = Truetime.class.getName();
  // time.apple.com (originally used in the iOS code) doesn't support ipv6. TrueTime wouldn't initiate properly.
  // time.google.com supports both v4 and v6. TrueTime initiates properly.
  private static final String DEFAULT_NTP_HOST = "time.google.com";

  @Override
  public void initialize(CordovaInterface cordova, CordovaWebView webView) {
    super.initialize(cordova, webView);
    this.sync();
  }

  private void sync() {
    cordova.getThreadPool().execute(new Runnable() {
      @Override
      public void run() {
        try {
          if (TrueTime.isInitialized()) {
            Log.w(TAG, "TrueTime already initialized");
            return;
          }
          TrueTime.build().withNtpHost(DEFAULT_NTP_HOST).initialize();
          Log.i(TAG, "TrueTime initialized");
        } catch (IOException e) {
          e.printStackTrace();
          Log.d(TAG, "Failed to initialize true");
        }
      }
    });
  }

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    if (action.equals("now")) {
      this.now(callbackContext);
      return true;
    }
    return false;
  }

  private void sendFallbackTime(CallbackContext callbackContext){
    Date fallback = new Date();
    Log.w(TAG, "Using fallback time");
    callbackContext.success(Long.toString(fallback.getTime()));
    sync();
  }


  private void now(CallbackContext callbackContext) {
    if(!TrueTime.isInitialized()){
      sendFallbackTime(callbackContext);
      return;
    }
    Log.i(TAG, "Using true time");
    Date date = TrueTime.now();
    PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, Long.toString(date.getTime()));
    callbackContext.sendPluginResult(pluginResult);
  }
}
