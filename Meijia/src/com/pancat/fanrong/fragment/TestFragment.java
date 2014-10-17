package com.pancat.fanrong.fragment;

import com.pancat.fanrong.R;
import com.pancat.fanrong.common.FreeTimeTableView;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class TestFragment extends DialogFragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		FrameLayout cv = (FrameLayout)inflater.inflate(R.layout.test_jogrunner,container,false);
		getDialog().setTitle("");
		FragmentManager fm = getChildFragmentManager();
		View v = (new FreeTimeTableView(getActivity())).setFragmentManager(fm).setDatas(null);
		cv.addView(v);
		
		return cv;
	}
	
}
