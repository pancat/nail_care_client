package com.pancat.fanrong.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pancat.fanrong.bean.Product;

public class SampleData {

    public static final int SAMPLE_DATA_ITEM_COUNT = 30;

    public static ArrayList<Product> generateSampleData() {
        final ArrayList<Product> data = new ArrayList<Product>(SAMPLE_DATA_ITEM_COUNT);
        
        Map<String, String> map = new HashMap<String, String>();
        map.put(Product.AUTHOR, "jogRunner");
        map.put(Product.TITLE,"xixihaha");
        map.put(Product.HOT, "100");
        map.put(Product.PRICE, "12.3");
       // map.put(Product., arg1)
        for (int i = 0; i < SAMPLE_DATA_ITEM_COUNT; i++) {
           data.add(new Product(map));
        }

        return data;
    }

}
