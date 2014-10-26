package com.pancat.fanrong.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.pancat.fanrong.R;
import com.pancat.fanrong.activity.ProductSearchActivity;
import com.pancat.fanrong.activity.SearchProductShowFragmentActivity;
import com.pancat.fanrong.bean.Product;
import com.pancat.fanrong.bean.SearchLabelAndTab;
import com.pancat.fanrong.common.FilterQueryAndParse;
import com.pancat.fanrong.util.DataTransfer;

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

/*
 * 新建一个实例，需要传送产品类型过来，如果未传递，会取为默认值
 */

public class ProductSearchFragment extends Fragment {
    
	private Button searchButton;
	private EditText editText;
	private ArrayList<SearchLabelAndTab> list;
	private String productType;
	private String searchText="";
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.product_search_fragment, container,false);
	}
    
	public static ProductSearchFragment  newInstance(String productType) {
		ProductSearchFragment instance = new ProductSearchFragment();
		instance.productType = productType;
		return instance;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<SearchLabelAndTab>();
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
				//Bundle bundle = new Bundle();
				//bundle.putString(FilterQueryAndParse.Q_SELECT_KEYWORDS, editText.getText().toString());
				
				intent.putExtras(DataTransfer.getBundle(getSearchFormatData()));
				startActivity(intent);
			}
		});
	}

	//添加标签
  public void add(SearchLabelAndTab tab){
	  obtainData();
	  list.add(tab);
	  update();
  }
  //删除标签
  public void del(SearchLabelAndTab tab){
	  obtainData();
	  for(SearchLabelAndTab t:list){
		  if(t.getId().equals(tab.getId())){
			  list.remove(t);
			  break;
		  }
	  }
	  update();
  }
  //更新搜索框数据
  private void update(){
	  String str = "";
	  for(SearchLabelAndTab t:list){
		  str += t.getName()+";";
	  }
	  str += searchText;
	  if(editText != null){
		  editText.setText(str);
	  }
  }
  //获取数据
  private void obtainData(){
	  String text = editText.getText().toString();
	  String[] ar = text.split(";");
	  int index = ar.length-1;
	  //去除最后空字符串
	  while(index>=0 && ar[index].equals(""))index--;
	  
	  //获取仍然存在的标签
	  ArrayList<SearchLabelAndTab> temp = new ArrayList<SearchLabelAndTab>();
	  int i=0;
	  for(i=0;i<=index && i<list.size();i++){
		  if(list.get(i).getName().equals(ar[i]))
			  temp.add(list.get(i));
		  else break;
	  }
	  list = temp;
	  
	  //拼接用户搜索的字符串
	  searchText = "";
	  for(;i<=index;i++)
		  if(searchText.equals(""))
			  searchText = ar[i];
		  else searchText += "_"+ar[i];
  }
  
  //获取询问参数
  private Map<String,Object> getSearchFormatData(){
	  obtainData();
	  Map<String,Object>map = new HashMap<String, Object>();
	  map.put(FilterQueryAndParse.Q_QUERY_TYPE, FilterQueryAndParse.QT_4);
	  map.put(FilterQueryAndParse.Q_SELECT_LIKE, searchText);
	  String str = "";
	  for(int i=0;i<list.size();i++)
		  if(str.equals("")) str += list.get(i).getId();
		  else str+="_"+list.get(i).getId();
	  
	  map.put(FilterQueryAndParse.Q_LABLE, str);
	  return map;
  }
}
