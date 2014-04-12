package com.HackYeah.AtLarge;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocation implements LocationListener {
	Location loc;

	public MyLocation() {
		// TODO Auto-generated constructor stub
	}

	public void onLocationChanged(Location loc) {
		Log.w("LOCATION_WARN", "Location updated to: " + loc.toString());
		System.out.println("Location updated to: " + loc.toString());;
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
