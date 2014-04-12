package com.HackYeah.AtLarge;

import android.app.IntentService;
import android.content.Intent;

public class RemoteService extends IntentService {
	static final String COMMAND_KEY = "command";
	
	static final String FRIEND_COMMAND = "friend_c";
	static final String FRIENDS_COMMAND = "frends_c"; 
	static final String UPDATE_COMMAND = "update_c";
	static final String LOGIN_COMMAND = "login_c";
	
	static final String USER_KEY = "user_k";
	static final String LAT_KEY = "lat_k";
	static final String LONG_KEY = "long_k";
	static final String FRIEND_KEY = "friend_k";
	static final String RADIUS_KEY = "radius_k";

	public RemoteService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		// TODO Auto-generated method stub
	}

}
