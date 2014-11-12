package com.pancat.fanrong.util.album;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * @author Administrator
 * 
 */
public class ImageItem implements Serializable {
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	//相片所属相册名称
	public String albumName;
	public boolean isSelected = false;
}
