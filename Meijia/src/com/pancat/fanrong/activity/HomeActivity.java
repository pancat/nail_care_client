package com.pancat.fanrong.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.fragment.HomeFragment;

@SuppressLint("ResourceAsColor")
public class HomeActivity extends Activity {

	private HomeFragment homeFragment;
	private FragmentManager fragmentManager;
	protected ScrollView scrollView;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		scrollView = (ScrollView) findViewById(R.id.home_scroll_view);
		// setFragment();
		setListView();
	}
	protected void onResume() {
		super.onResume();
	}
	private void setListView()
	{
		//绑定Layout里面的ListView  
        ListView list = (ListView) findViewById(R.id.list_view);  
        list.setBackgroundColor(getResources().getColor(R.color.grey));
          
		list.post(new Runnable() {
			@Override
			public void run() {
				scrollView.scrollTo(0, 0);
			}

		});
		// 生成动态数组，加入数据
		ArrayList<Product> listItem = new ArrayList<Product>();
		for (int i = 0; i < 100; i++) {
			listItem.add(new Product(null));
		}

		// 添加并且显示
		list.setAdapter(new ProductListAdapter(this, listItem));
	}

	public class ProductListAdapter extends BaseAdapter {

		private List<Product> data;
		private LayoutInflater layoutInflater;
		private Context context;

		public ProductListAdapter(Context context, List<Product> data) {
			this.context = context;
			this.data = data;
			this.layoutInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return data.size();
		}

		/**
		 * 获得某一位置的数据
		 */
		@Override
		public Object getItem(int position) {
			return data.get(position);
		}

		/**
		 * 获得唯一标识
		 */
		@Override
		public long getItemId(int position) {
			return data.get(position).getProductId();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = layoutInflater.inflate(R.layout.home_item_view,
						null);
			}

			ImageView icon = (ImageView) convertView.findViewById(R.id.icon);

			TextView title = (TextView) convertView.findViewById(R.id.title);
			title.setText("123123123");

			return convertView;
		}

	}

}
