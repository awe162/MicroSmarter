package com.impinj1.microsmarter.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


public class CommonViewHolder {
    HashMap<Integer,View> mViews =new HashMap<Integer,View>();
    public final View convertView;

    public static CommonViewHolder createCVH(View convertView , Context context, int itemLayout){
        if(convertView == null){
            convertView = LayoutInflater.from(context).inflate(itemLayout,null);
            convertView.setTag( new CommonViewHolder(convertView));
        }
        return (CommonViewHolder) convertView.getTag();
    }


    public CommonViewHolder(View convertView) {
        this.convertView = convertView;
    }

    private View getV(int id){
        if(mViews.get(id) ==null){
            mViews.put(id, convertView.findViewById(id));
        }

        return mViews.get(id);
    }

    public TextView getTv(int id){
        return (TextView) getV(id);
    }
    public ImageView getIv(int id){
        return (ImageView) getV(id);
    }

    public <T extends View> T getView(int id){
        return (T)getV(id);
    }
    public <T extends View> T getView(int id , Class<T> klass){
        return (T)getV(id);
    }
}
