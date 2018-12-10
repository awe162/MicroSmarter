package com.impinj1.microsmarter.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.impinj1.microsmarter.MyApplication;
import com.impinj1.microsmarter.R;
import com.impinj1.microsmarter.constants.Constant;
import com.impinj1.microsmarter.greendao.User;
import com.impinj1.microsmarter.greendao.UserDao;
import com.impinj1.microsmarter.utils.CommonUtils;
import com.impinj1.microsmarter.utils.CommonViewHolder;
import com.lzy.okgo.OkGo;

import java.util.ArrayList;
import java.util.List;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;


/**
 * Created by Administrator on 2017/8/22.
 */

public class TyreActivity extends Activity {
    private ArrayList<String> mList = new ArrayList<String>();
    private Button mTyre_add_title_query;
    private ListView mTyre_add_lv;
    private UserDao mUserDao;

    private int mListCount = -1;
    private Button mTyre_add_bt_replace;
    private Button mTyre_add_bt_query;
    private Button mTyre_add_bt_tyreStorage;
    private Button mTyre_add_bt_overhaul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyre);
        mUserDao = MyApplication.getInstances().getDaoSession().getUserDao();
        initById();


    }


    private void initById(){
        mTyre_add_bt_query = (Button) findViewById(R.id.tyre_add_bt_query);
        mTyre_add_bt_query.setOnClickListener(mTyre_add_bt_queryClickListener);

        mTyre_add_title_query = (Button) findViewById(R.id.tyre_add_title_query);
        mTyre_add_title_query.setOnClickListener(mTyre_add_title_queryClickListener);

        mTyre_add_lv = (ListView) findViewById(R.id.tyre_add_lv);
        mTyre_add_lv.setOnItemClickListener(mTyre_add_lvItemClickListener);


        mTyre_add_bt_replace = (Button) findViewById(R.id.tyre_add_bt_replace);
        mTyre_add_bt_replace.setOnClickListener(mTyre_add_bt_replaceClickListener);

        mTyre_add_bt_tyreStorage = (Button) findViewById(R.id.tyre_add_bt_TyreStorage);
        mTyre_add_bt_tyreStorage.setOnClickListener(mTyre_add_bt_tyreStorageClickListener);

        mTyre_add_bt_overhaul = (Button) findViewById(R.id.tyre_add_bt_overhaul);
        mTyre_add_bt_overhaul.setOnClickListener(overhaulClickListener);
    }

    /**
     * 轮胎检修的点击事件
     * */
    View.OnClickListener overhaulClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mList.size() == 0) {
                CommonUtils.showToast(TyreActivity.this,"请点击查看已读取的标签");
            }else {
                if (mListCount != -1) {

                    String string = CommonUtils.getString(TyreActivity.this, Constant.LOGIN_COMPANY);

                    Log.e("string",string);

                    CommonUtils.putExtraActivity(TyreActivity.this,TyreOverhaulActivity.class,Constant.TYRE_NEW_ID,mList.get(mListCount));




                } else {
                    CommonUtils.showToast(TyreActivity.this,"请先选择您要操作修改的标签");
                }

            }


        }
    };

/*
* 轮胎入库按钮
* */
View.OnClickListener mTyre_add_bt_tyreStorageClickListener =  new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (mList.size() == 0) {
            CommonUtils.showToast(TyreActivity.this,"请点击查看已读取的标签");
        }else {
            if (mListCount != -1) {

                Log.e("mListCount",mList.get(mListCount));
                String string = CommonUtils.getString(TyreActivity.this, Constant.LOGIN_COMPANY);

                Log.e("string",string);

                CommonUtils.putExtraActivity(TyreActivity.this,TyreStorageActivity.class,Constant.TYRE_NEW_ID,mList.get(mListCount));




            } else {
                CommonUtils.showToast(TyreActivity.this,"请先选择您要操作新增的标签");
            }

        }


    }
};



/*
* 轮胎查询的点击事件
*
* */
View.OnClickListener mTyre_add_bt_queryClickListener =  new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (mList.size() == 0) {
            CommonUtils.showToast(TyreActivity.this,"请点击查看已读取的标签");
        }else {
            if (mListCount != -1) {


                String string = CommonUtils.getString(TyreActivity.this, Constant.LOGIN_COMPANY);

                Log.e("string",string);


                CommonUtils.putExtraActivity(TyreActivity.this, TyreQueryActivity.class,Constant.TYRE_NEW_ID,mList.get(mListCount));



            } else {
                CommonUtils.showToast(TyreActivity.this,"请先选择您要操作查询的标签");
            }

        }

    }
};


/*
* 轮胎修改的点击事件
*
* */
View.OnClickListener mTyre_add_bt_replaceClickListener =  new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        if (mList.size() == 0) {
            CommonUtils.showToast(TyreActivity.this,"请点击查看已读取的标签");
        }else {
            if (mListCount != -1) {

                String string = CommonUtils.getString(TyreActivity.this, Constant.LOGIN_COMPANY);

                Log.e("string",string);

                CommonUtils.putExtraActivity(TyreActivity.this,TyreReplaceActivity.class,Constant.TYRE_NEW_ID,mList.get(mListCount));




            } else {
                CommonUtils.showToast(TyreActivity.this,"请先选择您要操作修改的标签");
            }

        }


    }
};


/*StringCallback replaceCallback =  new StringCallback() {
    @Override
    public void onSuccess(String s, Call call, Response response) {

        Log.e("s",s.toString());
        if (s.toString().equals("0")) {

            CommonUtils.putExtraActivity(TyreActivity.this,TyreNewActivity.class,Constant.TYRE_NEW_ID,mList.get(mListCount));

        }else{

            CommonUtils.LongToast(TyreActivity.this,"您的标签不存在,请先新增！");
        }

    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);

    }
};*/



/*StringCallback NewCallback =  new StringCallback() {
    @Override
    public void onSuccess(String s, Call call, Response response) {

        Log.e("s",s.toString());
        if (s.toString().equals("-1")) {

            CommonUtils.putExtraActivity(TyreActivity.this,TyreNewActivity.class,Constant.TYRE_NEW_ID,mList.get(mListCount));

        }else{

            CommonUtils.LongToast(TyreActivity.this,"您的标签已经存在，无需新增！");
        }

    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        CommonUtils.showToast(TyreActivity.this,"新增失败，请检查您的网络！");
    }
};*/

int mposition=-1;
AdapterView.OnItemClickListener mTyre_add_lvItemClickListener =   new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mposition=position;

        mListCount = position;

        TyreAddListViewAdapter.notifyDataSetChanged();


    }

};


View.OnClickListener mTyre_add_title_queryClickListener =  new View.OnClickListener() {


    @Override
    public void onClick(View v) {

/*  String string = CommonUtils.getString(getActivity(), Constant.LABEL_ID);

            Log.e("s",string);
            String[] split = string.split(",");

            for (String s : split) {

                Log.e("s",s.toString());
            }*/

        mList.clear();

        List<User> users = mUserDao.loadAll();

        if (users.size() > 0) {


            for (int i = 0; i < users.size(); i++) {

                mList.add(users.get(i).getName());

            }

            mTyre_add_lv.setAdapter(TyreAddListViewAdapter);

        }else {


            CommonUtils.LongToast(TyreActivity.this,"暂无读取的标签，请先连接蓝牙读取标签");
            TyreAddListViewAdapter.notifyDataSetChanged();
        }


    }
};

private BaseAdapter TyreAddListViewAdapter = new BaseAdapter() {
    @Override
    public int getCount() {
        return mList.size() == 0 ? 0 : mList.size();
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

        CommonViewHolder cvh = CommonViewHolder.createCVH(convertView, parent.getContext(), R.layout.item_tyre_add_listview);
        cvh.getTv(R.id.item_tyre_add_text).setText(mList.get(position));



        if (mposition != -1) {
            if (position==mposition){
                cvh.getView(R.id.item_tyre_add_ll, LinearLayout.class).setBackgroundColor(Color.GRAY);
                // item_tyre_add_ll.setBackgroundColor(Color.GRAY);
            }else {
                cvh.getView(R.id.item_tyre_add_ll,LinearLayout.class).setBackgroundColor(Color.WHITE);
            }
        }


        return cvh.convertView;
    }
};

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
