package com.kreggysoft.footao.fragments;

import android.support.v4.app.Fragment;

public abstract class BaseFragment extends Fragment {
	
	protected String noGameText;
	
	public BaseFragment(){
		
	}
	public abstract void refreshDataset();

}
