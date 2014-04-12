package com.HackYeah.AtLarge;

import java.util.List;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

public class LocationService extends IntentService {
	LocationListener llistener;
	LocationManager lmanager;

	public LocationService() {
		super("LocationService");
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		llistener = new MyLocation();
		lmanager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		//TODO: instead of infinite, die when requested
		boolean settingsUpdated = true;
		while(true){
			//TODO: Read settings from somewhere
			if(settingsUpdated){
				Criteria crit = new Criteria();
				crit.setAccuracy(Criteria.ACCURACY_FINE);
				String provider =  lmanager.getBestProvider(crit, true);
				Log.d("LOCATION_DEBUG", "Provider for Location is: "+ provider);
				lmanager.requestLocationUpdates(provider, 2000, (float) 0.3, llistener);
				Log.d("LOCATION_DEBUG", "Added locationListener");
				settingsUpdated = false;
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
