package com.pancat.fanrong.bean;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 *
 * @author trhuo
 *
 */
@SuppressWarnings("serial")
@DatabaseTable(tableName="user")
public class User implements Serializable{

	@DatabaseField(id=true)
	int id = -1;

	@DatabaseField
	int age = 18;

	@DatabaseField
	String sessionid = "";

	@DatabaseField
	String username = "";

	@DatabaseField
	String nickname = "";

	@DatabaseField
	String email = "";

	@DatabaseField
	String avatarUri = "";

	@DatabaseField
	String address = "";

	@DatabaseField
	Double Latitude = 0.0;

	@DatabaseField
	Double Longitude = 0.0;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSessionid() {
		return sessionid;
	}

	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAvatarUri() {
		return avatarUri;
	}

	public void setAvatarUri(String avatarUri) {
		this.avatarUri = avatarUri;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Double getLatitude() {
		return Latitude;
	}

	public void setLatitude(Double latitude) {
		Latitude = latitude;
	}

	public Double getLongitude() {
		return Longitude;
	}

	public void setLongitude(Double longitude) {
		Longitude = longitude;
	}

}
