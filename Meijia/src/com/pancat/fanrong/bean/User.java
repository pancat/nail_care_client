package com.pancat.fanrong.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 
 * @author trhuo
 *
 */
@DatabaseTable(tableName="test")
public class User {

	@DatabaseField(generatedId = true)
	private int id;
	
	@DatabaseField
	private String username;
	
	@DatabaseField
	private String password;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getusername() {
		return username;
	}

	public void setusername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
