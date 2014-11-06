package com.pancat.fanrong.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pancat.fanrong.common.RestClient;
import com.pancat.fanrong.util.LocalDateUtils;
import com.pancat.fanrong.util.StringUtils;

import android.util.Log;

/*
 * 产品类
 */
@SuppressWarnings("serial")
@DatabaseTable(tableName="product")
public class Product implements Serializable{
	private static final String TAG = "Product";
	
	//数据传递关键字
	public static final String KEY = "product_key";
	
	//产品类型
	public static final String MEIJIA = "0";
	public static final String MEIFA = "1";
	public static final String MEIZHUANGE = "2";
	
/*	//产品图片规格类型
	public static final int TINY = 0;
	public static final int SMALL = 1;
	public static final int NORMAL = 2;
	public static final int LARGE = 3;
	public static final int HUGE = 4;
	public static final int SUPERLARGE = 5;*/
	

	//产品字段关键字
	public static final String TYPE = "type";
	public static final String AUTHOR = "author";
	public static final String WIDTH = "width";
	public static final String HEIGHT = "height";
	public static final String TITLE = "title";
	public static final String DESCRIPTION = "description";
	public static final String AUTHORIMG = "author_img";
	public static final String DATE = "date";
	public static final String ID = "id";
	public static final String HOT = "hot";
	public static final String PRICE = "price";
	public static final String URL = "url";
	public static final String NUM = "num";
	
	//产品字段
	@DatabaseField
	private String productType = MEIJIA;
	@DatabaseField
	private String productUrl = "";
	@DatabaseField
	private double productWidth = -1; //default;
	@DatabaseField
	private double productHeight = -1;
	@DatabaseField
	private String productTitle = "";
	@DatabaseField
	private String productDescription = "";
	@DatabaseField
	private String productAuthor = "unkown";
	@DatabaseField
	private double productPrice = 0.0;
	@DatabaseField
	private String productAuthorImg = "";
	@DatabaseField
	private int productHot = 0;
	@DatabaseField(id = true)
	private int productId = 0;
	@DatabaseField
	private String productDate;
	@DatabaseField
	private int productNum;
	
	//服务器端产品列表的相对路径
	public static final String relativeURL="product";
	
	//产品字段的默认值Product Default
	public String PD_TYPE = MEIJIA;
	public String PD_URL = "";
	public String PD_WIDTH = "-1";
	public String PD_HEIGHT = "-1";
	public String PD_TITLE = "unkown";
	public String PD_DESCRIPTION = "产品";
	public String PD_AUTHOR = "unkown";
	public String PD_PRICE = "0.00";
	public String PD_AUTHORIMG = "";
	public String PD_HOT = "0";
	public String PD_ID = "0";
	public String PD_DATE = "2014-10-20";
	public String PD_NUM = "1";
	
	public Product(){
		
	}
	public Product(Map<String, String> productMap)
	{
		setProductType(getDefault(productMap, TYPE, PD_TYPE));
		setProductURL(getDefault(productMap, URL, PD_URL));
		setProductWidth(getDefault(productMap, WIDTH, PD_WIDTH));

		setProductHeight(getDefault(productMap, HEIGHT, PD_HEIGHT));
		setProductTitle(getDefault(productMap, TITLE, PD_TITLE));

		setProductDescription(getDefault(productMap, DESCRIPTION, PD_DESCRIPTION));
		setProductAuthor(getDefault(productMap, AUTHOR, PD_AUTHOR));
		setProductAuthorImg(getDefault(productMap, AUTHORIMG, PD_AUTHORIMG));

		setProductDate(getDefault(productMap, DATE, PD_DATE));
		setProductHot(getDefault(productMap, HOT, PD_HOT));
		setProductId(getDefault(productMap, ID, PD_ID));
		setProductPrice(getDefault(productMap, PRICE, PD_PRICE));
		setProductNum(getDefault(productMap,NUM,PD_NUM));
	}
	
	/*
	 * 获取productMap 中关键字key对应的值，如果不存在或者为null，则设置为默认值
	 * @param productMap 值键对
	 * @param key 参数键
	 * @param def 默认值
	 */
	private String getDefault(Map<String,String>productMap,String key,String def){
		if(productMap == null) return def;
		
		if(productMap.containsKey(key)){
			String res = productMap.get(key);
			if(res == null) return def;
			return res;
		}
		return def;
	}
	
	public void setProductType(String types){
		if((types == null) || types.equals("")){
			productType = MEIJIA;
			return ;
		}
		if(types.equals(MEIFA)){
			productType = MEIFA;
		}else if(types.equals(MEIJIA)){
			productType = MEIJIA;
		}else if(types.equals(MEIZHUANGE)){
			productType = MEIZHUANGE;
		}else{
			productType = PD_TYPE;
		}
	}
	
    public void setProductURL(String url)
    { 
    	productUrl = (url == null) ? "":url;
    }

    public void setProductWidth(String width){
    	try{
    		this.productWidth = Double.valueOf(width);
    	}catch(Exception e){
    		this.productWidth = Double.valueOf(PD_WIDTH);
    	}
    }
    public void setProductHeight(String heights)
    {
    	try{
    		this.productHeight = Double.valueOf(heights);
    	}catch(Exception e){
    		this.productHeight = Double.valueOf(PD_HEIGHT);
    	}
    }
    public void setProductTitle(String title)
    {
    	productTitle = title;
    }
    public void setProductDescription(String description)
    {
    	productDescription = description;
    }
    public void setProductAuthor(String author)
    {
    	if(author == "" ||author == null || author.length() == 0)
    		productAuthor = PD_AUTHOR;
    	else productAuthor = author;
    }
    public void setProductAuthorImg(String url)
    {
    	productAuthorImg = (url == null)?"":url;
    }
    public void setProductHot(String hots)
    {
    	int hot = Integer.valueOf(PD_HOT);
    	try{
    		hot = Integer.parseInt(hots);
    	}catch(Exception e){
    		
    	}
    	
    	if(hot < 0) productHot = 0;
    	else productHot = hot;
    }
    public void setProductId(String ids)
    {
    	int id = Integer.valueOf(PD_ID);
    	try{
    		id = Integer.parseInt(ids);
    	}catch(Exception e){
    		
    	}
    	if(id < 0) id = 0;
    	productId = id;
    }
    public void  setProductDate(String date)
    {  
    	this.productDate = date;
    }
    public void setProductPrice(String prices){
    	double price = Double.valueOf(PD_PRICE);
    	try{
    		price = Double.parseDouble(prices);
    	}catch(Exception e){
    		price = Double.valueOf(PD_PRICE);
    	}
    	productPrice = price >= 0 ? price: Double.valueOf(PD_PRICE);
    	DecimalFormat df = new DecimalFormat("#.00");
    	productPrice = Double.valueOf(df.format(productPrice));
    }
    public void setProductNum(String num){
    	try{
    		productNum = Integer.valueOf(num);
    	}catch(Exception e){
    		e.printStackTrace();
    		Log.d(TAG, "product Num字段必须为数字");
    	}
    }
    
	public String getProductType(){
		return  productType;
	}
    public String getProductURL() {
    	return productUrl;
    }
    public double getProductWidth(){
    	return productWidth;
    }
    public double getProductHeight(){
    	return productHeight;
    }
    public String getProductTitle(){
    	return productTitle ;
    }
    public String getProductDescription(){
    	return productDescription;
    }
    public String getProductAuthor(){
    	return productAuthor;
    }
    public String getProductAuthorImg(){
    	return productAuthorImg;
    }
    public String getProductHot(){
    	return String.valueOf(productHot);
    }
    public int getProductId() {
    	return productId ;
    }
    public String  getProductDate() {
    	return productDate;
    }
    public Date getProductdate(){
    	//Log
    	Date date = LocalDateUtils.getDateFromAllString(productDate);
    	Log.d(TAG, date.toString());
    	
    	return date;
    }
    public double getProductPrice(){
    	return productPrice;
    }
    
    public int getProductNum(){
    	return productNum;
    }
    
   /*
	@Override
	public String toString() {
		String res = "";
		res = 	ID+"*:"+productId+"#,"+
				WIDTH+"*:"+productWidth+"#,"+
				HEIGHT+"*:"+productHeight+"#,"+
				TYPE+"*:"+productType+"#,"+
				URL+"*:"+productUrl+"#,"+
				TITLE+"*:"+productTitle+"#,"+
				PRICE+"*:"+productPrice+"#,"+
				AUTHOR+"*:"+productAuthor+"#,"+
				AUTHORIMG+"*:"+productAuthorImg+"#,"+
				HOT+"*:"+productHot+"#,"+
				DESCRIPTION+"*:"+productDescription+"#,"+
				DATE+"*:"+productDate;
		return res;
	}
	
	//从toString中的字符串提取产品
	public static Product ParseFromString(String content)
	{
		String[] arr = content.split("#,");
		Map<String,String>param = new HashMap<String, String>();
		for(int i=0; i< arr.length; i++)
		{  
			String temp = arr[i];
			String[] key_value = temp.split("[*]:");
			if(key_value.length != 0)
			{   
				if(key_value.length == 1)
					param.put(key_value[0], "");
				else
					param.put(key_value[0],key_value[1]);
			}
		}
		return new Product(param);
	}
    */
}
