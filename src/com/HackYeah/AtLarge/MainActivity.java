package com.HackYeah.AtLarge;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.Session;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity {
	private MainFragment mainFragment;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private User mUser = new User();
	private static final String TAG = "MainActivity";
	public static enum mainViews{
		LOGIN, MAP
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i = new Intent(this, LocationService.class);
		startService(i);
		RemoteService.loginAsync("Lorenzo", "9005");
		RemoteService.updatePositionAsync(new ArrayList<String>(){{add("-84.375957");add("33.782965");}});
		RemoteService.updatesFriends(new ArrayList<String>(){{add("69"); add("9002"); add("9003"); add("9004"); add("9001");}});
		RemoteService.getFriendsAsync();
		
//		k.putExtra(RemoteService.COMMAND_KEY, RemoteService.UPDATE_USER_COMMAND);
//		k.putExtra(RemoteService.LOCATION_KEY, new ArrayList<String>(){{add("9001"); add("9001");}});
//		startService(j);
//		Intent l = new Intent(this, RemoteService.class);
//		l.putExtra(RemoteService.COMMAND_KEY, RemoteService.LOGOUT_COMMAND);
//		startService(l);
		
		//TODO: Instantiate list with items
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
		
		mTitle = mDrawerTitle = getTitle();
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open,
				R.string.drawer_close) {
			
			// When drawer is closed
			public void onDrawerClosed(View view) {
				super.onDrawerClosed(view);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
			
			// When drawer is open
			public void onDrawerOpened(View drawerView) {
				super.onDrawerOpened(drawerView);
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu();
			}
		};
		
		if (savedInstanceState == null) {
			// Add fragment on initial activity setup
			mainFragment = new MainFragment();
			mainFragment.setUser(mUser);
			switchViews(mainViews.LOGIN);
			
		}
//		else {
//			// Or set the fragment from restored state info
//			mainFragment = (MainFragment) getSupportFragmentManager()
//					.findFragmentById(android.R.id.content);
//		}
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if(handler == null) {handler = new Handler();}
	}
	public Handler handler;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		else if (id == R.id.log_out) {
			MainFragment.callFacebookLogout((ViewGroup) mainFragment.getView());
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	
	public void friendsSet() {
		setListItems();
		switchViews(MainActivity.mainViews.MAP);
	}
	
	private void setListItems() {
		if (mUser != null && mUser.getFriends() != null && mUser.getFriends().values() != null
				&& mUser.getFriends().values().toArray() != null) {
			List<String> res = new ArrayList<>(mUser.getFriends().values());
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, res.toArray(new String[0])));
		}
		Log.w("Hi", "I am alive");
		if(mUser.getFriends() == null) {
			throw new RuntimeException("Hypothesis");
		}
		Log.w("Hi", "I am alive STILLLLLLLLLL");
	}
	
	public void switchViews(mainViews s) {
		Log.i(TAG, "switchViews called.");
		Fragment fragment = null;
		FragmentManager fragmentManager = getSupportFragmentManager();
		switch(s) {
		case MAP:
			if (fragmentManager.findFragmentById(R.id.map) == null) {
				Log.w(TAG, "making new fragment called.");
				fragment = (Fragment) new MainMapFragment();
			}
			else {
				Log.w(TAG, " using this old fragment called.");
				fragment = fragmentManager.findFragmentById(R.id.map);
			}
			break;
		case LOGIN:
			fragment = (Fragment) new MainFragment();
			((MainFragment)fragment).setUser(mUser);
			break;
		default:
			throw new RuntimeException("TODO: FIll in theis case");
		}
		Log.w(TAG, "Working on txn");
		fragmentManager.beginTransaction()
        .replace(R.id.content_frame, fragment)
        .commit();
		Log.w(TAG, "Finished the txn");
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}