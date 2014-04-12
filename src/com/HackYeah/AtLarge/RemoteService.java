package com.HackYeah.AtLarge;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.IntentService;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.util.Log;

public class RemoteService extends IntentService {
	static final String COMMAND_KEY = "command";
	
	static final String FIND_COMMAND = "find_c";
	static final String FRIENDS_COMMAND = "friends_c"; 
	static final String UPDATE_USER_COMMAND = "update_c";
	static final String UPDATE_FRIENDS_COMMAND = "update_friends_c";
	static final String LOGIN_COMMAND = "login_c";
	static final String LOGOUT_COMMAND = "logout_c";
	
	static final String USER_KEY = "user_k";
	static final String FRIEND_KEY = "friend_k";
	static final String FRIENDS_KEY = "friends_k";
	static final String RADIUS_KEY = "radius_k";
	static final String LOCATION_KEY = "loc_k";
	
	private AndroidHttpClient client;
	private HttpContext httpContext;
	private CookieStore cookieStore;
	private final String baseUrlString = "http://www.atlarge.herokuapp.com";

	public RemoteService() {
		super("RemoteService");
	}
	
	@Override
	public void onCreate(){
		client = AndroidHttpClient.newInstance("ATLARGE_FOREVUH");
		httpContext = new BasicHttpContext();
		//TODO: Possibly save cookie onDestroy() & read onCreate()
		cookieStore = new BasicCookieStore();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}

	@Override
	protected void onHandleIntent(Intent i) {
		String command = i.getStringExtra(COMMAND_KEY);
		
		switch (command){
		case RemoteService.LOGIN_COMMAND:
			login(i.getStringExtra(USER_KEY));
		case RemoteService.LOGOUT_COMMAND:
			logout();
		case RemoteService.FRIENDS_COMMAND:
			friends();
		case RemoteService.FIND_COMMAND:
			find(i.getStringExtra(FRIEND_KEY));
		case RemoteService.UPDATE_USER_COMMAND:
			user_update(i.getStringArrayListExtra(LOCATION_KEY));
		case RemoteService.UPDATE_FRIENDS_COMMAND:
			friends_update(i.getStringArrayListExtra(FRIENDS_KEY));
		default:
			Log.w("REMOTE_WARN", "Command called and not handled: "+command);
		}
	}
	
	private void login(String user){
		try {
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/login?user="+user));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void logout(){
		try {
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/logout"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void friends(){
		try {
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/friends"));
			//TODO: Broadcast friends list
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void find(String friend){
		try {
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/find?friend="+friend));
			//TODO: Broadcast friend
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void friends_update(ArrayList<String> friends){
		
	}
	
	private void user_update(ArrayList<String> loc){
		
	}
}
