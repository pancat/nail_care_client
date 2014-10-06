package com.pancat.fanrong.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class DuitangInfo implements Serializable{

	private int width;
	private int height;
	private String albid = "";
	private String msg = "";
	private String isrc = "";

	public String getAlbid() {
		return albid;
	}

	public void setAlbid(String albid) {
		this.albid = albid;
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
