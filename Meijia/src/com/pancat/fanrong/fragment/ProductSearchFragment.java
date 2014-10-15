package com.pancat.fanrong.fragment;

import java.util.HashMap;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.ProductSearchActivity;
import com.pancat.fanrong.activity.SearchProductShowFragmentActivity;
import com.pancat.fanrong.bean.Product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ProductSearchFragment extends Fragment {
    
	private Button searchButton;
	private EditText editText;
	//private String productType;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.product_search_fragment, container,false);
	}
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		//productType = getArguments().getString(Product.TYPE);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		searchButton = (Button)view.findViewById(R.id.product_search_button);
		editText = (EditText)view.findViewById(R.id.search_btn_tv);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO 自动生成的方法存根
				Intent intent = new Intent(getActivity(),SearchProductShowFragmentActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(ProductViewFragment.SELECT_KEYWORDS, editText.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
	}
  
}
