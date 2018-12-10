package com.impinj1.microsmarter;

import java.util.ArrayList;
import java.util.List;

import com.impinj1.microsmarter.R;

import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class TaglistFragment extends ListFragment {
	private List<String> presidents = new ArrayList<String>();
	private ArrayAdapter<String> myadapter = null;
	private int curSelPosition = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.taglist, container, false);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myadapter = new ArrayAdapter<String>(getActivity(), R.layout.datalist,
				presidents);
		setListAdapter(myadapter);
	}

	public void onListItemClick(ListView parent, View v, int position, long id) {
		// 记录当前选中的标签位置
		curSelPosition = position;
		
		// 更新读写窗口的标签信息
		final FragmentManager fragmentManager = getActivity()
				.getFragmentManager();
		
		TagReadFragment objFragment = (TagReadFragment) fragmentManager
				.findFragmentById(R.id.fragment_tagRead);
		TagWriteFragment objWriteFragment = (TagWriteFragment) fragmentManager
				.findFragmentById(R.id.fragment_tagWrite);
		
		TextView txtEpc = (TextView) objFragment.getActivity().findViewById(
				R.id.txtReadEpc);
		TextView txtWriteEpc = (TextView) objWriteFragment.getActivity().findViewById(
				R.id.txtWriteEpc);
		
		txtEpc.setText(presidents.get(position));
		txtWriteEpc.setText(presidents.get(position));

		
		 Toast.makeText(getActivity(), "You have selected " +presidents.get(position), Toast.LENGTH_SHORT) .show();
		 
	}

	/**
	 * 增加列表显示项
	 * @param tagEPC 要显示的EPC信息
	 */
	public void addItem(String tagEPC) {
		presidents.add(tagEPC);
		myadapter.notifyDataSetChanged();
	}

	/**
	 * 清除列表的显示内容
	 */
	public void clearItem() {
		presidents.clear();
		
		final FragmentManager fragmentManager = getActivity()
				.getFragmentManager();
		
		TagReadFragment objFragment = (TagReadFragment) fragmentManager
				.findFragmentById(R.id.fragment_tagRead);
		TagWriteFragment objWriteFragment = (TagWriteFragment) fragmentManager
				.findFragmentById(R.id.fragment_tagWrite);
		
		TextView txtEpc = (TextView) objFragment.getActivity().findViewById(
				R.id.txtReadEpc);
		TextView txtWriteEpc = (TextView) objWriteFragment.getActivity().findViewById(
				R.id.txtWriteEpc);
		
		txtEpc.setText(getText(R.string.txt_null));
		txtWriteEpc.setText(getText(R.string.txt_null));
		
		myadapter.notifyDataSetChanged();
	}
	
	/**
	 * 更新当前被选中的项目信息
	 * @param tagEPC
	 */
	public void updateSelItem(String tagEPC) {
		if (curSelPosition != -1)
		{
			presidents.set(curSelPosition, tagEPC);
			myadapter.notifyDataSetChanged();
		}
	}
}
