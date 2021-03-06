package com.iitm.vishruit.netlogger;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.CellIdentityGsm;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "l0l";
    TelephonyManager telephonyManager;
    myPhoneStateListener psListener;
    Button btnShowLocation;
    GPSTracker gps;
    String filename = "logFile";
    FileOutputStream outputStream;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);

        File file = new File(this.getFilesDir(), filename);

        psListener = new myPhoneStateListener();
        telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(psListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS); // Listen to changes in signal strength
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        TextView txt2 = (TextView) findViewById(R.id.textView);
        TextView txt1 = (TextView) findViewById(R.id.textView2);
        String imei = telephonyManager.getDeviceId();
        String carrierName = telephonyManager.getNetworkOperatorName();
        txt2.setText("User ID = " + imei);
        txt1.setText("Carrier is " + carrierName);

//        // Automatically turning ON gps
//        Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
//        intent.putExtra("enabled", true);
//        sendBroadcast(intent);

        // show location button click event
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Firebase ref = new Firebase(Config.FIREBASE_URL);

                // create object of custom class GPSTracker
                gps = new GPSTracker(MainActivity.this);
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    Connectivity ck = new Connectivity();
                    String loc = "Your Location is - \nLat: " + latitude +
                            "\nLong: " + longitude +
                            "\nType: " + ck.getNetworkType(MainActivity.this);
                    Toast.makeText(getApplicationContext(), loc,
                            Toast.LENGTH_LONG).show();
                    ref.push().child("Init").setValue(loc);
                } else {
                    // Ask user to enable GPS/network in settings by throwing pop-up
                    gps.showSettingsAlert();
                }

////                ref.child("Person").setValue(person);
//                ref.setValue("qwd");
//
//                //Value event listener for realtime data update
//                ref.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot snapshot) {
//                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//                            //Getting the data from snapshot
////                            Person person = postSnapshot.getValue(Person.class);
//
//                            //Adding it to a string
//                            String string = "Name: "+"qazwsx"+"\nAddress: "+"qazwsxedc"+"\n\n";
//
//                            //Displaying it on textview
////                            textViewPersons.setText(string);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(FirebaseError firebaseError) {
//                        System.out.println("The read failed: " + firebaseError.getMessage());
//                    }
//                });






            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.iitm.vishruit.netlogger/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.iitm.vishruit.netlogger/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    /*  THIS PART IS TO SEND STRING FROM APP TO SERVER
    public void senddata(){
        RequestQueue queue = Volley.newRequestQueue(this);
        final String url ="http://tempserver.16net.net/insert.php";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("tag123", "test123");
                return params;
            }
        };

        queue.add(strRequest);

} */


    public class myPhoneStateListener extends PhoneStateListener {
        public int signalStrengthValue;

        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            if (signalStrength.isGsm()) {
                if (signalStrength.getGsmSignalStrength() != 99)// 99 is default value
                    signalStrengthValue = signalStrength.getGsmSignalStrength() * 2 - 113; // returns in ASu, to convert to db
                else
                    signalStrengthValue = signalStrength.getGsmSignalStrength();
            } else {
                signalStrengthValue = signalStrength.getCdmaDbm();
            }
            Log.i(TAG, "Strength = " + signalStrengthValue); // log the strength value for easier debugging
            TextView txt1 = (TextView) findViewById(R.id.textView1);
            txt1.setText("Strength = " + signalStrengthValue);

            String string = ""; // TODO

            try {
                outputStream = openFileOutput(filename, Context.MODE_PRIVATE);
                outputStream.write(string.getBytes());
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}