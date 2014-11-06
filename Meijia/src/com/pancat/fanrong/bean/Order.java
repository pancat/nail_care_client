package com.pancat.fanrong.bean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pancat.fanrong.util.StringUtils;

@SuppressWarnings("serial")
@DatabaseTable(tableName="order")
public class Order implements Serializable{
	public static final String TAG = "Order";
	
	public static final String KEY = "order";
	public static final String OID = "order_id";
	public static final String OPRODUCT = "order_product";
	public static final String OTIME = "order_time";
	public static final String OSTATE = "order_state";
	public static final String OPRICE = "order_price";
	public static final String OPAYWAY = "order_payway";
	public static final String OUSER = "order_user";
	public static final String OORDERTIME = "oordertime"; //协定时间
	
	//订单的状态依次是 待付款,正在支付,已支付,等待确认，已取消，已过期，未知
	public static enum ORDERSTATE{WAITPAY,ONPAY,PAYDONE,WAITAFFIRM,CANCEL,OUTDATE,UNKOWN};
	public static enum PAYWAY{WEIXI,BABA,OTHER};
	
	public static String[] orderState = new String[]{"待付款","正在支付","已支付","等待确认","已取消","已过期","未知"};
	public static String[] orderpayWay = new String[]{"微信","支付宝","其它"};
	
	@DatabaseField
	private int id;
	
	@DatabaseField
    private ArrayList<Product> product;
	
	@DatabaseField
	private Date time;
	
	@DatabaseField
	private ORDERSTATE state;
	
	@DatabaseField
	private double price;
	
	@DatabaseField
	private PAYWAY payWay;
	
	@DatabaseField
	private User user;
	
	@DatabaseField
    private String orderTime;
    
    public Order(){
    	
    }
    
    public Order(Map<String,Object>map){
    	setId(map.get(OID));
    	setProduct(map.get(OPRODUCT));
    	setState(map.get(OSTATE));
    	setPrice(map.get(OPRICE));
    	setPayWay(map.get(OPAYWAY));
    	setUser(map.get(OUSER));
    	setTime(map.get(OTIME));
    	setOrderTime(map.get(OORDERTIME));
    }
    
    public void setId(Object id){
    	try{
    		this.id = Integer.valueOf(id.toString());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    public void setProduct(Object product){
    	try{
    		Product tmp_product = (Product)product;
    		this.product = new ArrayList<Product>();
    		this.product.add(tmp_product);
    	}catch(Exception e){
    		try{
    			this.product = (ArrayList<Product>)product;
    		}catch(Exception es){
    			es.printStackTrace();
    			Log.d(TAG, OPRODUCT+" 字段不是Product类型或者ArrayList<Product>类型");
    		}
    	}
    }
    
    public void setTime(Object time){
    	try{
    		this.time = StringUtils.getDateByString(time.toString());
    	}catch(Exception e){
    		this.time = new Date();
    		e.printStackTrace();
    		Log.d(TAG, "日期格式不对");
    	}
    }
    public void setState(Object state){
    	try {
			this.state = (ORDERSTATE)state;
		} catch (Exception e) {
			this.state = ORDERSTATE.UNKOWN;
			e.printStackTrace();
			Log.d(TAG, "订单状态未知，不是内部枚举类ORDERSTATE");
		}
    }
    public void setPrice(Object price){
    	if(price == null) this.price = Double.valueOf(__getPrice());
    	else this.price = Double.valueOf(price.toString());
    }
    private double __getPrice(){
    	if(product == null) return 0.00;
    	
    	double res = 0;
    	for(int i=0; i<product.size(); i++)
    		res += product.get(i).getProductPrice() * product.get(i).getProductNum();
    	DecimalFormat df = new DecimalFormat("#.00");
    	return Double.valueOf(df.format(res));
    }
    
    public void setPayWay(Object payway){
    	try{
    		this.payWay = (PAYWAY)payway;
    	}catch(Exception e){
    		this.payWay = PAYWAY.OTHER;
    		e.printStackTrace();
    		Log.d(TAG, "支付方式不是枚举类PAYWAY类型");
    	}
    }
    public void setUser(Object user){
    	try {
			this.user = (User)user;
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "user 对象不对");
		}
    }
    public void setOrderTime(Object orderTime){
    	if(orderTime != null){
    		this.orderTime = orderTime.toString();
    	}
    }
    
    public int getId(){
    	return id;
    }
    public Product getProduct(){
    	return (product==null)?null:product.get(0);
    }
    public ArrayList<Product> getProducts(){
    	return product;
    }
    
    public Date getTime(){
    	return time;
    }
    public ORDERSTATE getState(){
    	return state;
    }
    public double getPrice(){
    	return price;
    }
    public User getUser(){
    	return user;
    }
    public PAYWAY getPayWay(){
    	return payWay;
    }
    public String getStateStr(){
    	return getOrderStateStr(state);
    }
    public String getPayWayStr(){
    	return getOrderPayWayStr(payWay);
    }
    public String getOrderTime(){
    	return orderTime;
    }
    private String getOrderStateStr(ORDERSTATE state){
    	switch (state) {
		case WAITPAY:
			return orderState[0];
		case ONPAY:
			return orderState[1];
		case PAYDONE:
			return orderState[2];
		case WAITAFFIRM:
			return orderState[3];
		case CANCEL:
			return orderState[4];
		case OUTDATE:
			return orderState[5];
		case UNKOWN:
			return orderState[6];
		default:
			return orderState[6];
		}
    }
    private String getOrderPayWayStr(PAYWAY payway){
    	switch (payway) {
		case WEIXI:
			return orderpayWay[0];
		case BABA:
			return orderpayWay[1];
		default:
			return orderpayWay[2];
		}
    }
}
