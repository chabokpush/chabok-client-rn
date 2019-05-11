package com.adpdigital.push.rn;

import com.adpdigital.push.AdpPushClient;
import com.adpdigital.push.ChabokNotification;
import com.adpdigital.push.ChabokNotificationAction;
import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.JavaScriptModule;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by
 * mohammad on 12/13/17.
 */

public class ChabokReactPackage implements ReactPackage {
    private AdpPushClientModule adpPushClientModule;

    public AdpPushClientModule getAdpPushClientModule() {
        return adpPushClientModule;
    }

    @Override
    public List<NativeModule> createNativeModules(ReactApplicationContext reactContext) {
        List<NativeModule> modules = new ArrayList<>();
        adpPushClientModule = new AdpPushClientModule(reactContext);
        modules.add(adpPushClientModule);
        return modules;
    }

    // Deprecated RN 0.47
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }

    public static void notificationOpened(ChabokNotification message, ChabokNotificationAction notificationAction){
        AdpPushClientModule.coldStartChabokNotification = message;
        AdpPushClientModule.coldStartChabokNotificationAction = notificationAction;
    }

}
