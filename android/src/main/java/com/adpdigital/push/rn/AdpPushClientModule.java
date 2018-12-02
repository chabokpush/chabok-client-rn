package com.adpdigital.push.rn;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Debug;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.adpdigital.push.AdpPushClient;
import com.adpdigital.push.AppState;
import com.adpdigital.push.Callback;
import com.adpdigital.push.ConnectionStatus;
import com.adpdigital.push.EventMessage;
import com.adpdigital.push.PushMessage;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;
import com.facebook.react.bridge.ReadableType;
import com.facebook.react.bridge.WritableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

/**
 * Created by
 * mohammad on 12/13/17.
 */

class AdpPushClientModule extends ReactContextBaseJavaModule implements LifecycleEventListener {

    private static final String TAG = "AdpPushClientModule";
    private static final String NAME = "AdpPushClient";

    public static final String APP_STATE_ACTIVE = "active";
    public static final String APP_STATE_BACKGROUND = "background";

    private String mAppState = "uninitialized";

    private AdpPushClient chabok;
    private final LocalBroadcastManager localBroadcastManager;
    private ReactApplicationContext mReactContext;
    private LocalBroadcastReceiver mLocalBroadcastReceiver;
    private Class activityClass;

    public AdpPushClient getChabok() {
        return chabok;
    }

    public AdpPushClientModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.mReactContext = reactContext;

        registerMessageHandler();
        this.mLocalBroadcastReceiver = new LocalBroadcastReceiver();
        localBroadcastManager = LocalBroadcastManager.getInstance(reactContext);
        localBroadcastManager.registerReceiver(mLocalBroadcastReceiver, new IntentFilter(Constants.ACTION_CONNECTION_STATUS));

        chabok = AdpPushClient.get();
        if (chabok != null) {
            attachChabokClient();
        }
    }

    @ReactMethod
    public void initializeApp(ReadableMap options, Promise promise) {
        activityClass = getMainActivityClass();

        try {
            chabok = AdpPushClient.get();
        } catch (Exception exc){
            Log.d(TAG, "Chabok client not initialized");
        }

        if (chabok == null) {
            chabok = AdpPushClient.init(
                    getReactApplicationContext(),
                    activityClass,
                    options.getString("appId"),
                    options.getString("apiKey"),
                    options.getString("username"),
                    options.getString("password")
            );
        }
        chabok.setDevelopment(options.getBoolean("devMode"));
        chabok.addListener(getReactApplicationContext());
        attachChabokClient();

        if (activityClass != null) {
            WritableMap response = Arguments.createMap();
            response.putString("result", "success");
            promise.resolve(response);
        } else { // TODO improve sending error or mechanism
            promise.reject("500","Activity class is null", new IllegalArgumentException("Activity class is null"));
        }
    }

    @ReactMethod
    public void init(String appId, String apiKey, String username, String password, boolean devMode, Promise promise) {
        activityClass = getMainActivityClass();

        try {
            chabok = AdpPushClient.get();
        } catch (Exception exc){
            Log.d(TAG, "Chabok client not initialized");
        }

        if (chabok == null) {
            chabok = AdpPushClient.init(
                    getReactApplicationContext(),
                    activityClass,
                    appId,
                    apiKey,
                    username,
                    password
            );
        }
        chabok.setDevelopment(devMode);
        chabok.addListener(getReactApplicationContext());
        attachChabokClient();

        if (activityClass != null) {
            WritableMap response = Arguments.createMap();
            response.putString("result", "success");
            promise.resolve(response);
        } else { // TODO improve sending error or mechanism
            WritableMap response = Arguments.createMap();
            response.putString("result", "failed");
            promise.resolve(response);
        }
    }

    public void onEvent(ConnectionStatus status) {
        updateConnectionStatus(status);
    }

    public void onEvent(final PushMessage msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WritableMap response = Arguments.createMap();
                response.putString("alertText", msg.getAlertText());
                response.putString("alertTitle", msg.getAlertTitle());
                response.putString("body", msg.getBody());
                response.putString("intentType", msg.getIntentType());
                response.putString("senderId", msg.getSenderId());
                response.putString("sentId", msg.getSentId());
                response.putString("id", msg.getId());
                response.putString("sound", msg.getSound());
                response.putString("channel", msg.getChannel());
                response.putDouble("receivedAt", msg.getReceivedAt());
                response.putDouble("createdAt", msg.getCreatedAt());
                response.putDouble("expireAt", msg.getExpireAt());
                if (msg.getData() != null) {
                    try {
                        response.putMap("data", toWritableMap(msg.getData()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (msg.getNotification() != null) {
                    try {
                        response.putMap("notification", toWritableMap(msg.getNotification()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                //sendEvent("onMessage", response);
                sendEvent("ChabokMessageReceived", response);
            }
        });
    }

    public void onEvent(final EventMessage eventMessage) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WritableMap response = Arguments.createMap();

                response.putString("id", eventMessage.getId());
                response.putString("eventName", eventMessage.getName());
                response.putString("installationId", eventMessage.getInstallationId());
                if (eventMessage.getData() != null) {
                    try {
                        response.putMap("data", toWritableMap(eventMessage.getData()));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                sendEvent("onEvent", response);
            }
        });
    }

    public static WritableMap toWritableMap(JSONObject jsonObject) throws JSONException {
        WritableMap writableMap = Arguments.createMap();
        Iterator iterator = jsonObject.keys();
        while(iterator.hasNext()) {
            String key = (String) iterator.next();
            Object value = jsonObject.get(key);
            if (value instanceof Float || value instanceof Double) {
                writableMap.putDouble(key, jsonObject.getDouble(key));
            } else if (value instanceof Number) {
                writableMap.putInt(key, jsonObject.getInt(key));
            } else if (value instanceof String) {
                writableMap.putString(key, jsonObject.getString(key));
            } else if (value instanceof JSONObject) {
                writableMap.putMap(key,toWritableMap(jsonObject.getJSONObject(key)));
            } else if (value instanceof JSONArray){
                writableMap.putArray(key, toWritableMap(jsonObject.getJSONArray(key)));
            } else if (value == JSONObject.NULL){
                writableMap.putNull(key);
            }
        }

        return writableMap;
    }

    public static WritableArray toWritableMap(JSONArray jsonArray) throws JSONException {
        WritableArray writableArray = Arguments.createArray();
        for(int i=0; i < jsonArray.length(); i++) {
            Object value = jsonArray.get(i);
            if (value instanceof Float || value instanceof Double) {
                writableArray.pushDouble(jsonArray.getDouble(i));
            } else if (value instanceof Number) {
                writableArray.pushInt(jsonArray.getInt(i));
            } else if (value instanceof String) {
                writableArray.pushString(jsonArray.getString(i));
            } else if (value instanceof JSONObject) {
                writableArray.pushMap(toWritableMap(jsonArray.getJSONObject(i)));
            } else if (value instanceof JSONArray){
                writableArray.pushArray(toWritableMap(jsonArray.getJSONArray(i)));
            } else if (value == JSONObject.NULL){
                writableArray.pushNull();
            }
        }
        return writableArray;
    }

    private void attachChabokClient() {
        chabok.setPushListener(this);
        fetchAndUpdateConnectionStatus();
    }

    private void detachClient() {
        chabok.removePushListener(this);
        fetchAndUpdateConnectionStatus();
    }

    private void updateConnectionStatus(final ConnectionStatus connectionStatus) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String statusValue = connectionStatus.toString();
                sendEvent("connectionStatus", statusValue);
            }
        });
    }


    private void fetchAndUpdateConnectionStatus() {
        chabok.getStatus(new Callback<ConnectionStatus>() {
            @Override
            public void onSuccess(final ConnectionStatus connectionStatus) {
                updateConnectionStatus(connectionStatus);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.i(TAG, "Chabok ConnectionStatus error");
            }
        });
    }

    private Class getMainActivityClass() {
        if (activityClass != null) {
            return activityClass;
        } else {
            String packageName = mReactContext.getPackageName();
            Intent launchIntent = mReactContext.getPackageManager().getLaunchIntentForPackage(packageName);
            String className = launchIntent.getComponent().getClassName();
            try {
                return Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public void initialize() {
        getReactApplicationContext().addLifecycleEventListener(this);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();

        constants.put("playServicesAvailability", getPlayServicesStatus());
        return constants;
    }

    private WritableMap getPlayServicesStatus() {
        GoogleApiAvailability gapi = GoogleApiAvailability.getInstance();
        final int status = gapi.isGooglePlayServicesAvailable(getReactApplicationContext());
        WritableMap result = Arguments.createMap();

        if (status == ConnectionResult.SUCCESS) {
            result.putBoolean("isAvailable", true);
        } else {
            result.putBoolean("isAvailable", false);
            result.putString("error", gapi.getErrorString(status));
            result.putBoolean("isUserResolvableError", gapi.isUserResolvableError(status));
            result.putBoolean("hasResolution", new ConnectionResult(status).hasResolution());
        }
        return result;
    }

    @ReactMethod
    public void register(String userId) {
        chabok.register(userId);
    }

    @ReactMethod
    public void register(String userId, ReadableArray channels) {
        String[] chs = new String[0];
        if (channels != null) {
            chs = new String[channels.size()];
            for (int i = 0; i < channels.size(); i++) {
                String ch = channels.getString(i);
                chs[i] = ch;
            }
        }
        chabok.register(userId, chs);
    }

    @ReactMethod
    public void unregister() {
        chabok.unregister();
    }

    @ReactMethod
    public void getInstallationId(Promise promise) {
        String installationId = chabok.getInstallationId();
        if (installationId == null) {
            promise.reject("500", "The installationId is null, You didn't register yet!");
        } else {
            promise.resolve(installationId);
        }
    }

    @ReactMethod
    public void getUserId(Promise promise) {
        String userId = chabok.getUserId();
        if (userId == null) {
            promise.reject("500", "The userId is null, You didn't register yet!");
        } else {
            promise.resolve(userId);
        }
    }

    public void onEvent(AppState state){
        if (state == AppState.REGISTERED){
            WritableMap params = Arguments.createMap();

            params.putBoolean("isRegister",true);
            sendEvent("onRegister",params);
        }
    }

    @ReactMethod
    public void setDevelopment(Boolean devMode) {
        chabok.setDevelopment(devMode);
    }

//    @ReactMethod
//    public void publish(String channel, String text, final Promise promise) {
//        chabok.publish(channel, text, new Callback() {
//            @Override
//            public void onSuccess(Object o) {
//                promise.resolve(o);
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                promise.reject(throwable);
//            }
//        });
//    }
//
//    @ReactMethod
//    public void publish(String userId, String channel, String text, final Promise promise) {
//        chabok.publish(userId, channel, text, new Callback() {
//            @Override
//            public void onSuccess(Object o) {
//                Log.d(TAG, "The publish method onSuccess invoked.");
//                promise.resolve(o);
//            }
//
//            @Override
//            public void onFailure(Throwable throwable) {
//                Log.d(TAG, "The publish method onFailure invoked.");
//                promise.reject(throwable);
//            }
//        });
//    }

    @ReactMethod
    public void publish(ReadableMap message, final Promise promise) {
        ReadableMap dataMap = null;
        if (message.hasKey("data")) {
            dataMap = message.getMap("data");
        }
        String body = message.getString("content");
        String userId = message.getString("userId");
        String channel = message.getString("channel");

        PushMessage msg = new PushMessage();

        if (body != null) {
            msg.setBody(body);
        }
        if (userId != null) {
            msg.setUser(userId);
        }
        if (userId != null) {
            msg.setUser(userId);
        }
        if (channel != null) {
            msg.setChannel(channel);
        }
        try {
            if (dataMap != null) {
                JSONObject data = toJsonObject(dataMap);
                msg.setData(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            promise.reject(e);
        }

        chabok.publish(msg, new Callback() {
            @Override
            public void onSuccess(Object o) {
                promise.resolve(o);
            }

            @Override
            public void onFailure(Throwable throwable) {
                promise.reject(throwable);
            }
        });
    }

    @ReactMethod
    public void publishEvent(String eventName, ReadableMap data, final Promise promise) {
        try {
            JSONObject eventData = toJsonObject(data);
            chabok.publishEvent(eventName, eventData);
        } catch (JSONException e) {
            e.printStackTrace();
            promise.reject(e);
        }
    }

    @ReactMethod
    public void addTag(String tag, final Promise promise) {
        String[] tagsName = new String[1];
        tagsName[0] = tag;
        addTags(tagsName, promise);
    }

    @ReactMethod
    public void addTags(String[] tagsName, final Promise promise) {
        chabok.addTag(tagsName, new Callback() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "The addTags onSuccess: called");
                promise.resolve(true);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "The addTag onFailure: called");
                promise.reject(throwable);
            }
        });
    }

    @ReactMethod
    public void removeTag(String tag, final Promise promise) {
        String[] tagsName = new String[1];
        tagsName[0] = tag;
        removeTags(tagsName, promise);
    }

    @ReactMethod
    public void removeTags(String[] tagsName, final Promise promise) {
        chabok.removeTag(tagsName, new Callback() {
            @Override
            public void onSuccess(Object o) {
                Log.d(TAG, "The removeTag onSuccess: called");
                promise.resolve(true);
            }

            @Override
            public void onFailure(Throwable throwable) {
                Log.d(TAG, "The removeTag onFailure: called");
                promise.reject(throwable);
            }
        });
    }

    @ReactMethod
    public void resetBadge() {
        chabok.resetBadge();
    }

    @ReactMethod
    public void track(String trackName, ReadableMap data) {
        try {
            if (data != null) {
                chabok.track(trackName, toJsonObject(data));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @ReactMethod
    public void subscribeEvent(final String eventName, final Promise promise) {
        if (TextUtils.isEmpty(eventName)) {
            promise.reject(new IllegalArgumentException("eventName parameter is null or empty"));
            return;
        }
        chabok.subscribeEvent(eventName, new Callback() {
            @Override
            public void onSuccess(Object value) {
                promise.resolve(true);
                WritableMap params = Arguments.createMap();

                params.putString("name",eventName);
                sendEvent("onSubscribe", params);
            }

            @Override
            public void onFailure(Throwable throwable) {
                promise.reject(throwable);
                WritableMap params = Arguments.createMap();

                params.putString("error",throwable.getMessage());
                sendEvent("onSubscribe", params);
            }
        });
    }

    @ReactMethod
    public void subscribeEvent(final String eventName, String installationId, final Promise promise) {
        if (TextUtils.isEmpty(eventName)) {
            promise.reject(new IllegalArgumentException("eventName parameter is null or empty"));
            return;
        }
        if (TextUtils.isEmpty(installationId)) {
            subscribeEvent(eventName, promise);
        } else {
            chabok.subscribeEvent(eventName, installationId, new Callback() {
                @Override
                public void onSuccess(Object value) {
                    promise.resolve(true);
                    WritableMap params = Arguments.createMap();

                    params.putString("name",eventName);
                    sendEvent("onSubscribe", params);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    promise.reject(throwable);
                    WritableMap params = Arguments.createMap();

                    params.putString("error",throwable.getMessage());
                    sendEvent("onSubscribe", params);
                }
            });
        }
    }

    @ReactMethod
    public void subscribe(final String channel, final Promise promise) {
        if (TextUtils.isEmpty(channel)) {
            promise.reject(new IllegalArgumentException("channel parameter is null or empty"));
            return;
        }
        chabok.subscribe(channel, true, new Callback() {
            @Override
            public void onSuccess(Object value) {
                promise.resolve(true);
                WritableMap params = Arguments.createMap();

                params.putString("name",channel);
                sendEvent("onSubscribe", params);
            }

            @Override
            public void onFailure(Throwable throwable) {
                promise.reject(throwable);
                WritableMap params = Arguments.createMap();

                params.putString("error",throwable.getMessage());
                sendEvent("onSubscribe", params);
            }
        });
    }

    @ReactMethod
    public void unSubscribe(final String channel, final Promise promise) {
        if (TextUtils.isEmpty(channel)) {
            promise.reject(new IllegalArgumentException("channel parameter is null or empty"));
            return;
        }
        chabok.unsubscribe(channel, new Callback() {
            @Override
            public void onSuccess(Object value) {
                promise.resolve(true);
                WritableMap params = Arguments.createMap();

                params.putString("name",channel);
                sendEvent("onUnsubscribe", params);
            }

            @Override
            public void onFailure(Throwable throwable) {
                promise.reject(throwable);
                WritableMap params = Arguments.createMap();

                params.putString("error",throwable.getMessage());
                sendEvent("onUnsubscribe", params);
            }
        });
    }

    @ReactMethod
    public void unSubscribeEvent(final String eventName, final Promise promise) {
        if (TextUtils.isEmpty(eventName)) {
            promise.reject(new IllegalArgumentException("eventName parameter is null or empty"));
            return;
        }

        chabok.unsubscribeEvent(eventName, new Callback() {
            @Override
            public void onSuccess(Object value) {
                promise.resolve(true);
                WritableMap params = Arguments.createMap();

                params.putString("name",eventName);
                sendEvent("onUnsubscribe", params);
            }

            @Override
            public void onFailure(Throwable throwable) {
                promise.reject(throwable);
                WritableMap params = Arguments.createMap();

                params.putString("error",throwable.getMessage());
                sendEvent("onUnsubscribe", params);
            }
        });
    }

    @ReactMethod
    public void unSubscribeEvent(final String eventName, String installationId, final Promise promise) {
        if (TextUtils.isEmpty(eventName)) {
            promise.reject(new IllegalArgumentException("eventName parameter is null or empty"));
            return;
        }
        if (TextUtils.isEmpty(installationId)) {
            unSubscribeEvent(eventName, promise);
        } else {
            chabok.unsubscribeEvent(eventName, installationId, new Callback() {
                @Override
                public void onSuccess(Object value) {
                    promise.resolve(true);
                    WritableMap params = Arguments.createMap();

                    params.putString("name",eventName);
                    sendEvent("onUnsubscribe", params);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    promise.reject(throwable);
                    WritableMap params = Arguments.createMap();

                    params.putString("error",throwable.getMessage());
                    sendEvent("onUnsubscribe", params);
                }
            });
        }
    }

    @Override
    public void onHostResume() {
        if (chabok != null) {
            attachChabokClient();
        }
        if (mLocalBroadcastReceiver != null) {
            localBroadcastManager.registerReceiver(mLocalBroadcastReceiver, new IntentFilter(Constants.ACTION_CONNECTION_STATUS));
        }
        mAppState = APP_STATE_ACTIVE;
        sendAppStateChangeEvent();
    }

    @Override
    public void onHostPause() {
        if (chabok != null) {
            detachClient();
        }
        if (mLocalBroadcastReceiver != null) {
            localBroadcastManager.unregisterReceiver(mLocalBroadcastReceiver);
        }
        mAppState = APP_STATE_BACKGROUND;
        sendAppStateChangeEvent();
    }

    @Override
    public void onHostDestroy() {
        // By the current implementation, the
        // catalyst instance is going to be immediately dropped, and all JS calls with it.
    }

    private void sendEvent(String eventName, WritableMap params) {
        getReactApplicationContext()
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private void sendEvent(String eventName, String event) {
        if (getReactApplicationContext().hasActiveCatalystInstance()) {
            getReactApplicationContext().getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, event);
        }
    }

    private WritableMap createAppStateEventMap() {
        WritableMap appState = Arguments.createMap();
        appState.putString("app_state", mAppState);
        return appState;
    }

    private void sendAppStateChangeEvent() {
        sendEvent("appStateDidChange", createAppStateEventMap());
    }

    private void registerMessageHandler() {
        IntentFilter intentFilter = new IntentFilter("com.adpdigital.push.client.MSGRECEIVE");

        getReactApplicationContext().registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (getReactApplicationContext().hasActiveCatalystInstance()) {

                    WritableMap params = Arguments.createMap();
                    WritableMap fcmData = Arguments.createMap();

                    sendEvent("messaging_notification_received", params);

                }
            }
        }, intentFilter);
    }

    public class LocalBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String status = intent.getStringExtra("status");
            sendEvent("connectionStatus", status);
        }
    }

    private JSONObject toJsonObject(ReadableMap readableMap) throws JSONException {
        JSONObject object = new JSONObject();
        ReadableMapKeySetIterator iter = readableMap.keySetIterator();
        while (iter.hasNextKey()) {
            String key = iter.nextKey();
            ReadableType type = readableMap.getType(key);
            switch (type) {
                case Boolean:
                    object.put(key, readableMap.getBoolean(key));
                    break;
                case Number:
                    object.put(key, readableMap.getDouble(key));
                    break;
                case String:
                    object.put(key, readableMap.getString(key));
                    break;
                case Map:
                    object.put(key, toJsonObject(readableMap.getMap(key)));
                    break;
                case Array:
                    object.put(key, toJsonArray(readableMap.getArray(key)));
                    break;
            }
        }
        return object;
    }

    private JSONArray toJsonArray(ReadableArray readableArray) throws JSONException {
        JSONArray array = new JSONArray();
        for (int idx = 0; idx < readableArray.size(); idx++) {
            ReadableType type = readableArray.getType(idx);
            switch (type) {
                case Boolean:
                    array.put(readableArray.getBoolean(idx));
                    break;
                case Number:
                    array.put(readableArray.getDouble(idx));
                    break;
                case String:
                    array.put(readableArray.getString(idx));
                    break;
                case Map:
                    array.put(toJsonObject(readableArray.getMap(idx)));
                    break;
                case Array:
                    array.put(toJsonArray(readableArray.getArray(idx)));
                    break;
            }
        }
        return array;
    }
}