package com.pancat.fanrong.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.fragment.HomeFragment;


@SuppressLint("ResourceAsColor") public class HomeActivity extends Activity{
	
	private HomeFragment homeFragment;
	private FragmentManager fragmentManager;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
	//	setFragment();
		setListView();
	}
	
	private void setListView()
	{
		//绑定Layout里面的ListView  
        ListView list = (ListView) findViewById(R.id.list_view);  
        list.setBackgroundColor(getResources().getColor(R.color.grey));
          
        //生成动态数组，加入数据  
        ArrayList<String> listItem = new ArrayList<String>();  
        for(int i=0;i<100;i++)  
        {  
            listItem.add("item: " + i);  
        }  
       
        //添加并且显示  
        list.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItem)); 
	}

	@SuppressLint("NewApi")
	private void setFragment() {
		fragmentManager = getFragmentManager();
		//开启一个Fragment事务
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		if(homeFragment == null){
			//如果MessageFragment为空，则创建一个并添加到界面上
			homeFragment = new HomeFragment();
			transaction.add(R.id.content, homeFragment);
		}
		else{
			transaction.show(homeFragment);
		}
		transaction.commit();
	}
	
	
}
