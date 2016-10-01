package com.iitm.vishruit.netlogger;

import android.app.IntentService;
import android.content.Intent;


public class loggingService extends IntentService {

    public loggingService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
    }
}