package com.pancat.fanrong.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.Bundle;

public class DataTransfer {
	
	//会将对象变成字符串
	public static Bundle getBundle(Map<String,Object>map){
		Bundle bundle = new Bundle();
		if(map == null) return bundle;
		
		Iterator<String> iter = map.keySet().iterator();
		while(iter.hasNext()){
			String next = iter.next();
			bundle.putString(next, map.get(next)+"");
		}
		return bundle;
	}
	
	public static Map<String,Object> getMap(Bundle bundle){
		Map<String,Object> map = new HashMap<String, Object>();
		if(bundle == null) return map;
		
		Iterator<String> iter = bundle.keySet().iterator();
		while(iter.hasNext()){
			String next = iter.next();
			map.put(next, bundle.get(next));
		}
		
		return map;
	}
	
	/*
	public static Bundle getBundle(String key,List<Object> list){
		Bundle bundle = new Bundle();
		String rekey = "__sys__" + key;
		int i = 0;
		for(Object obj:list){
			String k = rekey + i;
			bundle.put
		}
	}*/
}
