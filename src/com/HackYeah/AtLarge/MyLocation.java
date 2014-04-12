package com.HackYeah.AtLarge;

import java.util.ArrayList;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocation implements LocationListener {
	private Location loc;

	public MyLocation() {
		// TODO Auto-generated constructor stub
	}

	public void onLocationChanged(Location loc) {
		Log.w("LOCATION_WARN", "Location updated to: " + loc.toString());
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(String.valueOf(loc.getLongitude()));
		arr.add(String.valueOf(loc.getLatitude()));
		RemoteService.updatePositionAsync(arr);
	}

	public void onProviderDisabled(String provider) {
		Log.w("LOCATION_WARN", "Provider Disabled!");
	}

	public void onProviderEnabled(String provider) {
		Log.w("LOCATION_WARN", "Provider Enabled!");
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.w("LOCATION_WARN", "Provider Status Changed!");
	}
}
