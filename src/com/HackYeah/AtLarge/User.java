package com.HackYeah.AtLarge;

import java.util.Map;

public class User {
	private int mId;
	private String mName;
	private Map<Integer, String> mFriends;
	
	public User() {
		super();
	}
	
	public User(int id, String name) {
		this.mId = id;
		this.mName = name;
	}
	public int getId() {
		return mId;
	}
	public void setId(int mId) {
		this.mId = mId;
	}
	public String getName() {
		return mName;
	}
	public void setName(String mName) {
		this.mName = mName;
	}
	public Map<Integer, String> getFriends() {
		return mFriends;
	}
	public void setFriends(Map<Integer, String> mFriends) {
		this.mFriends = mFriends;
	}
	
	
}
