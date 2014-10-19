package com.pancat.fanrong.bean;

public class CircleComment {
	
	//评论id
	private int id;
	//评论的圈子id
	private int CircleId;
	//评论内容
	private String comment;
	//评论用户id
	private int uid;
	//评论时间
	private String commentTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCircleId() {
		return CircleId;
	}
	public void setCircleId(int circleId) {
		CircleId = circleId;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}
	
	
}
