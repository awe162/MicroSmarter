package com.impinj1.microsmarter.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.impinj1.microsmarter.R;
import com.impinj1.microsmarter.utils.CommonViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/2.
 */

public class PoPuWindowLogin extends BaseAdapter {

    private List<String> mList = new ArrayList<>();
    public PoPuWindowLogin(List<String> mList){
        this.mList = mList;

    }

    @Override
    public int getCount() {
        return mList == null ? 0 :mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CommonViewHolder cvh = CommonViewHolder.createCVH(convertView, parent.getContext(), R.layout.item_popo_login);

        cvh.getTv(R.id.popu_tv).setText(mList.get(position));

        return cvh.convertView;
    }
}
