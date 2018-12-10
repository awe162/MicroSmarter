package com.impinj1.microsmarter.activity;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.impinj1.microsmarter.R;
import com.impinj1.microsmarter.adapter.PoPuWindowLogin;
import com.impinj1.microsmarter.bean.BaseResponseModel;
import com.impinj1.microsmarter.bean.CompanyBean;
import com.impinj1.microsmarter.bean.CompanyLogin;
import com.impinj1.microsmarter.bean.LoginBean;
import com.impinj1.microsmarter.callback.StringDialogCallback;
import com.impinj1.microsmarter.constants.Constant;
import com.impinj1.microsmarter.constants.Net;
import com.impinj1.microsmarter.utils.CommonUtils;
import com.lzy.okgo.OkGo;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

import static android.R.attr.data;

/**
 * Created by Administrator on 2017/5/18.
 */

public class LoginActivity extends Activity implements View.OnClickListener {

    private EditText mLogin_name_et;        //账号输入框
    private EditText mLogin_password_et;    //密码输入框
    private String mName;                   // 账号
    private String mPassword;               //密码
    private Typeface TEXT_TYPE ;             // 第三方字体
    private Typeface TEXT_TYPE2 ;            // 第三方字体
    private TextView mActivity_login_tv;    //  生命周期管理系统字体
    private Button mLogin_bt;               //登录
    private PopupWindow mPopupWindow;       // 弹窗
    private ListView mPopuwindow_lv;
    private List<String> mList = new ArrayList<>();            //存放车队信息的集合
    private LinearLayout mLogin_transit_ll;
 //   private TextView mActivity_login_tv2;   // 英文logo

    private boolean flag;                   //用来判断是否是第一次进入，第一次进入联网获取公交公司不弹出泡泡窗口
    private RelativeLayout mActivity_login;
    private TextView mLogin_transit_tv;

    private PoPuWindowLogin mPoPuWindowLogin; //公交公司适配器对象
    private CheckBox mLogin_cb;              // 记住密码勾选框

    private Map<String,String> mMap = new HashMap<>();        //存放服务器返回的公交公司

    private boolean flagPopu;                //控制联网次数，防止多次联网

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//将activity全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        
     //  OkGo.init(new Application());
//初始化公交数据
        initTransit();
        CommonUtils.put(LoginActivity.this, Constant.EXPLAIN_FLEET,"");

//初始化公交map集合
       // iniTransitMap();
        // 初始化控件
        init();

     //初始化数据
        initData();


        }



    /**
     * 关闭软键盘
     */
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);

    }
    private void init(){
        mLogin_transit_tv = (TextView) findViewById(R.id.login_transit_tv);     //公交公司选择后显示的textview
        mLogin_name_et = (EditText) findViewById(R.id.login_name_et);
    mLogin_password_et = (EditText) findViewById(R.id.login_password_et);
        mActivity_login_tv = (TextView) findViewById(R.id.activity_login_tv);
        mActivity_login = (RelativeLayout) findViewById(R.id.activity_login);    //最外层相对布局
        mLogin_bt = (Button) findViewById(R.id.login_bt);
        mLogin_transit_ll = (LinearLayout) findViewById(R.id.login_transit_ll);
     //   mActivity_login_tv2 = (TextView) findViewById(R.id.activity_login_tv2);
        mLogin_cb = (CheckBox) findViewById(R.id.login_cb);
    }


   //初始化数据
    private void initData(){
   //使用第三方字体
        TEXT_TYPE = Typeface.createFromAsset(getAssets(),"front/fzzy_gbk.ttf");
        mActivity_login_tv.setTypeface(TEXT_TYPE);
        TEXT_TYPE2 = Typeface.createFromAsset(getAssets(),"front/New_York.ttf");
   //     mActivity_login_tv2.setTypeface(TEXT_TYPE2);
        mLogin_bt.setOnClickListener(this);
        mLogin_transit_ll.setOnClickListener(this);

   //进入此界面的时候取出上一次保存的账号，密码 显示在输入框上
        if (CommonUtils.getString(LoginActivity.this,Constant.LOGIN_USERNAME) != null && !TextUtils.isEmpty(CommonUtils.getString(LoginActivity.this,Constant.LOGIN_USERNAME))){
        mLogin_name_et.setText(CommonUtils.getString(LoginActivity.this,Constant.LOGIN_USERNAME));
//判断是否勾选记住密码
            if (CommonUtils.getBoolean(LoginActivity.this,Constant.LOGIN_CHECKBOX)) {
                mLogin_password_et.setText(CommonUtils.getString(LoginActivity.this,Constant.LOGIN_PASSWORD));
                mLogin_cb.setChecked(true);
            }else{
                mLogin_cb.setChecked(false);
            }


        }




     //控制输入的时候点击其他地方消失
        mActivity_login.setOnTouchListener(mTouchListener);
        mLogin_name_et.setOnFocusChangeListener(focusChangeListener);
        mLogin_password_et.setOnFocusChangeListener(focusChangeListener);


    }

    //输入框焦点监听，作用：用来配合点击外部区域收起小键盘的功能
    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            switch (v.getId()){
                case R.id.login_name_et:
                    if (mLogin_name_et.isFocused()){
                        mLogin_transit_ll.setClickable(false);
                    }else {
                        mLogin_transit_ll.setClickable(true);

                    }
                    break;

                case R.id.login_password_et:
                    if (mLogin_password_et.isFocused()){
                        mLogin_transit_ll.setClickable(false);
                    }else {
                        mLogin_transit_ll.setClickable(true);
                    }
                    break;
            }
        }
    };

   //外部区域的点击事件，作用：点击账号密码输入框的时候，点击外部区域时关闭小键盘退出输入状态
    View.OnTouchListener mTouchListener =  new View.OnTouchListener() {

        public boolean onTouch(View v, MotionEvent event) {
            // TODO Auto-generated method stub
            mActivity_login.setFocusable(true);
            mActivity_login.setFocusableInTouchMode(true);
            mActivity_login.requestFocus();
            /*switch (v.getId()){
                case R.id.login_name_et :
                    CommonUtils.showToast(LoginActivity.this,"账号被点击了");
                    closeKeybord(mLogin_name_et,LoginActivity.this);
                    break;
                case R.id.login_password_et :
                    CommonUtils.showToast(LoginActivity.this,"密码被点击了");
                    closeKeybord(mLogin_password_et,LoginActivity.this);
                    break;
            }*/

            return false;
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_bt:

    /*
    * 这一部分是逻辑判断
    *
    * */
                mName = mLogin_name_et.getText().toString().trim();
                mPassword = mLogin_password_et.getText().toString().trim();

        if (mLogin_transit_tv.getText().equals("公交公司选择")){

            CommonUtils.showToast(LoginActivity.this,"您还没有选择公交公司哦！");
            return;
        }

        if (mName == null || TextUtils.isEmpty(mName) || mPassword == null || TextUtils.isEmpty(mPassword)){
            CommonUtils.showToast(LoginActivity.this,"账号或密码不能为空哦！");
            return;
        }

  /*
  * 确认登录逻辑无误后开始将输入的参数携带请求服务器
  * */

                OkGo.post(Net.LOGIN_IP)
            .tag(this)
            .connTimeOut(15000)      // 设置当前请求的连接超时时间
            .readTimeOut(15000)      // 设置当前请求的读取超时时间
            .writeTimeOut(15000)
            .params("companyID",mMap.get(mLogin_transit_tv.getText().toString()))  //选择的公交公司 将这三个属性发送到服务器
            .params("username",mName)                                     //账号
            .params("password",mPassword)                             //密码
            .execute(new StringDialogCallback(LoginActivity.this) {
        @Override
        public void onSuccess(String s, Call call, Response response) {
            Type type = new TypeToken<BaseResponseModel<LoginBean>>() {
            }.getType();
            BaseResponseModel<LoginBean> model = new Gson().fromJson(s,type);

            if (model.getCode()!=0) {
                CommonUtils.showToast(LoginActivity.this,model.getMsg());
            }
            else if (model.getCode()==0) {
    //登录成功之后将登录的账号储存起来
                CommonUtils.put(LoginActivity.this,Constant.LOGIN_USERNAME,mName);

   // 首先判断记住密码是否勾选， 勾选才将密码储存,并且将勾选框的状态记录起来
                if (mLogin_cb.isChecked()) {
   //记住勾选框的状态，勾选状态存true ， 没有勾选存false
                    CommonUtils.put(LoginActivity.this,Constant.LOGIN_CHECKBOX,true);
                    CommonUtils.put(LoginActivity.this,Constant.LOGIN_PASSWORD,mPassword);
                }else{
                    CommonUtils.put(LoginActivity.this,Constant.LOGIN_CHECKBOX,false);
                }

//下面是登录成功的逻辑
                String mCompany = mMap.get(mLogin_transit_tv.getText().toString());   //选择的公交公司

                CommonUtils.put(LoginActivity.this,Constant.LOGIN_COMPANY_NAME,mLogin_transit_tv.getText().toString());

                CommonUtils.put(LoginActivity.this,Constant.LOGIN_COMPANY,mCompany);   //将选择的公交公司储存，方便内部取出
                CommonUtils.put(LoginActivity.this,Constant.EXTRA_COMPANY,mLogin_transit_tv.getText().toString());

                CommonUtils.showToast(LoginActivity.this,"成功登录！");
                CommonUtils.startActivity(LoginActivity.this,MyMainActivity.class);
                finish();
            }

        }

        @Override
        public void onError(Call call, Response response, Exception e) {
            super.onError(call, response, e);
            CommonUtils.showToast(LoginActivity.this,"登录失败，请检查您的网络！");
        }
        });

                break;
            case R.id.login_transit_ll :            //公交公司的点击事件
                flag = true;
                // 点击公交车的时候先去连接网络；

                if (!flagPopu){
                    initTransit();
                }else {
                    initPopWindow();
                }



                break;
        }

    }

    private void initPopWindow() {
        //得到PopupWindow的布局
        View contentView = LayoutInflater.from(getApplicationContext())
                .inflate(R.layout.item_popuwindow_login, null);
       // contentView.setBackgroundColor(Color.WHITE);
        //设置PopWindow的宽高
        mPopupWindow = new PopupWindow(findViewById(R.id.activity_login), ViewGroup.LayoutParams.WRAP_CONTENT, 530,true);
//        popOutShadow(mPopupWindow);
        mPopupWindow.setContentView(contentView);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopuwindow_lv = (ListView) contentView.findViewById(R.id.popuwindow_lv);
        mPopuwindow_lv.setOnItemClickListener(OnItemClickListener);

        if (mPoPuWindowLogin == null) {
            mPoPuWindowLogin = new PoPuWindowLogin(mList);
        }
        //向adapter装载数据
        mPopuwindow_lv.setAdapter(mPoPuWindowLogin);
        mPoPuWindowLogin.notifyDataSetChanged();

        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                if (mPopupWindow != null && mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                    mPopupWindow = null;

                }
                return false;
            }
        });
        mPopupWindow.showAsDropDown(mLogin_transit_ll);
    }
//公交车下拉条目的点击事件
    AdapterView.OnItemClickListener OnItemClickListener =  new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

 //从集合中取出点击的字符串，将点击的字符串设置在公交公司输入框上，并且关闭泡泡窗体
            String s = mList.get(position);
            mLogin_transit_tv.setText(s);
            mPopupWindow.dismiss();
        }
    };

//请求服务器公交车公司
    private void initTransit(){
        OkGo.post(Net.LOGIN_COMPANY)
                .tag(this)
                .connTimeOut(15000)      // 设置当前请求的连接超时时间
                .readTimeOut(15000)      // 设置当前请求的读取超时时间
                .writeTimeOut(15000)
                .execute(new StringDialogCallback(LoginActivity.this) {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        mList.clear();

                            /*JSONObject jsonObject = new JSONObject(s);

                            for (Iterator<?> iter = jsonObject.keys(); iter.hasNext(); ) {
                                String key = (String) iter.next();
                                String value = jsonObject.get(key).toString();

                                if (!key.equals("扬子公交")) {
                                    mList.add(key);
                                    mMap.put(key,value);
                                }



                            }*/
                            Gson gson = new Gson();
                            CompanyLogin companyLogin = gson.fromJson(s, CompanyLogin.class);
                        int code = companyLogin.getCode();
                        if (code!=0) {
                            CommonUtils.showToast(LoginActivity.this,companyLogin.getMsg());
                            return;
                        }
                        List<CompanyBean> data = companyLogin.getData();
                            for (int i = 0; i < data.size(); i++) {
                                CompanyBean companyBean = data.get(i);
                                int companyID = companyBean.getCompanyID();
                                String companyName = companyBean.getCompanyName();
                                mList.add(companyName);
                                mMap.put(companyName,companyID+"");
                            }
                            flagPopu = true;

                            if (flag == true) {
                                initPopWindow();
                            }



                       /* String substring = s.substring(1, s.length() - 1);
                        String[] split = substring.split(",");
                        for(String s2 : split){
                            String json = s2.substring(1, s2.length() - 1);
                            mList.add(json);
                        }*/
 //获取到服务器的公交公司后再打开泡泡窗口,进入此界面第一次联网时不弹出泡泡窗口，点击公交公司将flag改成true才能弹出


                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        CommonUtils.showToast(LoginActivity.this,"您的网络可能未开启，请检查！");
                    }
                });
    }


    //将公交公司对应的服务器的键值封装起来！
    private void iniTransitMap(){
    //    mMap = new HashMap<>();
        mMap.put("北京公交","beijing");
        mMap.put("深圳东部公交","shenzhendongbu");
        mMap.put("杭州公交","hangzhou");
        mMap.put("上海公交","shanghai");
        mMap.put("深圳西部公交","shenzhenxibu");
        mMap.put("郑州宇通公交","zhengzhouyutong");
        mMap.put("青岛公交","qingdao");
        mMap.put("山东烟台","yantai");
        mMap.put("富阳公交","fuyang");
        mMap.put("盐城公交","yancheng");
        mMap.put("西宁公交","xining");
        mMap.put("广州一汽","guangzhouyiqi");
        mMap.put("中国重型汽车集团","zhongqijituan");
        mMap.put("蚌埠公交","bengbu");
        mMap.put("廊坊公交","langfang");
        mMap.put("安阳公交","anyang");
   //     mMap.put("锦州公交","jinzhou");
        mMap.put("昆明公交","kunming");


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        OkGo.getInstance().cancelTag(this);

       // CommonUtils.put(LoginActivity.this,Constant.EXTRA_COMPANY,"");
    }


}
