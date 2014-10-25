package com.pancat.fanrong.fragment;
import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.NailTechnicianDetailActivity;
import com.pancat.fanrong.bean.Product;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class ProductAuthorFragment extends Fragment {
	private static final String TAG = "ProductAuthorFragment";
	
	private Product product;
	//技师名字
    private TextView productAFName;
    //技师评价
    private TextView productAFCharacteric;
    //技师的自我描述
    private TextView productAFDescription;
    //技师的头像
    private ImageView productAFAuthorImg;
    //技师红心框架,最多为5
    private LinearLayout red_heart_view;
    
    //禁止点击作者视图
    private boolean forbitClick = false;
    
	public static ProductAuthorFragment newInstance(Product product)
	{
		ProductAuthorFragment productAuthorFragment = new ProductAuthorFragment();
		productAuthorFragment.product = product;
		return productAuthorFragment;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onActivityCreated(savedInstanceState);
		View view = getView();
		
		productAFName = (TextView)view.findViewById(R.id.product_author_fragment_name);
	    productAFCharacteric = (TextView)view.findViewById(R.id.product_author_fragment_characteric);
	    productAFDescription = (TextView)view.findViewById(R.id.product_author_fragment_description);
	    productAFAuthorImg = (ImageView)view.findViewById(R.id.product_author_fragment_author_img);
    	red_heart_view = (LinearLayout)view.findViewById(R.id.product_author_fragment_heart_image);
	   // productAFCharacteric.setText("专业技能");
    	
	    if(product != null)
	    {
            productAFAuthorImg.setImageResource(R.drawable.user);
	    	productAFName.setText(product.getProductAuthor());
	    	//productAFDescription.setText("美甲师的具体描述");
	    }
	    
	    //为整个视图添加点击响应
	    view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!forbitClick){
					Intent intent = new Intent(getActivity(),NailTechnicianDetailActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable(Product.KEY, product);
					intent.putExtras(bundle);
					startActivity(intent);
				}
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.product_author_fragment, container,false);
	}
	
	public void setForbitClick(){
		forbitClick = true;
	}

}
