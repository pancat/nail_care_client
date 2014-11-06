package com.pancat.fanrong.bean;

public class GuessLikeProduct {
	
	private String iconPath;
	private String title;
	private float distance;
	private String description;
	private float price;
	private int id;
	
	public int getId(){
		return id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public String getIconPath() {
		return iconPath;
	}
	
	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public float getDistance() {
		return distance;
	}
	
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public float getPrice() {
		return price;
	}
	
	public void setPrice(float price) {
		this.price = price;
	}
	
}
