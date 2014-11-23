package com.example.amotz.mockLocationForDeveloper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MyActivity extends Activity {
    MockLocationProvider mockGPS;
    MockLocationProvider mockWifi;
    LocationManager locationManager;

    String logTag="GPS mock";

    /* use for test */
    float dummyLat= -10;
    float dummylong= 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        if (!isMockSettingsON(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("In order to use this app you must enable mock location do you want to enable it now?").setTitle("Mock location is not enable");
            builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i =new Intent(android.provider.Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS);
                    startActivity(i);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {

            location.getProvider();
            Log.i(logTag, location.getLatitude() + " " + location.getLongitude() + "from provider: " + location.getProvider());
            Toast.makeText(MyActivity.this,location.getLatitude() + " " + location.getLongitude() + " received from provider: " + location.getProvider(),Toast.LENGTH_LONG).show();
            if (location.getLatitude()==dummyLat && location.getLongitude()==dummylong) {
                Toast.makeText(MyActivity.this,"Test pass, For provider:" + location.getProvider(),Toast.LENGTH_LONG).show();
            }
            else {
                //Toast.makeText(MyActivity.this,"Test fail, this mean that you CANT use this app from command line",Toast.LENGTH_LONG).show();
            }
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {}

        public void onProviderEnabled(String provider) {}

        public void onProviderDisabled(String provider) {}
    };

    public void updateLoc(View view) {


        double lat,  lon;

        //Log.d(logTag,String.format("lat=%f, lon=%f",lat,lon));
        //mockGPS.pushLocation(lat, lon);
        //mockWifi.pushLocation(lat, lon);

/*        mockGPS = new MockLocationProvider(LocationManager.GPS_PROVIDER, this);
        mockWifi = new MockLocationProvider(LocationManager.NETWORK_PROVIDER, this);*/





        Intent intent = new Intent();
        intent.putExtra("lat",String.valueOf(dummyLat));
        intent.putExtra("lon",String.valueOf(dummylong));
        intent.setAction("com.example.amotz.mockLocationForDeveloper.updateLocation");
        sendBroadcast(intent);
    }

    public void stopMock(View view) {
        Log.d(logTag, "STOP MOCK");
        mockGPS.shutdown();
        mockWifi.shutdown();
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Register to both GPS and network providers
        if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        }
        if (locationManager.getAllProviders().contains(LocationManager.GPS_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(locationListener);

    }

    private boolean isMockSettingsON(Context context) {
        String st=Settings.Secure.getString(context.getContentResolver(),Settings.Secure.ALLOW_MOCK_LOCATION);
        return st.equals("1");

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_help) {
            Uri uri = Uri.parse("https://github.com/amotzte/android-mock-location-from-command-line/wiki/Help#why-do-i-need-this-app");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
