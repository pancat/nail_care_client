package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.pancat.fanrong.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;


@SuppressLint("NewApi")
public class HomeFragment extends Fragment implements OnPageChangeListener{
	
	private View contextView;
	private List<ImageView> imageList;
	private TextView tvDescription;
	private LinearLayout ll;
	private int preEnablePosition = 0;//前一个被选中的点的索引位置默认设置为0
	private String[] description = {"巩俐不低俗，我就不能低俗", "朴树又回来了，再唱经典老歌引万人大合唱",
			"揭秘北京电影如何升级", "乐视网TV版大派对", "热血屌丝的反击"};
	private ViewPager viewPager;
	private boolean isStop = false;//是否停止子线程

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contextView = inflater.inflate(R.layout.fragment_home, container, false);
		init();
		Thread autoMoveThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while(!isStop){
					//每过3秒钟发一条消息到主线程，更新viewpager
					SystemClock.sleep(3000);
					getActivity().runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							int newIndex = viewPager.getCurrentItem() + 1;
							viewPager.setCurrentItem(newIndex);
							
						}
					});
				}
			}
		}); 
		autoMoveThread.start();
		return contextView;
	}
	
	private void init() {
		viewPager = (ViewPager)contextView.findViewById(R.id.view_pager);
		ll = (LinearLayout)contextView.findViewById(R.id.ll_point_group);
		tvDescription = (TextView)contextView.findViewById(R.id.tv_description);
		imageList = new ArrayList<ImageView>();
		int[] imageIDs = {R.drawable.a, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e};
		ImageView iv;
		View view;
		LayoutParams params;
		for(int id:imageIDs){
			iv = new ImageView(getActivity());
			iv.setBackgroundResource(id);
			imageList.add(iv);
			
			view = new View(getActivity());
			view.setBackgroundResource(R.drawable.point_background);
			params = new LayoutParams(15, 15);
			params.leftMargin = 5;
			view.setEnabled(false);
			view.setLayoutParams(params);
			//向线性布局中添加点
			ll.addView(view);
		}
		viewPager.setAdapter(new MyAdapter());
		viewPager.setOnPageChangeListener(this);
		//初始化图片和选中的点
		tvDescription.setText(description[0]);
		ll.getChildAt(0).setEnabled(true);
		
		int index = (Integer.MAX_VALUE/2)-((Integer.MAX_VALUE/2)%imageList.size());
		viewPager.setCurrentItem(index);
	}

	
	@Override
	public void onResume() {
//		Log.e(TAG, "resume");
		isStop = false;
		super.onResume();
	}
	
	

	@Override
	public void onPause() {
//		Log.e(TAG, "pause");
		System.exit(0);
		super.onPause();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		
	}

	@Override
	public void onPageSelected(int position) {
		int newPos = position%imageList.size();
		tvDescription.setText(description[newPos]);
		ll.getChildAt(preEnablePosition).setEnabled(false);
		ll.getChildAt(newPos).setEnabled(true);
		preEnablePosition = newPos;
	}
	
	class MyAdapter extends PagerAdapter{
		
		/**
		 * 销毁对象
		 */
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageList.get(position%imageList.size()));
		}

		
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			
			container.addView(imageList.get(position%imageList.size()));
			//点击图片操作在这里实现
			imageList.get(position%imageList.size()).setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Log.e("click","image"+position%imageList.size()+" is clicking");
				}
			});
			return imageList.get(position%imageList.size());
		}


		@Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
}
