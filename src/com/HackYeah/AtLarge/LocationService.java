package com.HackYeah.AtLarge;

import java.util.List;

import android.app.IntentService;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.util.Log;

public class LocationService extends Service {
	private LocationListener llistener;
	private LocationManager lmanager;

	public LocationService() {
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		llistener = new MyLocation();
		lmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {		//TODO: instead of infinite, die when requested
		//TODO: Read settings from somewhere
		Criteria crit = new Criteria();
		crit.setAccuracy(Criteria.ACCURACY_FINE);
		lmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 00, (float) 0.0, llistener);
		Log.d("LOCATION_DEBUG", "Added locationListener");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		Log.d("LOCATION_DEBUG", "Location Service Destroyed! :(");
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
