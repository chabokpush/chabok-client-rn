package com.adpdigital.push.rn;

import android.app.IntentService;
import android.content.Intent;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import com.adpdigital.push.location.LocationManager;



public class LocationHostService extends IntentService {

    private static final String TAG = "LocationHostService";

    public LocationHostService() {super("LocationHostService");}
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LocationHostService(String name) {
        super(name);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null)
        {
            Log.d(TAG, String.valueOf(extras.get(LocationManager.LOCATION_KEY)));
            Location location = extras.getParcelable(LocationManager.LOCATION_KEY);
            if(location != null) {}
        }
    }

}