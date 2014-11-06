package com.pancat.fanrong.bean;

//搜索标签类
public class SearchLabelAndTab {
	public static final String NAME = "name";
	public static final String TAG = "tag";
	public static final String HOTLABEL = "hot";
	public static final String COLORLABEL = "color";
	public static final String STYLELABEL = "style";
	public static final String AUTHOR = "author";
	public static final String ID = "id";
	
	private String id;
	private String name;
	private String label;
	
	public SearchLabelAndTab(String id,String name,String label){
		this.id  = id;
		this.name = name;
		this.label = (label == null)?HOTLABEL:label;
	}
	
	public String getName(){
		return name;
	}
	public String getId(){
		return id;
	}
	public String getLabel(){
		return label;
	}
}
