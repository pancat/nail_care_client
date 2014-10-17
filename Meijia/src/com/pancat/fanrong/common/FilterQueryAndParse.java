package com.pancat.fanrong.common;

import java.util.HashMap;
import java.util.Map;

import android.util.Log;

import com.pancat.fanrong.bean.Product;

public class FilterQueryAndParse {
	public static final String TAG = "FilterQueryAndParse";
	
	//产品相关模块的询问参数
	public static final String Q_QUERY_TYPE = "query_type";
	public static final String Q_KEYWORD = "keyword";
	public static final String Q_LIMIT = "limit";
	public static final String Q_QUERY_ID = "product_id";
	public static final String Q_OFFSET = "offset";
	public static final String Q_SORT_ORDER_KEY = "order";
	public static final String Q_SORT_ORDER = "desc";
	public static final String Q_SELECT_LEFT = "select_left";
	public static final String Q_SELECT_RIGHT = "select_right";
	public static final String Q_SELECT_LIKE = "select_like";
	public static final String Q_SELECT_KEYWORDS = "select_keyword";
	public static final String Q_PRODUCT_TYPE = "product_type";
	public static final String Q_LAST_QUERY_ID = "last_query_id";
	public static final String Q_LABEL_AUTHOR = "author";
	public static final String Q_AUTHOR_ID = "author_id";
	public static final String Q_LABEL_STYLE = "style";
	public static final String Q_LABEL_COLOR = "color";
	public static final String Q_HOTSEARCH = "hotsearch";
	public static final String Q_LONGTITUDE = "longtitude";
	public static final String Q_LATITUDE = "latitude";
	public static final String Q_FIRST_DATE = "first_date";
	public static final String Q_ACTION = "action";
	public static final String Q_USER_ID = "user_id";
	public static final String Q_USER_POSITION = "user_position";
	public static final String Q_MSG = "user_msg";
	public static final String Q_PRODUCT_ID = "product_id";
	public static final String Q_NEXT = "next";
	
	//询问类型常量
	public static final int QT_1 = 1;
	public static final int QT_2 = 2;
	public static final int QT_3 = 3;
	public static final int QT_4 = 4;
	public static final int QT_5 = 5;
	public static final int QT_6 = 6;
	public static final int QT_7 = 7;
	public static final int QT_8 = 8;
	public static final int QT_9 = 9;
	public static final int QT_10 = 10;
	public static final int QT_11 = 11;
	public static final int QT_12 = 12;
	public static final int QT_13 = 13;
	
	//动作类别
	public static final int LOAD = 0;
	public static final int REFRESH = 1;
	
	//产品类型常量
	public static final int MEIJIA = 0;
	public static final int MEIZHUANGE = 1;
	public static final int MEIFA = 2;
    
	//产品类型字符串数组
	public static final String[] product = {Product.MEIJIA,Product.MEIZHUANGE,Product.MEIFA};
	
	//关键字
	public static final String HOT = "hot";
	public static final String NEW = "new";
	
	//已知排序关键字 key
	public static final String K_CRE_DATE ="cre_date";
	public static final String K_PRICE = "price";
	
	//预定义询问值pre define
	public static final int PD_QUERY_TYPE = QT_1;
	public static final int PD_PRODUCT_TYPE = MEIJIA;
	public static final String PD_KEYWORD = HOT;
	public static final int PD_LIMIT = 10;
	public static final int PD_OFFSET = 0;
	public static final int PD_ACTION = LOAD;
	public static final int PD_SELECT_LEFT = -1;
	public static final int PD_FIRST_DATE = -1;
	public static final int PD_SELECT_RIGHT = -1;
	public static final String PD_SELECT_KEYWORD = K_CRE_DATE;
	public static final String PD_SORT_ORDER_KEY = K_CRE_DATE;
	public static final int PD_SORT_ORDER = 0;
	public static final String PD_SELECT_LIKE = "";
	public static final String PD_AUTHOR = "";
	public static final String PD_STYLE = "";
	public static final String PD_COLOR = "";
	public static final String PD_USER_ID = "";
	public static final int PD_NEXT = 0;
	//服务器端返回的调试信息及反馈信息
	public static class DEBUG{
		public static final String ERROR = "error";
		public static final String DEBUG_INFO = "debug_info";
		
		public static final int PERFECT = 0;
		public static final int ABSENCEPARAMS = 1;
		public static final int NORIGHTS = 2;
		public static final int NODATA = 3;
		public static final int FREQUENT = 4;
		public static final int MORE = 5;
		public static final int FORMATERROR = 6;
		public static final int UNKOWN = 7;
	}
	
	public static Map<String,String> FilterAndRepairDefault(Map<String,Object> map){
		int query_type = QT_1;
		if(map != null && map.containsKey(Q_QUERY_TYPE)) {
			query_type = ParseErrorInt(map, Q_QUERY_TYPE,-1);
		}
		
		switch (query_type) {
		case QT_1:
			return FilterAndRepairDefault1(map);
		case QT_2:
			return FilterAndRepairDefault2(map);
		case QT_3:
			return FilterAndRepairDefault3(map);
		case QT_4:
			return FilterAndRepairDefault4(map);

		case QT_5:
			return FilterAndRepairDefault5(map);

		case QT_6:
			return FilterAndRepairDefault6(map);
		case QT_7:
			return FilterAndRepairDefault7(map);
		case QT_8:
			return FilterAndRepairDefault8(map);
		case QT_9:
			return FilterAndRepairDefault9(map);
		case QT_10:
			return FilterAndRepairDefault10(map);
		case QT_11:
			return FilterAndRepairDefault11(map);
		case QT_12:
			
			return FilterAndRepairDefault12(map);
		case QT_13:
			return FilterAndRepairDefault13(map);

		default:
              return null;
		}
	}
	private static Map<String,String> FilterAndRepairDefault1(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, S(QT_1));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_KEYWORD, HOT);
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		filter.put(Q_ACTION, S(ParseErrorInt(map, Q_ACTION, PD_ACTION)));
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault2(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, S(QT_2));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_KEYWORD, NEW);
		filter.put(Q_FIRST_DATE, S(ParseErrorInt(map, Q_FIRST_DATE, PD_FIRST_DATE)));
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		filter.put(Q_ACTION, S(ParseErrorInt(map, Q_ACTION, PD_ACTION)));
		
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault3(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_3));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_SELECT_KEYWORDS, ParseErrorString(map, Q_SELECT_KEYWORDS, PD_SELECT_KEYWORD));
		filter.put(Q_SELECT_LEFT, S(ParseErrorInt(map, Q_SELECT_LEFT, PD_SELECT_LEFT)));
		filter.put(Q_SELECT_RIGHT, S(ParseErrorInt(map, Q_SELECT_RIGHT, PD_SELECT_RIGHT)));
		filter.put(Q_SORT_ORDER_KEY, ParseErrorString(map, Q_SORT_ORDER_KEY, PD_SORT_ORDER_KEY));
		filter.put(Q_SORT_ORDER, S(ParseErrorInt(map, Q_SORT_ORDER, PD_SORT_ORDER)));
		filter.put(Q_FIRST_DATE, S(ParseErrorInt(map, Q_FIRST_DATE, PD_FIRST_DATE)));
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		filter.put(Q_ACTION, S(ParseErrorInt(map, Q_ACTION, PD_ACTION)));
		return filter;
	}
	
	private static Map<String,String> FilterAndRepairDefault4(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_4));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_SELECT_LIKE, ParseErrorString(map, Q_SELECT_LIKE, PD_SELECT_LIKE));
		filter.put(Q_LABEL_AUTHOR,ParseErrorString(map, Q_LABEL_AUTHOR, PD_AUTHOR));
		filter.put(Q_LABEL_STYLE, ParseErrorString(map, Q_LABEL_STYLE, PD_STYLE));
		filter.put(Q_LABEL_COLOR, ParseErrorString(map, Q_LABEL_COLOR, PD_COLOR));
		filter.put(Q_SORT_ORDER_KEY, ParseErrorString(map, Q_SORT_ORDER_KEY, PD_SORT_ORDER_KEY));
		filter.put(Q_SORT_ORDER, S(ParseErrorInt(map, Q_SORT_ORDER, PD_SORT_ORDER)));
		filter.put(Q_FIRST_DATE, S(ParseErrorInt(map, Q_FIRST_DATE, PD_FIRST_DATE)));
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		filter.put(Q_ACTION, S(ParseErrorInt(map, Q_ACTION, PD_ACTION)));
		
		//这里可以加更多的标签，暂不考虑
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault5(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_5));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_AUTHOR_ID, S(ParseErrorInt(map, Q_AUTHOR_ID, -1)));
		filter.put(Q_SORT_ORDER_KEY, ParseErrorString(map, Q_SORT_ORDER_KEY, PD_SORT_ORDER_KEY));
		filter.put(Q_SORT_ORDER, S(ParseErrorInt(map, Q_SORT_ORDER, PD_SORT_ORDER)));
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault6(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_6));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_SELECT_KEYWORDS, ParseErrorString(map, Q_SELECT_KEYWORDS, PD_SELECT_KEYWORD));
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault7(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_7));
		filter.put(Q_PRODUCT_TYPE, S(ParseErrorInt(map, Q_PRODUCT_TYPE, PD_PRODUCT_TYPE)));
		filter.put(Q_FIRST_DATE, S(ParseErrorInt(map, Q_FIRST_DATE, PD_FIRST_DATE)));
		filter.put(Q_LIMIT, S(ParseErrorInt(map, Q_LIMIT, PD_LIMIT)));
		filter.put(Q_OFFSET, S(ParseErrorInt(map, Q_OFFSET, PD_OFFSET)));
		filter.put(Q_ACTION, S(ParseErrorInt(map, Q_ACTION, PD_ACTION)));
		//filter.put(Q_LONGTITUDE, ParseErrorString(map, Q_LONGTITUDE, "-1"));
		//TODO 经纬度没加
		
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault8(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_8));
		filter.put(Q_PRODUCT_ID, ParseErrorString(map, Q_PRODUCT_ID, "-1"));
		filter.put(Q_USER_ID, ParseErrorString(map, Q_USER_ID, PD_USER_ID));
		return filter;
	}
	
	private static Map<String,String> FilterAndRepairDefault9(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_9));
		filter.put(Q_AUTHOR_ID, ParseErrorString(map, Q_AUTHOR_ID, ""));
		filter.put(Q_NEXT, S(ParseErrorInt(map, Q_NEXT, PD_NEXT)));
		
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault10(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_10));
		filter.put(Q_PRODUCT_ID, ParseErrorString(map, Q_PRODUCT_ID, "-1"));
		
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault11(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_11));
		
		return filter;
	}
	private static Map<String,String> FilterAndRepairDefault12(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_12));
		filter.put(Q_AUTHOR_ID, ParseErrorString(map, Q_AUTHOR_ID, ""));
		filter.put(Q_PRODUCT_ID, ParseErrorString(map, Q_PRODUCT_ID, "-1"));
		//TODO 未完全实现
		return null;
	}
	private static Map<String,String> FilterAndRepairDefault13(Map<String,Object> map){
		Map<String,String> filter = new HashMap<String,String>();
		filter.put(Q_QUERY_TYPE, String.valueOf(QT_13));
		//TODO 未完全实现
		return null;
	}
	
	private static String S(int v){
		return String.valueOf(v);
	}
	private static int ParseErrorInt(Map<String,Object>map,String key,int def){
		if(map == null ) return def;
		if(!map.containsKey(key)) return def;
		
		try{
			int v = Integer.parseInt(map.get(key).toString());
			return v;
		}catch(Exception e){
			Log.d(TAG, key+": value"+"convert into int error");
			e.printStackTrace();
		}
		return def;
	}
	
	private static String ParseErrorString(Map<String,Object>map,String key,String def){
		if(map == null ) return def;
		if(!map.containsKey(key)) return def;
		
		try{
			String v = map.get(key).toString();
			return v;
		}catch(Exception e){
			Log.d(TAG, key+": value"+"convert into String error");
			e.printStackTrace();
		}
		return def;
	}
}
