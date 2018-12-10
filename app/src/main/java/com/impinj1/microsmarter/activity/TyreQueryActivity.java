package com.impinj1.microsmarter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.impinj1.microsmarter.R;
import com.impinj1.microsmarter.bean.QueryBean;
import com.impinj1.microsmarter.bean.StorageBrand;
import com.impinj1.microsmarter.callback.StringDialogCallback;
import com.impinj1.microsmarter.constants.Constant;
import com.impinj1.microsmarter.constants.Net;
import com.impinj1.microsmarter.utils.CommonCompany;
import com.impinj1.microsmarter.utils.CommonUtils;
import com.lzy.okgo.OkGo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/26.
 */

public class TyreQueryActivity extends Activity {
    private Button mActivity_tyre_query_add_bt;
    private TextView mActivity_tyre_query_number;
    private TextView mActivity_tyre_query_brand;
    private TextView mActivity_tyre_query_factorytime;
    private TextView mActivity_tyre_query_temper;
    private TextView mActivity_tyre_query_pressure;
    private TextView mActivity_tyre_query_mileage;
    private TextView mActivity_tyre_query_replacetime;
    private TextView mActivity_tyre_query_position;
    private ImageView mActivity_tyre_query_fanhui;
    private EditText mActivity_tyre_query_label;
    private String company;
    private String mLabelId;                                    //标签名称
    private Map<String,String> mBrandMap ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //底部导航栏的颜色
            //   window.setNavigationBarColor(Color.TRANSPARENT);
        }*/
        setContentView(R.layout.activity_tyre_query);
        mLabelId = this.getIntent().getStringExtra(Constant.TYRE_NEW_ID);
        //公司名称
        company = CommonUtils.getString(TyreQueryActivity.this, Constant.LOGIN_COMPANY);
        initById();

       // BrandName();

      //  initOKgo();
    }

private void BrandName(){
OkGo.post(Net.STORAGE_BRAND)
        .tag(this)
        .connTimeOut(5000)      // 设置当前请求的连接超时时间
        .readTimeOut(5000)      // 设置当前请求的读取超时时间
        .writeTimeOut(5000)
        .params("company",company)
        .execute(new StringDialogCallback(TyreQueryActivity.this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                if (s.trim().length() == 2) {
                    CommonUtils.showToast(TyreQueryActivity.this,"您登录的公司暂无品牌信息");
                    return;
                }
                Gson gson = new Gson();
                mBrandMap = new HashMap<String, String>();
                try {
                    JSONArray array = new JSONArray(s);

//遍历全部的数组，每循环一次就是一个json对象
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);

                        StorageBrand storageBrand = gson.fromJson(jsonObject.toString(), StorageBrand.class);

                        if (!storageBrand.getBrandName().equals("泰盛")) {
                            mBrandMap.put(storageBrand.getBrandId(),storageBrand.getBrandName());
                        }

                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                CommonUtils.showToast(TyreQueryActivity.this,"请求品牌失败！！");
            }
        });
    }

    private void initById(){
        mActivity_tyre_query_fanhui = (ImageView) findViewById(R.id.activity_tyre_query_fanhui);
        mActivity_tyre_query_fanhui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActivity_tyre_query_label = (EditText) findViewById(R.id.activity_tyre_query_Label);
        mActivity_tyre_query_label.setText(mLabelId);
        mActivity_tyre_query_number = (TextView) findViewById(R.id.activity_tyre_query_number);
        mActivity_tyre_query_brand = (TextView) findViewById(R.id.activity_tyre_query_brand);
        mActivity_tyre_query_factorytime = (TextView) findViewById(R.id.activity_tyre_query_factorytime);
        mActivity_tyre_query_temper = (TextView) findViewById(R.id.activity_tyre_query_temper);
        mActivity_tyre_query_pressure = (TextView) findViewById(R.id.activity_tyre_query_pressure);
        mActivity_tyre_query_mileage = (TextView) findViewById(R.id.activity_tyre_query_mileage);
        mActivity_tyre_query_replacetime = (TextView) findViewById(R.id.activity_tyre_query_replacetime);
        mActivity_tyre_query_position = (TextView) findViewById(R.id.activity_tyre_query_position);

        mActivity_tyre_query_add_bt = (Button) findViewById(R.id.activity_tyre_query_add_bt);
        mActivity_tyre_query_add_bt.setOnClickListener(mActivity_tyre_query_add_btClickListener);

        mActivity_tyre_query_label.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId== EditorInfo.IME_ACTION_SEARCH||(event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
//点击软件盘搜索键，先将软键盘关闭。
                    InputMethodManager systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    systemService.hideSoftInputFromWindow(v.getWindowToken(),0);

                    if (mActivity_tyre_query_label.getText().toString().trim().length() != 8) {
                        CommonUtils.showToast(TyreQueryActivity.this,"请输入8位数的轮胎编号！");

                    }else {
                        IsStorage(mActivity_tyre_query_label.getText().toString().trim(),false);
                    }
                }
                return true;
            }
        });

    }


    private void IsStorage(String id, final boolean flag){
        OkGo.post(Net.IS_STORAGE)
                .tag(this)
                .connTimeOut(5000)      // 设置当前请求的连接超时时间
                .readTimeOut(5000)      // 设置当前请求的读取超时时间
                .writeTimeOut(5000)
                .params(Constant.COMPANY,company)  //选择的公交公司 将这三个属性发送到服务器
                .params("id",id)                                     //账号
                .execute(new StringDialogCallback(TyreQueryActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("s",s.toString());
                        if (s.toString().equals("-1")) {


                            new AlertDialog.Builder(TyreQueryActivity.this)
                                    .setTitle("提示框")
                                    .setMessage("您输入的轮胎编号不存在，请先入库。")
                                    .setPositiveButton("确定",null)
                                    .setCancelable(false)
                                    .show();
                        }else{

                                initStorage();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        CommonUtils.showToast(TyreQueryActivity.this,"请求服务器失败！！");
                    }
                });

    }

    /**
     * 以下三个方法点击其他任何地方关闭软键盘
     * */
    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }

    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = { 0, 0 };
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if(hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

 /**
  * 确定查询的点击事件
  * */
View.OnClickListener mActivity_tyre_query_add_btClickListener =  new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if (mActivity_tyre_query_label.getText().toString().trim().length() != 8) {
            CommonUtils.showToast(TyreQueryActivity.this,"请输入8位数的轮胎编号！");

        }else {
            if (mBrandMap == null ) {

            }
            IsStorage(mActivity_tyre_query_label.getText().toString().trim(),true);
        }

      //  initOKgo();
    }
};

private void initStorage(){

    String company = CommonUtils.getString(TyreQueryActivity.this, Constant.LOGIN_COMPANY); //公司名称
    OkGo.post(Net.QUERY)
    .tag(this)
    .connTimeOut(5000)      // 设置当前请求的连接超时时间
    .readTimeOut(5000)      // 设置当前请求的读取超时时间
    .writeTimeOut(5000)
    .params(Constant.COMPANY,company)  //选择的公交公司 将这三个属性发送到服务器
    .params("id",mActivity_tyre_query_label.getText().toString().trim())
    .execute(new StringDialogCallback(TyreQueryActivity.this) {
        @Override
        public void onSuccess(String s, Call call, Response response) {

            if (s.trim().equals("{\"error\":\"没有数据\"}")||s.trim()==null|| TextUtils.isEmpty(s.trim())) {

                mActivity_tyre_query_number.setText("无数据");
                mActivity_tyre_query_brand.setText("无数据");
                mActivity_tyre_query_factorytime.setText("无数据");
                mActivity_tyre_query_temper.setText("无数据");
                mActivity_tyre_query_pressure.setText("无数据");
                mActivity_tyre_query_mileage.setText("无数据");
                mActivity_tyre_query_replacetime.setText("无数据");
                mActivity_tyre_query_position.setText("无数据");

                return;
            }

            Gson gson = new Gson();

                QueryBean queryBean = gson.fromJson(s, QueryBean.class);


            if (queryBean.getPlace().equals("0")&&queryBean.getBrand_id().equals("0")&&queryBean.getFst_place_stamp().equals("0")&&queryBean.getLast_stamp().equals("0")&&queryBean.getMile_count().equals("0")&&queryBean.getPlate_no().equals("0")&&queryBean.getPressure().equals("0")&&queryBean.getTemp().equals("0")) {
                mActivity_tyre_query_number.setText("无数据");
                mActivity_tyre_query_brand.setText("无数据");
                mActivity_tyre_query_factorytime.setText("无数据");
                mActivity_tyre_query_temper.setText("无数据");
                mActivity_tyre_query_pressure.setText("无数据");
                mActivity_tyre_query_mileage.setText("无数据");
                mActivity_tyre_query_replacetime.setText("无数据");
                mActivity_tyre_query_position.setText("无数据");

                return;

            }

                mActivity_tyre_query_number.setText(queryBean.getPlate_no());
                mActivity_tyre_query_brand.setText(mBrandMap.get(queryBean.getBrand_id()));
                mActivity_tyre_query_factorytime.setText(getStrTime(queryBean.getFst_place_stamp()));
                mActivity_tyre_query_temper.setText(queryBean.getTemp()+" ℃");
                mActivity_tyre_query_pressure.setText(queryBean.getPressure()+" kg");
                mActivity_tyre_query_mileage.setText(queryBean.getMile_count()+" 公里");
                mActivity_tyre_query_replacetime.setText(getStrTime(queryBean.getLast_stamp()));
                mActivity_tyre_query_position.setText(CommonCompany.getTyrePosition(queryBean.getPlace()));



        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);

            mActivity_tyre_query_number.setText("- - -");
            mActivity_tyre_query_brand.setText("- - -");
            mActivity_tyre_query_factorytime.setText("- - -");
            mActivity_tyre_query_temper.setText("- - -");
            mActivity_tyre_query_pressure.setText("- - -");
            mActivity_tyre_query_mileage.setText("- - -");
            mActivity_tyre_query_replacetime.setText("- - -");
            mActivity_tyre_query_position.setText("- - -");

            CommonUtils.showToast(TyreQueryActivity.this,"请求失败,服务器异常！");
        }
    });
}

    //时间戳转字符串
    public static String getStrTime(String timeStamp){
        String timeString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        long  l = Long.valueOf(timeStamp);
        timeString = sdf.format(new Date(l));//单位秒
        return timeString;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }
}
