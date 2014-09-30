package com.pancat.fanrong.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 首页广告栏广告
 * @author realxie
 *
 */

@DatabaseTable(tableName="adBannerItem")
public class AdBannerItem {

	@DatabaseField(generatedId=true)
	private int id;
	
	@DatabaseField
	private int productID; 
	
	@DatabaseField
	private String imgUrl;
	
	@DatabaseField
	private String desc;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public int getProductID() {
		return productID;
	}

	public void setProductID(int productID) {
		this.productID = productID;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}
}
