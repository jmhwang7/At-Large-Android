package com.HackYeah.AtLarge;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainMapFragment extends SupportMapFragment{

	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.fragment_map, container, false);
	    return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.w("MainMap", "on start");
		((MainActivity)getActivity()).handler.post(new Runnable() { public void run() {
			MainMapFragment.this.updateMap();
		}});
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.w("MainMap", "Activity created");
	}
	
	public void updateMap() {
//		FragmentManager manager = getSupportFragmentManager();
//		getSupportFragmentManager().getBackStackEntryAt(0);
//		for (Fragment f : manager.getFragments()) {
//			Log.e("YO", "" + f.getId());
//			
//		}
//		SupportMapFragment frag = this.findFragmentById(R.id.map);
//		if(frag == null) {
//			throw new RuntimeException("Frag is null");
//		}
		GoogleMap map = this.getMap();
		if (map == null) {
			throw new RuntimeException("map is null");
		}
		Log.w("AMMAMA", "OMGSSKLDFJKLJKFD");
		LatLng sydney = new LatLng(-33.867, 151.206);
		if (map != null) {
			map.setMyLocationEnabled(true);
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
			map.addMarker(new MarkerOptions()
					.title("Sydney")
					.position(sydney));
		}
	}
	
}
