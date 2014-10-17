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
	private String description;
	@DatabaseField
	private String path;
	@DatabaseField
	private int uid;
	@DatabaseField
	private String creTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
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
	
	public int getUid(){
		return uid;
	}
	
	public void setUid(int uid){
		this.uid = uid;
	}
	
	
	public String getCreTime(){
		return creTime;
	}
	
	public void setCreTime(String creTime){
		this.creTime = creTime;
	}
}
