package com.adpdigital.push.rn;

import android.util.Log;

import com.facebook.react.bridge.ReactContext;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * Created by
 * mohammad on 12/23/17.
 */

public class Utils {
    private static final String TAG = "Utils";

    /**
     * send a JS event
     **/
    public static void sendEvent(final ReactContext context, final String eventName, Object body) {
        if (context != null) {
            context
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, body);
        } else {
            Log.d(TAG, "Missing context - cannot send event!");
        }
    }
}
