package com.phoobs.pointplace2;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.phoobs.pointplace2.models.Global;


public class PhoobsLocationService extends Service {

	private static final String TAG = "PhoobsLocationService";

	public String service_latitude = "36.538927";
	public String service_longitude = "-4.623142";

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d(TAG, "PhoobsLocationService create!!!");
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "PhoobsLocationService Started!!!");

		onGetMyLocation();
		timerLocationHandler.postDelayed(timerLocationRunnable, 0);
	}

	@Override
	public void onDestroy() {
		timerLocationHandler.removeCallbacks(timerLocationRunnable);

		Log.d(TAG, "PhoobsLocationService Stopped!!!");
	}

	public void onGetMyLocation() {

		double latitude = 0;
		double longitude = 0;

		LocationManager locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);

		if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			return;
		}
		Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		if (location == null) {
        	location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
          
        	Global.location_info = "Network";
        	
        	Log.d(TAG, "Get NETWORK Location of Phone!");
        } else {
        	
        	Global.location_info = "GPS";
        	
        	Log.d(TAG, "Get GPS Location of Phone!");
        }

        if(location != null){
	        // Getting latitude of the current location
	        latitude = location.getLatitude();
	        // Getting longitude of the current location
	        longitude = location.getLongitude();
	        
	        service_latitude = String.valueOf(latitude);
	        service_longitude = String.valueOf(longitude);
	        
	        Global.latitude = service_latitude;
	        Global.longitude = service_longitude;
	        
        } 
		
		Log.d(TAG, "My Location : " + "Lat = " + service_latitude + ", Lon = " + service_longitude);
	}
	
	Handler timerLocationHandler = new Handler();
    Runnable timerLocationRunnable = new Runnable() {
        @Override
        public void run() {
        	
        	Log.i(TAG, "Timer : 10s");
        	
        	onGetMyLocation();
        	timerLocationHandler.postDelayed(this, 1000 * 10);
        }
    };


}
