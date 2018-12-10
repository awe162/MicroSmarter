package com.impinj1.microsmarter.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.impinj1.microsmarter.MainActivity;
import com.impinj1.microsmarter.R;


public class MyMainActivity extends TabActivity { // ActionBarActivity
	public static TabHost.TabSpec tab1, tab2;
	private TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
					| WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
					| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
					| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
			//底部导航栏的颜色
			window.setNavigationBarColor(Color.BLACK);
		}*/
		//StatusBarUtil.setTranslucentForImageViewInFragment(this,null);

		setContentView(R.layout.activity_mymain);
		initTabHost();
	}

	private void initTabHost(){

		mTabHost = getTabHost();

		tab1 = mTabHost
				.newTabSpec("tab1")
				.setIndicator("蓝牙连接",
						getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new Intent(this, MainActivity.class));
		mTabHost.addTab(tab1);

		tab2 = mTabHost
				.newTabSpec("tab2")
				.setIndicator(
						"轮胎设置",
						getResources().getDrawable(
								android.R.drawable.arrow_down_float))
				.setContent(new Intent(this, TyreActivity.class));
		mTabHost.addTab(tab2);



		mTabHost.setCurrentTab(0);

	}
}
