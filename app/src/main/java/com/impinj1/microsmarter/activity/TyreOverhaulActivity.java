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
import android.widget.Spinner;
import android.widget.TextView;

import com.example.liangmutian.mypicker.DatePickerDialog;
import com.example.liangmutian.mypicker.DateUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.impinj1.microsmarter.R;
import com.impinj1.microsmarter.bean.BaseResponseModel;
import com.impinj1.microsmarter.callback.StringDialogCallback;
import com.impinj1.microsmarter.constants.Constant;
import com.impinj1.microsmarter.constants.Net;
import com.impinj1.microsmarter.utils.CommonUtils;
import com.lzy.okgo.OkGo;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.id;

/**
 * Created by Administrator on 2018/1/17 0017.
 */

public class TyreOverhaulActivity extends Activity {

    private String[] positionarr = {"左前轮","右前轮","左后外","左后内","右后内","右后外"};
    private Spinner mActivity_tyre_replace_position;
    private ArrayAdapter arradp_position;
    private EditText mActivity_tyre_replace_number;
    private LinearLayout mActivity_tyre_replace_replacetime_ll;
    //    private TextView mActivity_tyre_replace_replacetime;
    private EditText mActivity_tyre_replace_label;
    private Button mActivity_tyre_replace_add_bt;
    private EditText mActivity_tyre_replace_depth;
    //   private EditText mActivity_tyre_replace_sensor;
    //   private Spinner mActivity_tyre_replace_fleet;
    private String company;
    private List<String> mFleetList = new ArrayList<>();    //用来存放车队
    private List<String> displayList = new ArrayList<>();   //用来存放要显示的车队的中文名
    //   private ArrayAdapter arradp_fleet;

    private String mLabelId;                                    //标签名称
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        setContentView(R.layout.activity_tyre_overhaul);


        mLabelId = this.getIntent().getStringExtra(Constant.TYRE_NEW_ID);

        //公司名称
        company = CommonUtils.getString(TyreOverhaulActivity.this, Constant.LOGIN_COMPANY);

        initById();
        initSpinner();
        //  initFleet();

    }

    private void initById(){
        // mActivity_tyre_replace_sensor = (EditText) findViewById(R.id.activity_tyre_replace_sensor);

        mActivity_tyre_replace_depth = (EditText) findViewById(R.id.activity_tyre_replace_depth);
        mActivity_tyre_replace_depth.setText("5");

        ImageView mActivity_tyre_replace_title =  (ImageView) findViewById(R.id.activity_tyre_replace_fanhui);
        mActivity_tyre_replace_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mActivity_tyre_replace_add_bt = (Button) findViewById(R.id.activity_tyre_replace_add_bt);
        mActivity_tyre_replace_add_bt.setOnClickListener(mActivity_tyre_replace_add_btClickListener);
        mActivity_tyre_replace_label = (EditText) findViewById(R.id.activity_tyre_replace_Label);
        mActivity_tyre_replace_label.setText(mLabelId);
        mActivity_tyre_replace_number = (EditText) findViewById(R.id.activity_tyre_replace_number);
       /* mActivity_tyre_replace_replacetime_ll = (LinearLayout) findViewById(R.id.activity_tyre_replace_replacetime_ll);
        mActivity_tyre_replace_replacetime_ll.setOnClickListener(mActivity_tyre_replace_replacetime_llClickListener);*/
        //   mActivity_tyre_replace_replacetime = (TextView) findViewById(R.id.activity_tyre_replace_replacetime);


        mActivity_tyre_replace_label.setOnEditorActionListener(OnEditorActionListener);
    }

    TextView.OnEditorActionListener OnEditorActionListener = new TextView.OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId== EditorInfo.IME_ACTION_SEARCH||(event!=null&&event.getKeyCode()==KeyEvent.KEYCODE_ENTER)){
//点击软件盘搜索键，先将软键盘关闭。
                InputMethodManager systemService = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                systemService.hideSoftInputFromWindow(v.getWindowToken(),0);

                /*if (mActivity_tyre_replace_label.getText().toString().trim().length() != 8) {
                    CommonUtils.showToast(TyreReplaceActivity.this,"请输入8位数的轮胎编号！");

                }else {
                    IsStorage(mActivity_tyre_replace_label.getText().toString().trim(),false);
                }*/
            }
            return true;
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

    private void IsStorage(String id, final boolean flag){
        OkGo.post(Net.IS_STORAGE)
                .tag(this)
                .connTimeOut(5000)      // 设置当前请求的连接超时时间
                .readTimeOut(5000)      // 设置当前请求的读取超时时间
                .writeTimeOut(5000)
                .params(Constant.COMPANY,company)  //选择的公交公司 将这三个属性发送到服务器
                .params("id",id)                                     //账号
                .execute(new StringDialogCallback(TyreOverhaulActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.e("s",s.toString());
                        if (s.toString().equals("-1")) {


                            new AlertDialog.Builder(TyreOverhaulActivity.this)
                                    .setTitle("提示框")
                                    .setMessage("您输入的轮胎编号不存在，请先入库。")
                                    .setPositiveButton("确定",null)
                                    .setCancelable(false)
                                    .show();
                        }else{

                            if (flag) {
                                initStorage();
                            }else {
                                CommonUtils.LongToast(TyreOverhaulActivity.this,"搜索成功！请继续输入...");
                            }
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        CommonUtils.showToast(TyreOverhaulActivity.this,"请求服务器失败！！");
                    }
                });

    }

    private void initStorage(){
        String label = mActivity_tyre_replace_label.getText().toString().trim();   //标签id
     //   String number = mActivity_tyre_replace_number.getText().toString().trim();//车牌号
        //  String time = mActivity_tyre_replace_replacetime.getText().toString();    //更换时间
      //  String positionItem = (String) mActivity_tyre_replace_position.getSelectedItem();//轮胎位置
        String depth = mActivity_tyre_replace_depth.getText().toString().trim();   //花纹深度
        //     String sensor = mActivity_tyre_replace_sensor.getText().toString().trim(); //传感器编号
      /*if (label.length()!=8) {
          CommonUtils.LongToast(TyreReplaceActivity.this,"您的轮胎编号不正确,请写入8位编号！");
          return;
      }*/

       /* if (number == null || TextUtils.isEmpty(number)) {
            CommonUtils.showToast(TyreOverhaulActivity.this,"您还没有输入车载终端编号哦！");
            return;
        }*/
       /* if (number.length() != 7) {
            CommonUtils.LongToast(TyreOverhaulActivity.this,"请输入正确的7位数车牌号");
            return;
        }*/

     /* if (time.equals("选择卸载时间")) {
          CommonUtils.LongToast(TyreReplaceActivity.this,"您还没有选择卸载时间哦！");
          return;
      }*/

        if (depth == null || TextUtils.isEmpty(depth)) {
            CommonUtils.showToast(TyreOverhaulActivity.this,"您还没有输入花纹深度哦！");
            return;
        }

      /*  if (sensor == null || TextUtils.isEmpty(sensor)) {
            CommonUtils.showToast(TyreReplaceActivity.this,"您还没有输入传感器编号哦！");
            return;
        }*/

       /* int position = -1;
        switch (positionItem){

            case "左前轮" :
                position = 1;
                break;
            case "右前轮" :
                position = 2;
                break;
            case "左后外" :
                position = 3;
                break;
            case "左后内" :
                position = 4;
                break;
            case "右后内" :
                position = 5;
                break;
            case "右后外" :
                position = 6;
                break;

        }*/
        String company = CommonUtils.getString(TyreOverhaulActivity.this, Constant.LOGIN_COMPANY); //公司名称
        //下方是车队
        /*String  FleetNum = (String) mActivity_tyre_replace_fleet.getSelectedItem();     //归属车队
        Map<String, Map<String, String>> mbusCompany = CommonCompany.getCompany();          //将服务器中所有公司中的每个车队取出
        Map<String, String> buslist = mbusCompany.get(company);
        String fleetkey = getKey(buslist, FleetNum);*/

        OkGo.post(Net.OVERHAUL)
                .tag(this)
                .connTimeOut(5000)      // 设置当前请求的连接超时时间
                .readTimeOut(5000)      // 设置当前请求的读取超时时间
                .writeTimeOut(5000)
                //   .params(Constant.COMPANY,company)  //选择的公交公司 将这三个属性发送到服务器
                .params("factoryCode",label)                                     //账号
              //  .params("plateNO",number)  //车载终端编号
                //  .params("end",time)
             //   .params("place",position)
                .params("figureValue",depth)   //花纹深度
                //   .params("storeId",fleetkey)   //归属车队
                //   .params("sensor_no",sensor)     //传感器编号
                .execute(new StringDialogCallback(TyreOverhaulActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Type type = new TypeToken<BaseResponseModel<String>>() {
                        }.getType();
                        BaseResponseModel<String> model = new Gson().fromJson(s,type);

                        if (model.getCode()!=0) {
                            CommonUtils.LongToast(TyreOverhaulActivity.this,model.getMsg());
                        } else {

                            CommonUtils.LongToast(TyreOverhaulActivity.this,model.getMsg());
                            finish();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        CommonUtils.LongToast(TyreOverhaulActivity.this,"访问服务器失败！");
                    }
                });
    }
    /*
    * 确定修改的点击事件
    *
    * */
    View.OnClickListener mActivity_tyre_replace_add_btClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

       /* if (mActivity_tyre_replace_label.getText().toString().trim().length() != 8) {
            CommonUtils.showToast(TyreReplaceActivity.this,"请输入8位数的轮胎编号！");

        }else {
            IsStorage(mActivity_tyre_replace_label.getText().toString().trim(),true);
        }*/
            initStorage();
        }
    };

/*View.OnClickListener mActivity_tyre_replace_replacetime_llClickListener = new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDialog(DateUtil.getDateForString(getCurrentData()),mActivity_tyre_replace_replacetime);
    }
};*/


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
        builder.setOnDateSelectedListener(new DatePickerDialog.OnDateSelectedListener() {
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
    private void initSpinner(){
        mActivity_tyre_replace_position = (Spinner) findViewById(R.id.activity_tyre_replace_position);
        arradp_position = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, positionarr);
        arradp_position.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mActivity_tyre_replace_position.setAdapter(arradp_position);


        //   mActivity_tyre_replace_fleet = (Spinner) findViewById(R.id.activity_tyre_replace_fleet);
    }

/*private void initFleet(){
    OkGo.post(Net.MAIN_QUERY_FLEET)
            .tag(this)
            .connTimeOut(5000)      // 设置当前请求的连接超时时间
            .readTimeOut(5000)      // 设置当前请求的读取超时时间
            .writeTimeOut(5000)
            .params("company",company)
            .execute(new StringDialogCallback(TyreReplaceActivity.this) {
                @Override
                public void onSuccess(String s, Call call, Response response) {
                    Log.e("company","dsadsafa");
                    displayList.clear();
                    mFleetList.clear();

                    if (s.trim().length() == 2) {
                        CommonUtils.showToast(TyreReplaceActivity.this,"您登录的公司暂无车队信息");
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
                        if (s3!=null) {
                            displayList.add(s3);                                    //再将取出的车队名添加到要显示在界面上的集合中
                        }

                    }

                   *//* arradp_fleet = new ArrayAdapter<String>(TyreReplaceActivity.this,
                            android.R.layout.simple_spinner_item, displayList);
                    arradp_fleet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    mActivity_tyre_replace_fleet.setAdapter(arradp_fleet);*//*
                }

                @Override
                public void onError(Call call, Response response, Exception e) {
                    super.onError(call, response, e);

                    CommonUtils.showToast(TyreReplaceActivity.this,"请求车队失败！！");
                }
            });
}*/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        OkGo.getInstance().cancelTag(this);
    }

    private static String getKey(Map<String,String> map, String value){
        String key="";
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(value.equals(entry.getValue())){
                key=entry.getKey();
            }
        }
        return key;
    }
}
