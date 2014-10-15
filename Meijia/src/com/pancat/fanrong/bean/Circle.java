package com.pancat.fanrong.bean;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 圈子bean
 * @author trh
 *
 */
@SuppressWarnings("serial")
@DatabaseTable(tableName="circle")
public class Circle implements Serializable{
	@DatabaseField(generatedId=true)
	private int id;	
	@DatabaseField
	private int width;
	@DatabaseField
	private int height;
	@DatabaseField
	private String msg;
	@DatabaseField
	private String isrc;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getIsrc() {
		return isrc;
	}

	public void setIsrc(String isrc) {
		this.isrc = isrc;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth(){
		return width;
	}
	
	public void setWidth(int width){
		this.width = width;
	}
	
}
