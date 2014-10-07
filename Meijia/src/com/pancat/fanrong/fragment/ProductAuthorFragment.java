package com.pancat.fanrong.fragment;
import com.pancat.fanrong.R;
import com.pancat.fanrong.bean.Product;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class ProductAuthorFragment extends Fragment {
	private static final String TAG = "ProductAuthorFragment";
	
	private Product product;
    private TextView productAFName;
    private TextView productAFCharacteric;
    private TextView productAFDescription;
    private ImageView productAFAuthorImg;
    
	public static ProductAuthorFragment newInstance(String content)
	{
		ProductAuthorFragment productAuthorFragment = new ProductAuthorFragment();
		Bundle bundle = new Bundle();
		bundle.putString(Product.KEY, content);
		productAuthorFragment.setArguments(bundle);
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
    	productAFCharacteric.setText("专业技能");
    	
	    if(product != null)
	    {
            productAFAuthorImg.setImageResource(R.drawable.user);
	    	productAFName.setText(product.getProductAuthor());
	    	productAFDescription.setText("美甲师的具体描述");
	    }
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		product = Product.ParseFromString(args.getString(Product.KEY));
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		return inflater.inflate(R.layout.product_author_fragment, container,false);
	}
	

}
