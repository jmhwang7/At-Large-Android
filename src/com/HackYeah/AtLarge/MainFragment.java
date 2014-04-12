package com.HackYeah.AtLarge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

public class MainFragment extends Fragment {
	private UiLifecycleHelper uiHelper;
	private static final String TAG = "MainFragment";
	private Map<Integer, String> friends = new HashMap<Integer, String>();
	private static LoginButton authButton;
	private User mUser;
	
	/**
	 * Called whenever status changes
	 */
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	    	Log.i(TAG, "call called with state opened:" + state.isOpened());
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    uiHelper = new UiLifecycleHelper(getActivity(), callback);
	    uiHelper.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, 
	        ViewGroup container, 
	        Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
	    View view = inflater.inflate(R.layout.fragment_main, container, false);
	    authButton = (LoginButton) view.findViewById(R.id.authButton);
	    authButton.setReadPermissions(Arrays.asList("user_friends"));

	    return view;
	}
	@Override
	public void onResume() {
	    super.onResume();
	    uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	/**
	 * Called whenever user logs in/out
	 * @param session
	 * @param state
	 * @param exception
	 */
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		Settings.addLoggingBehavior(LoggingBehavior.REQUESTS);
		if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        
	        new Request(session, "/me/friends", null, HttpMethod.GET, new Request.Callback() {
    	        public void onCompleted(Response response) {
    	        	Log.i(TAG, "response gotten.");
    	        	if (response.getGraphObject() != null) {
    	        		try {
							JSONArray arr = (JSONArray) response.getGraphObject().getInnerJSONObject().get("data");
							for (int i = 0; i < arr.length(); ++i) {
								JSONObject friend = arr.getJSONObject(i);
								friends.put(friend.getInt("id"), friend.getString("name"));
							}
							if (mUser != null) {
								mUser.setFriends(friends);
								Log.i(TAG, "friends: " + mUser.getFriends().toString());
								if (getActivity() != null) {
									((MainActivity) getActivity()).friendsSet();
									Log.w(TAG, "Switching views");
//									((MainActivity) getActivity()).updateMap();
								}
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    	        	}
    	        }
    	     	}).executeAsync();
	        
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	    }
	}
	/**
	 * Logout From Facebook 
	 */
	public static void callFacebookLogout(ViewGroup view) {
	    Session session = Session.getActiveSession();
	    if (session != null) {

	        if (!session.isClosed()) {
	            session.closeAndClearTokenInformation();
	            //clear your preferences if saved
	        }
	    }
	    
	}
	
	
	/**
	 * Gets user
	 */
	public void setUser(User user) {
		this.mUser = user;
	}
}
