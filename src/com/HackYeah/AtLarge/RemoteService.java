package com.HackYeah.AtLarge;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.IntentService;
import android.content.Intent;
import android.net.http.AndroidHttpClient;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class RemoteService extends Thread {
	static final String COMMAND_KEY = "command";

	static final String FIND_COMMAND = "find_c";
	static final String FRIENDS_COMMAND = "friends_c"; 
	static final String UPDATE_USER_COMMAND = "update_c";
	static final String UPDATE_FRIENDS_COMMAND = "update_friends_c";
	static final String LOGIN_COMMAND = "login_c";
	static final String LOGOUT_COMMAND = "logout_c";

	static final String USER_KEY = "user_k";
	static final String FB_KEY = "fb_k";
	static final String FRIEND_KEY = "friend_k";
	static final String FRIENDS_KEY = "friends_k";
	static final String RADIUS_KEY = "radius_k";
	static final String LOCATION_KEY = "loc_k";
	
	public RemoteService() {

		client = new DefaultHttpClient();
		httpContext = new BasicHttpContext();
		cookieStore = new BasicCookieStore();
		httpContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
	}
	
	private static RemoteService service;
	private static synchronized RemoteService get() {
		if(service == null) {
			service = new RemoteService();
			service.start();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return service;
	}
	
	public static synchronized void loginAsync(String name, String fbId) {
				RemoteService r = get();
				Bundle b = new Bundle();
				b.putString(RemoteService.COMMAND_KEY, RemoteService.LOGIN_COMMAND);
				b.putString(RemoteService.USER_KEY, name);
				b.putString(RemoteService.FB_KEY, fbId);
				Message msg = Message.obtain(r.h);
				msg.setData(b);
				msg.sendToTarget();
	}
	
	public static synchronized void updatePositionAsync(ArrayList<String> pos) {
		RemoteService r = get();
		Bundle b = new Bundle();
		b.putString(RemoteService.COMMAND_KEY, RemoteService.UPDATE_USER_COMMAND);
		b.putStringArrayList(RemoteService.LOCATION_KEY, pos);
		Message msg = Message.obtain(r.h);
		msg.setData(b);
		msg.sendToTarget();
	}
	
	public static synchronized void logoutAsync(){
		RemoteService r = get();
		Bundle b = new Bundle();
		b.putString(RemoteService.COMMAND_KEY, RemoteService.LOGOUT_COMMAND);
		Message msg = Message.obtain(r.h);
		msg.setData(b);
		msg.sendToTarget();
	}
	
	public static synchronized void getFriendsAsync(){
		RemoteService r = get();
		Bundle b = new Bundle();
		b.putString(RemoteService.COMMAND_KEY, RemoteService.FRIENDS_COMMAND);
		Message msg = Message.obtain(r.h);
		msg.setData(b);
		msg.sendToTarget();
	}
	
	public static synchronized void findFriend(String friendFbId){
		RemoteService r = get();
		Bundle b = new Bundle();
		b.putString(RemoteService.COMMAND_KEY, RemoteService.FIND_COMMAND);
		b.putString(RemoteService.FRIEND_KEY, friendFbId);
		Message msg = Message.obtain(r.h);
		msg.setData(b);
		msg.sendToTarget();
	}
	
	public static synchronized void updatesFriends(ArrayList<String> fbIds){
		RemoteService r = get();
		Bundle b = new Bundle();
		b.putString(RemoteService.COMMAND_KEY, RemoteService.UPDATE_FRIENDS_COMMAND);
		b.putStringArrayList(RemoteService.FRIENDS_KEY, fbIds);
		Message msg = Message.obtain(r.h);
		msg.setData(b);
		msg.sendToTarget();
	}

	private HttpClient client;
	private HttpContext httpContext;
	private CookieStore cookieStore;
	private final String baseUrlString = "http://atlarge.herokuapp.com";
	public Handler h;

	public void run(){
		Looper.prepare();
		h = new Handler() {
			public void handleMessage(Message msg){ Log.d("REMOTE_DEBUG", "OnHandleIntent");
				Bundle i = msg.getData();
				String command = i.getString(COMMAND_KEY);
				switch (command){
				case RemoteService.LOGIN_COMMAND:
					login(i.getString(USER_KEY), i.getString(FB_KEY));
					break;
				case RemoteService.LOGOUT_COMMAND:
					logout();
					break;
				case RemoteService.FRIENDS_COMMAND:
					friends();
					break;
				case RemoteService.FIND_COMMAND:
					find(i.getString(FRIEND_KEY));
					break;
				case RemoteService.UPDATE_USER_COMMAND:
					user_update(i.getStringArrayList(LOCATION_KEY));
					break;
				case RemoteService.UPDATE_FRIENDS_COMMAND:
					friends_update(i.getStringArrayList(FRIENDS_KEY));
					break;
				default:
					Log.w("REMOTE_WARN", "Command called and not handled: "+command);
				}
			}
		};
		Looper.loop();
	}

	private void login(String user, String fbId){
		try {
			Log.d("REMOTE_DEBUG", "Login called! for " + user);
			Log.d("REMOTE_DEBUG", "URI: " + baseUrlString+"/login?user="+user);
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/login?user="+user+"&fbId="+fbId), httpContext);
			Log.d("REMOTE_DEUBG", "Server response: "+resp.getStatusLine().toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void logout(){
		try {
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/logout"), httpContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void friends(){
		try {
			ResponseHandler<String> respHandler=new BasicResponseHandler();
			String resp = client.execute(new HttpGet(baseUrlString+"/friends"), respHandler, httpContext);
			Log.d("REMOTE_DEBUG", "Server response: "+resp);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void find(String friend){
		try {
			HttpResponse resp = client.execute(new HttpGet(baseUrlString+"/find?friend="+friend), httpContext);
			//TODO: Broadcast friend
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void friends_update(ArrayList<String> friends){
		try{
			HttpPost q = new HttpPost(baseUrlString+"/friends/update");
			Log.d("REMOTE_DEUBG", baseUrlString+"/update");
			List<NameValuePair> friendCSV = new ArrayList<NameValuePair>();
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < friends.size()-1;i++){
				sb.append(friends.get(i));
				sb.append(",");
			}
			sb.append(friends.get(friends.size()-1));
			friendCSV.add(new BasicNameValuePair("friends", sb.toString()));
			q.setEntity(new UrlEncodedFormEntity(friendCSV));
			ResponseHandler<String> respHandler=new BasicResponseHandler();
			String resp = client.execute(q, respHandler, httpContext);
			Log.d("REMOTE_DEBUG", "Server response: "+resp);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}

	}

	private void user_update(ArrayList<String> loc){
		try{
			HttpPost q = new HttpPost(baseUrlString+"/update");
			Log.d("REMOTE_DEUBG", baseUrlString+"/update");
			List<NameValuePair> pairs = new ArrayList<NameValuePair>();
			pairs.add(new BasicNameValuePair("loc", loc.get(0) + "," + loc.get(1)));
			q.setEntity(new UrlEncodedFormEntity(pairs));
			ResponseHandler<String> respHandler=new BasicResponseHandler();
			String resp = client.execute(q, respHandler, httpContext);
			Log.d("REMOTE_DEBUG", "Server response: "+resp);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}
}
