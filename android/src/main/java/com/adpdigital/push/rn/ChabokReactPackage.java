package com.adpdigital.push.rn;

import com.adpdigital.push.AdpPushClient;
import com.facebook.react.ReactPackage;
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

    @Override
    public List<ViewManager> createViewManagers(ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }
}
