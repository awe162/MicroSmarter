package com.impinj1.microsmarter.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.DateUtil;
import com.google.gson.Gson;
import com.impinj1.microsmarter.R;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;


/**
 * Created by Administrator on 2017/11/6 0006.
 */

public class TyreStorageActivity extends Activity {
    private EditText mActivity_tyre_new_label;

    private Spinner mActivity_tyre_new_brand;
    private ArrayAdapter arradp_brand;
    private ArrayAdapter arradp_fleet;
 //   private String[] brandarr = {"固特异","双钱","赛轮","倍耐力","佳通","其它","普利司通","马牌","德力","锦湖"};
 private String[] brandarr = {"韩泰","佳通"};
// private String[] brandarr = {"米其林","固特异","朝阳"};

    private Button mActivity_tyre_new_add_bt;
    private TextView mActivity_tyre_new_factorytime;
    private LinearLayout mActivity_tyre_new_factorytime_ll;
    private LinearLayout mActivity_tyre_new_replacetime_ll;
    private TextView mActivity_tyre_new_replacetime;
    private ImageView mActivity_tyre_new_fanhui;
    private EditText mActivity_tyre_new_depth;
    private EditText mActivity_tyre_new_pressuretop;
    private EditText mActivity_tyre_new_pressurebottom;
    private EditText mActivity_tyre_new_temperaturetop;
    private EditText mActivity_tyre_new_temperaturebottom;
    private Spinner mActivity_tyre_new_fleet;
    private String company;
    private List<String> mFleetList = new ArrayList<String>();    //用来存放车队
    private List<String> displayList = new ArrayList<String>();   //用来存放要显示的车队的中文名
    private RelativeLayout mActivity_tyre_new_bisic;
    private LinearLayout mActivity_tyre_new_ll;
    private String mLabelId;                                    //标签名称
    private Map<String,String> mBrandMap = new HashMap<>();
    private List<String> mmBrandList = new ArrayList<>();
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
        setContentView(R.layout.activity_tyre_storage);

        mLabelId = this.getIntent().getStringExtra(Constant.TYRE_NEW_ID);
        //公司名称
        company = CommonUtils.getString(TyreStorageActivity.this, Constant.LOGIN_COMPANY);

        intiById();

        initSpinner();
     //   initFleet();


        initBrand();
    }

    private void intiById(){


        //    mActivity_tyre_new_sensor = (EditText) findViewById(R.id.activity_tyre_new_sensor);
        mActivity_tyre_new_bisic = (RelativeLayout) findViewById(R.id.activity_tyre_new_bisic);

        mActivity_tyre_new_depth = (EditText) findViewById(R.id.activity_tyre_new_depth);
        mActivity_tyre_new_depth.setText("5");
        mActivity_tyre_new_pressuretop = (EditText) findViewById(R.id.activity_tyre_new_pressuretop);
        mActivity_tyre_new_pressuretop.setText("12.5");
        mActivity_tyre_new_pressurebottom = (EditText) findViewById(R.id.activity_tyre_new_pressurebottom);
        mActivity_tyre_new_pressurebottom.setText("6.5");
        mActivity_tyre_new_temperaturetop = (EditText) findViewById(R.id.activity_tyre_new_temperaturetop);
        mActivity_tyre_new_temperaturetop.setText("90");
        mActivity_tyre_new_temperaturebottom = (EditText) findViewById(R.id.activity_tyre_new_temperaturebottom);
        mActivity_tyre_new_temperaturebottom.setText("-20");
        mActivity_tyre_new_fanhui = (ImageView) findViewById(R.id.activity_tyre_new_fanhui);
        mActivity_tyre_new_fanhui.setOnClickListener(fanhuiClickListener);
        mActivity_tyre_new_label = (EditText) findViewById(R.id.activity_tyre_new_Label);
        mActivity_tyre_new_label.setText(mLabelId);

        mActivity_tyre_new_add_bt = (Button) findViewById(R.id.activity_tyre_new_add_bt);
        mActivity_tyre_new_add_bt.setOnClickListener(mActivity_tyre_new_add_btClickListener);

        mActivity_tyre_new_factorytime = (TextView) findViewById(R.id.activity_tyre_new_factorytime);

        mActivity_tyre_new_factorytime_ll = (LinearLayout) findViewById(R.id.activity_tyre_new_factorytime_ll);
        mActivity_tyre_new_factorytime_ll.setOnClickListener(mActivity_tyre_new_factorytime_llClickListener);

        mActivity_tyre_new_replacetime_ll = (LinearLayout) findViewById(R.id.activity_tyre_new_replacetime_ll);
        mActivity_tyre_new_replacetime_ll.setOnClickListener(mActivity_tyre_new_replacetime_llClickListene);

        mActivity_tyre_new_replacetime = (TextView) findViewById(R.id.activity_tyre_new_replacetime);


      //  mActivity_tyre_new_bisic.setOnTouchListener(mTouchListener);
/**
 * 监听轮胎边编号输入框焦点的   变化
 * */
//mActivity_tyre_new_label.setOnFocusChangeListener(FocusChangeListener);
/**
 * 点击软键盘的搜索按键的时候进入此回调
 * */
mActivity_tyre_new_label.setOnEditorActionListener(OnEditorActionListener);



    }


    TextView.OnEditorActionListener OnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId== EditorInfo.IME_ACTION_SEARCH||(event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
//点击软件盘搜索键，先将软键盘关闭。
                InputMethodManager systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                systemService.hideSoftInputFromWindow(v.getWindowToken(),0);

                if (mActivity_tyre_new_label.getText().toString().trim().length() != 8) {
                    CommonUtils.showToast(TyreStorageActivity.this,"请输入8位数的轮胎编号！");

                }else {
                IsStorage(mActivity_tyre_new_label.getText().toString().trim(),false);
                }
            }
            return true;
        }
    };


    View.OnFocusChangeListener FocusChangeListener =  new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (mActivity_tyre_new_label.isFocused()){
              //  mActivity_tyre_new_bisic.setClickable(false);
            CommonUtils.showToast(TyreStorageActivity.this,"获取焦点");
            }else {
               // mActivity_tyre_new_bisic.setClickable(true);
                CommonUtils.showToast(TyreStorageActivity.this,"失去焦点");
            }
        }
    };

    View.OnTouchListener mTouchListener =  new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            mActivity_tyre_new_bisic.setFocusable(true);
            mActivity_tyre_new_bisic.setFocusableInTouchMode(true);
            mActivity_tyre_new_bisic.requestFocus();

            return false;
        }
    };
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


    View.OnClickListener  fanhuiClickListener =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    /*
    * 更换时间的点击事件
    * */
    View.OnClickListener mActivity_tyre_new_replacetime_llClickListene =  new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(DateUtil.getDateForString(getCurrentData()),mActivity_tyre_new_replacetime);
        }
    };

    /*
    * 出厂时间的点击事件
    *
    * */
    View.OnClickListener mActivity_tyre_new_factorytime_llClickListener =   new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(DateUtil.getDateForString(getCurrentData()),mActivity_tyre_new_factorytime);

        }
    };

    /**
     * 获取当前年月日
     * @return "2017-10-27"
     */
    private String getCurrentData(){
        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = sDateFormat.format(new java.util.Date());
        return date;
    }

    /**
     * 选择开始日期
     */
    private void showDialog(List<Integer> list, final TextView tv) {

        DatePickerDialog.Builder builder = new DatePickerDialog.Builder(this);
        builder.setOnDateSelectedListener(new com.example.liangmutian.mypicker.DatePickerDialog.OnDateSelectedListener() {
            @Override
            public void onDateSelected(int[] dates) {

                tv.setText(dates[0] + "-" + (dates[1] > 9 ? dates[1] : ("0" + dates[1])) + "-"
                        + (dates[2] > 9 ? dates[2] : ("0" + dates[2])));
            }

            @Override
            public void onCancel() {

            }
        }).setSelectYear(list.get(0) - 1)
                .setSelectMonth(list.get(1) - 1)
                .setSelectDay(list.get(2) - 1);
        builder.setMaxYear(DateUtil.getYear());
        builder.setMaxMonth(DateUtil.getDateForString(DateUtil.getToday()).get(1));
        builder.setMaxDay(DateUtil.getDateForString(DateUtil.getToday()).get(2));
        Dialog dateDialog = builder.create();
        dateDialog.show();
    }
private void IsStorage(String id, final boolean flag){
    OkGo.post(Net.IS_STORAGE)
            .tag(this)
    .connTimeOut(15000)      // 设置当前请求的连接超时时间
    .readTimeOut(15000)      // 设置当前请求的读取超时时间
    .writeTimeOut(15000)
    .params(Constant.COMPANY,company)  //选择的公交公司 将这三个属性发送到服务器
    .params("id",id)                                     //账号
    .execute(new StringDialogCallback(TyreStorageActivity.this) {
        @Override
        public void onSuccess(String s, Call call, Response response) {
            Log.e("s",s.toString());
            if (s.toString().equals("0")) {


                new AlertDialog.Builder(TyreStorageActivity.this)
                        .setTitle("提示框")
                        .setMessage("您输入的轮胎编号已经存在，无需入库。")
                        .setPositiveButton("重新输入",null)
                        .setCancelable(false)
                        .show();
            }else{

                if (flag) {
                    initStorage();
                }else {
                    CommonUtils.LongToast(TyreStorageActivity.this,"搜索成功！请继续输入...");
                }
            }
        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            CommonUtils.showToast(TyreStorageActivity.this,"请求服务器失败！！");
        }
    });

}


    /*
    * 新增入库的点击事件
    *
    * */
    View.OnClickListener mActivity_tyre_new_add_btClickListener =   new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //下方是车队


            if (mActivity_tyre_new_label.getText().toString().trim().length() != 8) {
                CommonUtils.showToast(TyreStorageActivity.this,"请输入8位数的轮胎编号！");

            }else {
                IsStorage(mActivity_tyre_new_label.getText().toString().trim(),true);
            }
        }
    };

    private void initStorage(){
        String label = mActivity_tyre_new_label.getText().toString().trim();

        String factorytime = mActivity_tyre_new_factorytime.getText().toString();//出厂时间
        String replace = mActivity_tyre_new_replacetime.getText().toString();    //新增时间
        String depth = mActivity_tyre_new_depth.getText().toString().trim();     //花纹深度
        String pressuretop = mActivity_tyre_new_pressuretop.getText().toString().trim(); //压力上限
        String pressurebottom = mActivity_tyre_new_pressurebottom.getText().toString().trim();//压力下限
        String temperaturetop = mActivity_tyre_new_temperaturetop.getText().toString().trim(); //温度上限
        String temperaturebottom = mActivity_tyre_new_temperaturebottom.getText().toString().trim();//温度下限
//    String sensor = mActivity_tyre_new_sensor.getText().toString().trim();    //传感器编号


      /*  if (label.length()!=8) {
            CommonUtils.LongToast(TyreStorageActivity.this,"您的轮胎编号不正确,请写入8位编号！");
            return;
        }*/

        if (factorytime.equals("选择生产日期")) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有选择生产日期哦！");
            return;
        }
        if (replace.equals("选择入库时间")) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有选择入库时间哦！");
            return;
        }


        if (depth == null || TextUtils.isEmpty(depth)) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有输入花纹深度哦！");
            return;
        }

        if (pressuretop == null || TextUtils.isEmpty(pressuretop)) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有输入压力上限哦！");
            return;
        }

        if (pressurebottom == null || TextUtils.isEmpty(pressurebottom)) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有输入压力下限哦！");
            return;
        }

        if (temperaturetop == null || TextUtils.isEmpty(temperaturetop)) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有输入温度上限哦！");
            return;
        }

        if (temperaturebottom == null || TextUtils.isEmpty(temperaturebottom)) {
            CommonUtils.showToast(TyreStorageActivity.this,"您还没有输入温度下限哦！");
            return;
        }

       /* if (sensor == null || TextUtils.isEmpty(sensor)) {
            CommonUtils.showToast(TyreNewActivity.this,"您还没有输入传感器编号哦！");
            return;
        }*/

        String brandItem = (String) mActivity_tyre_new_brand.getSelectedItem();      //轮胎品牌

        if (brandItem == null || TextUtils.isEmpty(brandItem)) {
            CommonUtils.showToast(TyreStorageActivity.this,"品牌请求失败，请重新进入此界面");
            return;
        }

        //下方是车队
        String  FleetNum = (String) mActivity_tyre_new_fleet.getSelectedItem();     //归属车队

        if (FleetNum == null || TextUtils.isEmpty(FleetNum)) {
            CommonUtils.showToast(TyreStorageActivity.this,"车队请求失败，请重新进入此界面");
            return;
        }


        Map<String, Map<String, String>> mbusCompany = CommonCompany.getCompany();          //将服务器中所有公司中的每个车队取出
        Map<String, String> buslist = mbusCompany.get(company);
        String fleetkey = getKey(buslist, FleetNum);

        String brandId = mBrandMap.get(brandItem);//轮胎品牌的ID


        Log.e("sfds",company+"    "+label+"   "+brandId+"   "+factorytime+"   "+replace+"   "+depth+"   "+pressuretop+"   "+pressurebottom+"   "+temperaturetop+"   "+temperaturebottom+"   "+fleetkey);

        OkGo.post(Net.ADDTYRE)
                .tag(this)
                .connTimeOut(15000)      // 设置当前请求的连接超时时间
                .readTimeOut(15000)      // 设置当前请求的读取超时时间
                .writeTimeOut(15000)
                .params(Constant.COMPANY, company)  //选择的公交公司 将这三个属性发送到服务器
                .params("id",label)                                     //账号
                .params("brandId",brandId)
                .params("start",factorytime)
                .params("end",replace)
                .params("figure_value",depth)   //花纹深度
                .params("pressure_ul",pressuretop)   //压力上限
                .params("pressure_ll",pressurebottom)//压力下限
                .params("temp_ul",temperaturetop)//温度上限
                .params("temp_ll",temperaturebottom)//温度下限
                .params("storeId",fleetkey)//车队
                .execute(new StringDialogCallback(TyreStorageActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        if (s.trim().equals("-1")) {
                            CommonUtils.showToast(TyreStorageActivity.this,"新增失败！");

                        }else {
                            CommonUtils.showToast(TyreStorageActivity.this,"成功新增！");
                            finish();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        CommonUtils.LongToast(TyreStorageActivity.this,"请求失败！服务器异常！");
                    }
                });
    }

/**
 * 初始化spinner数据
 * */
    private void initSpinner(){
        mActivity_tyre_new_brand = (Spinner) findViewById(R.id.activity_tyre_new_brand);


        mActivity_tyre_new_fleet = (Spinner) findViewById(R.id.activity_tyre_new_fleet);
    }
/**
 * 获取对应公司的车队
 * */
 /*   private void initFleet(){
        OkGo.post(Net.MAIN_QUERY_FLEET)
                .tag(this)
                .connTimeOut(15000)      // 设置当前请求的连接超时时间
                .readTimeOut(15000)      // 设置当前请求的读取超时时间
                .writeTimeOut(15000)
                .params("company",company)
                .execute(new StringDialogCallback(TyreStorageActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("company","dsadsafa");
                        displayList.clear();
                        mFleetList.clear();

                        if (s.trim().length() == 2) {
                            CommonUtils.showToast(TyreStorageActivity.this,"您登录的公司暂无车队信息");
                        }else if (s.trim().length() == 3){
                            String substring2 = s.substring(1, s.length() - 1);
                            mFleetList.add(substring2);
                        }else {

                            String substring = s.substring(1, s.length() - 1);
                            String[] split = substring.split(",");

                            for (String s1 : split) {
                                mFleetList.add(s1);                                                        //服务器返回车队的序号，将其封装到集合中
                            }

                        }
                        Map<String, Map<String, String>> mCompany = CommonCompany.getCompany();          //将服务器中所有公司中的每个车队取出
                        //       String BusCompany = CommonUtils.getString(TyreNewActivity.this, Constant.LOGIN_COMPANY);  //将登录时选择的公交公司取出
                        Map<String, String> displayMap = mCompany.get(company);                         //通过公交公司取出其对应的车队信息


//首先遍历从服务器获取的车队id，在根据id从本地封装的去取出对应的车队，将车队添加到要显示的集合中
                        for (String s2 : mFleetList) {                                                   //遍历服务器返回的车队序号，用公司的车队集合根据序号，取出车队名

                            String s3 = displayMap.get(s2);
                            if (s3 != null) {

                                displayList.add(s3);                                    //再将取出的车队名添加到要显示在界面上的集合中
                            }

                        }

                        arradp_fleet = new ArrayAdapter<String>(TyreStorageActivity.this,
                                android.R.layout.simple_spinner_item, displayList);
                        arradp_fleet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mActivity_tyre_new_fleet.setAdapter(arradp_fleet);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                        CommonUtils.showToast(TyreStorageActivity.this,"请求车队失败！！");
                    }
                });
    }*/

private void initBrand(){
    OkGo.post(Net.STORAGE_BRAND)
        .tag(this)
        .connTimeOut(15000)      // 设置当前请求的连接超时时间
        .readTimeOut(15000)      // 设置当前请求的读取超时时间
        .writeTimeOut(15000)
        .params("company",company)
        .execute(new StringDialogCallback(TyreStorageActivity.this) {
            @Override
            public void onSuccess(String s, Call call, Response response) {

                if (s.trim().length() == 2) {
                    CommonUtils.showToast(TyreStorageActivity.this,"您登录的公司暂无品牌信息");
                    return;
                }
                Gson gson = new Gson();

                try {
                    JSONArray array = new JSONArray(s);

//遍历全部的数组，每循环一次就是一个json对象
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);

                        StorageBrand storageBrand = gson.fromJson(jsonObject.toString(), StorageBrand.class);

                        if (!storageBrand.getBrandName().equals("泰盛")) {
                            mBrandMap.put(storageBrand.getBrandName(),storageBrand.getBrandId());
                            mmBrandList.add(storageBrand.getBrandName());
                        }

                    }

                    arradp_brand =  new ArrayAdapter<String>(TyreStorageActivity.this,
                            android.R.layout.simple_spinner_item, mmBrandList);
                    arradp_brand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mActivity_tyre_new_brand.setAdapter(arradp_brand);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Call call, Response response, Exception e) {
                super.onError(call, response, e);

                CommonUtils.showToast(TyreStorageActivity.this,"请求品牌失败！！");
            }
        });
}

    @Override
    protected void onDestroy() {
        super.onDestroy();

        OkGo.getInstance().cancelTag(this);
    }
/**
 * 通过集合的值获取对应的键
 * */
    private static String getKey(Map<String,String> map,String value){
        String key="";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(value.equals(entry.getValue())){
                key=entry.getKey();
            }
        }
        return key;
    }

}

