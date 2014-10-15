package com.pancat.fanrong.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.pancat.fanrong.util.StringUtils;

import android.util.Log;

public class Product implements Serializable{
	private static final String TAG = "Product";
	public static final String KEY = "product";
	
	//��Ʒ���ͳ���
	public static final String MEIJIA = "0";
	public static final String MEIFA = "1";
	public static final String MEIZHUANGE = "2";
	
	//��Ʒͼ��߶���������
	public static final int TINY = 0;
	public static final int SMALL = 1;
	public static final int NORMAL = 2;
	public static final int LARGE = 3;
	public static final int HUGE = 4;
	public static final int SUPERLARGE = 5;
	
	//��Ʒͼ��߶���ʼ�߶��빫��
	public static final double IMAGESTARTHIGH = 12.0;
	public static final double IMAGEINCREMENT = 5.0;
	
	//��Ʒ�ؼ���ӳ���
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
	
	//��Ʒ����
	private String productType = MEIJIA;
	private String productUrl = "";
	private int productWidth = -1; //default;
	private int productHeight = NORMAL;
	private String productTitle = "";
	private String productDescription = "";
	private String productAuthor = "unkown";
	private double productPrice = 0.0;
	private String productAuthorImg = "";
	private int productHot = 0;
	private int productId = 0;
	private Date productDate;
	
	public Product(Map<String, String> productMap)
	{
		try{
			setProductType(productMap.get(TYPE) );
			setProductURL(productMap.get(URL));
			//setProductWidth()
			setProductHeight(productMap.get(HEIGHT));
			setProductTitle(productMap.get(TITLE));
			Log.d(TAG, productMap.get(TITLE));
			
			setProductDescription(productMap.get(DESCRIPTION));
			setProductAuthor(productMap.get(AUTHOR));
			setProductAuthorImg(productMap.get(AUTHORIMG));
			//setProductDate(productMap.get(DATE));
			setProductHot(productMap.get(HOT));
			setProductId(productMap.get(ID));
			setProductPrice(productMap.get(PRICE));
		}catch(Exception e)
		{
			Log.d(TAG, "data bug" +e.getMessage());
		}
	}
	
	public void setProductType(String types)
	{
		if(types == null)
		{
			productType = MEIJIA;
			return ;
		}
		int type = Integer.parseInt(types);
		
		if(type <= 0 || type > 2)
		{
			productType = MEIJIA;
		}
		else if(type == 1)
		{
			productType = MEIFA;
		}
		else
		{
			productType = MEIZHUANGE;
		}
	}
    public void setProductURL(String url)
    { 
    	productUrl = (url == null) ? "":url;
    }
    public void setProductWidth(int width)
    {
    	productWidth = width;
    }
    public void setProductHeight(String heights)
    {
    	int height = NORMAL;
    	if(heights != null)
    		height = Integer.parseInt(heights);
    	switch(height)
    	{
	    	case TINY: productHeight = TINY; break;
	    	case SMALL: productHeight = SMALL; break;
	    	case NORMAL: productHeight = NORMAL; break;
	    	case LARGE: productHeight = LARGE; break;
	    	case HUGE: productHeight = HUGE; break;
	    	case SUPERLARGE: productHeight = SUPERLARGE;break;
	    	default:productHeight = NORMAL;break;
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
    		productAuthor = "unkown";
    	else productAuthor = author;
    }
    public void setProductAuthorImg(String url)
    {
    	productAuthorImg = url;
    }
    public void setProductHot(String hots)
    {
    	int hot = 0;
    	if(hots != null) hot = Integer.parseInt(hots);
    	
    	if(hot < 0) productHot =0;
    	else productHot = hot;
    }
    public void setProductId(String ids)
    {
    	int id = 0;
    	if(ids != null) id = Integer.parseInt(ids);
    	productId = id;
    }
    //TODO ʱ�ڵ�ʱ����Ҫ���һ��
    public void  setProductDate(String date)
    {  
    	try{
    		productDate = StringUtils.getDateByString(date);
    	}catch(Exception e)
    	{
    		
    	}
    }
    public void setProductPrice(String prices)
    {
    	double price = 0;
    	if(prices != null) price = Double.parseDouble(prices);
    	productPrice = price > 0 ? price:0.0;
    }
	public String getProductType()
	{
		return  productType;
	}
    public String getProductURL()
    {
    	return productUrl;
    }
    public int getProductWidth()
    {
    	return productWidth;
    }
    public int getProductHeight()
    {
    	return productHeight;
    }
    public String getProductTitle()
    {
    	return productTitle ;
    }
    public String getProductDescription()
    {
    	return productDescription;
    }
    public String getProductAuthor()
    {
    	return productAuthor;
    }
    public String getProductAuthorImg()
    {
    	return productAuthorImg;
    }
    public String getProductHot()
    {
    	return String.valueOf(productHot);
    }
    public int getProductId()
    {
    	return productId ;
    }
    //TODO ʱ�ڵ�ʱ����Ҫ���һ��
    public String  getProductDate()
    {
    	return productDate.toString();
    }
    public String getProductPrice()
    {
    	return String.valueOf(productPrice) ;
    }

	@Override
	public String toString() {
		// TODO 自动生成的方法存根
		String res = "";
		res = ID+"*:"+productId+"#,"+
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
    
}
