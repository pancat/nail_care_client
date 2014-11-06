package com.pancat.fanrong.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.pancat.fanrong.bean.Order;
import com.pancat.fanrong.bean.Order.ORDERSTATE;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.bean.User;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 30;
    public static final String[] authors = {"Jog","Runner","JogRunner","xixi","haha"};
    
    public static ArrayList<Product> generateSampleData() {
        final ArrayList<Product> data = new ArrayList<Product>(SAMPLE_DATA_ITEM_COUNT);
        
       // map.put(Product., arg1)
        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
            Random rand = new Random();
            
            Map<String, String> map = new HashMap<String, String>();
            
            map.put(Product.AUTHOR, authors[rand.nextInt(100)%authors.length]);
            int s = rand.nextInt(200)%8;
            int e = s + rand.nextInt(200)%(8-s);
            map.put(Product.TITLE,"xixihaha".substring(s, e)+"a");
            map.put(Product.HOT, "100");
            map.put(Product.PRICE, (rand.nextDouble()*10 + "").substring(0,4));
            map.put(Product.NUM, ""+(rand.nextInt(3)+1));
            data.add(new Product(map));
        }

        return data;
    }
    public static ArrayList<Order> getnerateSmapleOrder(){
    	ArrayList<Order> data = new ArrayList<Order>(SAMPLE_DATA_ITEM_COUNT);
        Order.ORDERSTATE [] a = {Order.ORDERSTATE.CANCEL,ORDERSTATE.PAYDONE,ORDERSTATE.WAITAFFIRM,ORDERSTATE.WAITPAY};;
    	
    	for(int i = 0; i< SAMPLE_DATA_ITEM_COUNT; i++){
    		Random rand = new Random();
    		
        	Map<String,Object> map = new HashMap<String, Object>();
        	map.put(Order.OID, "1");
        	map.put(Order.OPAYWAY, Order.PAYWAY.WEIXI);
        	//map.put(Order.OPRICE,"1.00");
        	map.put(Order.OPRODUCT, generateSampleData().get(0));
        	map.put(Order.OSTATE, a[rand.nextInt(a.length)]);
        	map.put(Order.OTIME, "2014-02-01");
        	User user = new User();
        	user.setAddress("中山大学外环东路");
        	user.setNickname("15626470960");
        	user.setUsername("JogRunner");
        	
        	map.put(Order.OUSER, user);
        	map.put(Order.OORDERTIME, "2014-11-10 11点");
    		data.add(new Order(map));
    	}
    	
    	return data;
    }
}
