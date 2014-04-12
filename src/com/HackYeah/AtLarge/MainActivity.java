package com.HackYeah.AtLarge;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.facebook.Session;

public class MainActivity extends FragmentActivity {
	private MainFragment mainFragment;
	private DrawerLayout mDrawerLayout;
	private ActionBarDrawerToggle mDrawerToggle;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private CharSequence mDrawerTitle;
	private User mUser = new User();
	private static final String TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent i = new Intent(this, LocationService.class);
		startService(i);
		
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
			getSupportFragmentManager().beginTransaction()
				.add(R.id.content_frame, mainFragment)
				.commit();
			
		}
//		else {
//			// Or set the fragment from restored state info
//			mainFragment = (MainFragment) getSupportFragmentManager()
//					.findFragmentById(android.R.id.content);
//		}
		
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
	}
	
//	@Override
//	public boolean onPrepareOptionsMenu(Menu menu) {
//		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
//		menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
//		return super.onPrepareOptionsMenu(menu);
//	}

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

//	/**
//	 * A placeholder fragment containing a simple view.
//	 */
//	public static class PlaceholderFragment extends Fragment {
//
//		public PlaceholderFragment() {
//		}
//
//		@Override
//		public View onCreateView(LayoutInflater inflater, ViewGroup container,
//				Bundle savedInstanceState) {
//			View rootView = inflater.inflate(R.layout.fragment_main, container,
//					false);
//			return rootView;
//		}
//	}
	
	public void friendsSet() {
		setListItems();
	}
	
	private void setListItems() {
//		String[] a = new String[2];
//		a[0] = "WERSDFSDF";
//		a[1] = "SDFOSEURSDF";
		if (mUser != null && mUser.getFriends() != null && mUser.getFriends().values() != null
				&& mUser.getFriends().values().toArray() != null) {
			List<String> res = new ArrayList<>(mUser.getFriends().values());
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, res.toArray(new String[0])));
		}
		if(mUser.getFriends() == null) {
			throw new NullPointerException();
		}
//		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//				android.R.layout.simple_list_item_1, a));
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}

}