package com.example.bycycle.tracking;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;

/**
 * This class is a Service that implements a Location Listener that listens to the changes
 * on the location of the device. When it happens, the Service creates and sends an Intent
 * to the Main Activity.
 */

public class TrackingCalculations extends Service implements LocationListener {
	
	private LocationManager locationManager;

	public static final String LOC = "LocationChanged";
    static final long MIN_TIME = 10000; //10000 m
    static final float MIN_DIST  = 25; //30 m

	@Override
	public IBinder onBind(Intent intent) {return null;}
	
	@Override
    public void onCreate() {
		super.onCreate();
    }
	
	@Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, this);
        return Service.START_STICKY;
    }

	@Override
	public void onLocationChanged(Location locationNew) {
        if (locationNew!=null){
	        Intent bcIntent = new Intent(LOC);
	        bcIntent.putExtra("loc", locationNew);
	        LocalBroadcastManager.getInstance(this).sendBroadcast(bcIntent);
			
		}
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {		
	}

	@Override
	public void onProviderEnabled(String provider) {		
	}

	@Override
	public void onProviderDisabled(String provider) {		
	}
	
	
}