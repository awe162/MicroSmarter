package com.impinj1.microsmarter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.impinj1.microsmarter.greendao.User;
import com.impinj1.microsmarter.greendao.UserDao;
import com.impinj1.rfidapi.InventoryEvent;
import com.impinj1.rfidapi.MacErrorEvent;
import com.impinj1.rfidapi.OnInventoryEventListener;
import com.impinj1.rfidapi.OnMacErrorEventListener;
import com.impinj1.rfidapi.RadioCtrl;
import com.impinj1.rfidapi.ctrlOperateResult;
import com.impinj1.rfidapi.radioBusyException;
import com.impinj1.rfidapi.radioFailException;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
	protected static final int MSG_SHOW_EPC_INFO = 1;
	protected static final int MSG_CONNECT_WAIT_SHOW = 2;
	protected static final int MSG_DISCONNECT_WAIT_SHOW = 3;
	protected static final int MSG_EXIT_APP = 4;

	public static final RadioCtrl myRadio = new RadioCtrl();
	private static ToggleButton buttonConnect = null;
	private static ToggleButton buttonInv = null;
	private static ProgressDialog prgDlg = null;
	public static TaglistFragment objFragment = null;
	public static TextView txtCount = null;
	public static TextView txtTimes = null;
	public static TabHost tabHost = null;
	public static TextView txtPowerBar = null;
	public static SeekBar powerScroll = null;
	private static int tagCount = 0;
	private static int tagTimes = 0;
	private static List<String> tagInfoList = new ArrayList<String>();
	private int exitcount = 1;
	// 声音播放
	private static SoundPool sp;
	// 声音播放模式
	public static final int RINGER_MODE_SILENT = 0;
	public static final int RINGER_MODE_VIBRATE = 1;
	public static final int RINGER_MODE_NORMAL = 2;
	private static AudioManager audio;
	private static UserDao mUserDao;


	static int flag;
	// private static final handle myHandle=new handle();
	/**
	 * 用于集中处理显示等事件信息的静态类
	 * 
	 * @author chenshanjing
	 * 
	 */
	static class StartHander extends Handler {
		WeakReference<Activity> mActivityRef;
		Activity activity;

		StartHander(Activity activity) {
			// 创建

			mActivityRef = new WeakReference<Activity>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			activity = mActivityRef.get();
			if (activity == null) {
				return;
			}

			switch (msg.what) {
			case MSG_SHOW_EPC_INFO:
				InventoryEvent info = (InventoryEvent) msg.obj;

				//Log.e("GetFlagID",info.GetFlagID());

				ShowEPC(activity, info.GetFlagID());
				
		
				// 播放
//				if (audio.getRingerMode() == RINGER_MODE_NORMAL) {
//					try {
//						sp.play(1, 1f, 1f, 0, 0, 1);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//				}

				break;

			case MSG_CONNECT_WAIT_SHOW:
				prgDlg.dismiss();
				ctrlOperateResult returnValue = (ctrlOperateResult) msg.obj;
				switch (returnValue) {
				case OK:
					Toast.makeText(activity,
							activity.getText(R.string.info_connect_success),
							Toast.LENGTH_SHORT).show();
					buttonInv.setClickable(true);
					powerScroll.setEnabled(false);
					break;

				case ERROR:
					Toast.makeText(activity,
							activity.getText(R.string.info_connect_fail),
							Toast.LENGTH_SHORT).show();
					buttonConnect.setChecked(false);
					buttonInv.setClickable(false);
					powerScroll.setEnabled(true);
					break;

				case SERIALPORTERROR:
					Toast.makeText(activity,
							activity.getText(R.string.info_antenna_fail),
							Toast.LENGTH_SHORT).show();
					buttonConnect.setChecked(false);
					buttonInv.setClickable(false);
					powerScroll.setEnabled(true);
					break;

				}

				break;

			case MSG_DISCONNECT_WAIT_SHOW:
				ctrlOperateResult returnValue1 = (ctrlOperateResult) msg.obj;
				switch (returnValue1) {
				case OK:
					Toast.makeText(activity,
							activity.getText(R.string.info_disconnect_success),
							Toast.LENGTH_SHORT).show();

					break;
				case ERROR:
					Toast.makeText(activity,
							activity.getText(R.string.info_disconnect_fail),
							Toast.LENGTH_SHORT).show();
					break;
				}
				break;
			case MSG_EXIT_APP:
				activity.finish();
				break;
			}
		}
	};

	private Handler hMsg = new StartHander(this);
	
//	 private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {  
//	        @Override  
//	        public void onReceive(final Context context, final Intent intent) {  
//	            final String action = intent.getAction();  
//	           if(Intent.ACTION_SCREEN_ON.equals(action)){  
//	                //Log.d(TAG, "-----------------screen is on...");  
//	           }else if(Intent.ACTION_SCREEN_OFF.equals(action)){  
//	        	   if (myRadio.IsInventory()) {
//	        		   buttonInv.setChecked(false);
//					}
//	   
//	           }  
//	        }  
//	    }; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); 
		setContentView(R.layout.activity_main);

/**
 * 进入此界面时初始化数据库，并且清空数据库
 * */
		mUserDao = MyApplication.getInstances().getDaoSession().getUserDao();
		mUserDao.deleteAll();

		audio = (AudioManager) this.getSystemService(this.AUDIO_SERVICE);

		sp = new SoundPool(10, 3, 5);
		try {
			sp.load(this, R.raw.popo, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		flag = 1;
//		 final IntentFilter filter = new IntentFilter();  
//	        filter.addAction(Intent.ACTION_SCREEN_OFF);  
//	        filter.addAction(Intent.ACTION_SCREEN_ON);  
//	        registerReceiver(mBatInfoReceiver, filter);  

		// 获取设备信息，串口和上电

		myRadio.setInventoryEventListener(new OnInventoryEventListener() {
			public void RadioInventory(InventoryEvent event) {
				Message msg = new Message();
				msg.obj = event;
				msg.what = MSG_SHOW_EPC_INFO;
				hMsg.sendMessage(msg);
			}
		});
		
		myRadio.setMacErrorEventListener(new OnMacErrorEventListener() {
			
			@Override
			public void RadioMacError(MacErrorEvent event) {
				MacErrorEvent mErrorEvent=event;
				if (mErrorEvent.GetErrorInfo()==0x309) {
					Toast.makeText(MainActivity.this, "出现0x309错误", Toast.LENGTH_LONG).show();
				}
				System.out.println("触发MacErrorEventListener  0x"+Integer.toHexString((int) mErrorEvent.GetErrorInfo())+"");
//				Toast.makeText(MainActivity.this, "触发MacErrorEventListener  0x"+Integer.toHexString((int) mErrorEvent.GetErrorInfo()), Toast.LENGTH_LONG).show();
			}
		});

		// 获取后续需要经常操作的窗口控件
		txtCount = (TextView) findViewById(R.id.txtCount);
		txtTimes = (TextView) findViewById(R.id.txtTimes);
		txtPowerBar = (TextView) findViewById(R.id.txtPowerBar);
		powerScroll = (SeekBar) findViewById(R.id.powerBar);
		buttonConnect = (ToggleButton) findViewById(R.id.togBtn_open);
		buttonInv = (ToggleButton) findViewById(R.id.togBtn_inv);
		final FragmentManager fragmentManager = getFragmentManager();
		objFragment = (TaglistFragment) fragmentManager
				.findFragmentById(R.id.fragment_taglist);
		tabHost = (TabHost) findViewById(R.id.tabhost);
		// 如果没有继承TabActivity时，通过该种方法加载启动tabHost
		tabHost.setup();
		tabHost.addTab(tabHost.newTabSpec("tab1")
				.setIndicator(getText(R.string.txt_powerOn_inventory))
				.setContent(R.id.firsttab));

		tabHost.addTab(tabHost.newTabSpec("tab3")
				.setIndicator(getText(R.string.txt_ReadWrite_tag))
				.setContent(R.id.secondtab));

		TabWidget tw = tabHost.getTabWidget();
		//tw.getChildAt(1).setVisibility(View.GONE);


		 buttonInv.setChecked(false);
		buttonConnect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				buttonConnect.setClickable(false);
				
				if (isChecked) {
					if (prgDlg != null) {
						prgDlg.show();
					} else {
						prgDlg = ProgressDialog.show(MainActivity.this, "连接设备",
								"正在连接设备，请稍后...", true, false);
					}
					// 开启一个线程来处理RFID设备的连接
					new Thread() {
						@Override
						public void run() {
							Message closemsg = new Message();

							ctrlOperateResult i = ctrlOperateResult.ERROR;
							try {
								// 连接设备
								Log.i("123", "1");
								i = myRadio.ConnectRadio(MainActivity.this,
										400, powerScroll.getProgress(), 4);
								System.out.println("-----连接返回参数："+i+"  -----");
							} catch (radioBusyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (radioFailException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							closemsg.obj = (Object) i;
							closemsg.what = MSG_CONNECT_WAIT_SHOW;
							hMsg.sendMessage(closemsg);
						}
					}.start();
					
				} else {
					Message closemsg = new Message();
					ctrlOperateResult i = ctrlOperateResult.ERROR;
					try {
						i = myRadio.DisconnectRadio();
					} catch (radioBusyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (i == ctrlOperateResult.OK) {
						buttonInv.setClickable(false);
					}
					closemsg.obj = (Object) i;
					closemsg.what = MSG_DISCONNECT_WAIT_SHOW;
					hMsg.sendMessage(closemsg);
				}
				powerScroll.setEnabled(true);
				buttonConnect.setClickable(true);
			}
		});

		// connectRadio();

		buttonInv.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				// TODO Auto-generated method stub
				buttonInv.setClickable(false);
				if (isChecked) {
					try {
						Thread.sleep(200);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					mUserDao.deleteAll();

					Inv();
				} else {
					stop();
				}

				buttonInv.setClickable(true);
			}
		});

		powerScroll.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				txtPowerBar.setText((progress) + "dBm");
				if (progress>23) {
				}else {
				}
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {       
            super.onConfigurationChanged(newConfig);     
  
    }    

	@Override
	public void finish() {
		if (exitcount == 2) {
			exitcount++;

			// 创建一个线程来处理关闭模块连接等相关的操作
			new Thread() {
				@Override
				public void run() {
					if (myRadio.IsInventory()) {
						myRadio.StopInventory();
					}
					try {
						Thread.sleep(200);
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					// 因为模块处于盘点等操作状态时，结束需要一小段时间，
					// 因此设计一个循环等待来确保模块断开操作的正常进行
					boolean waitOperEnd = false;
					do {
						waitOperEnd = false;
						try {
							myRadio.DisconnectRadio();
							myRadio.clearInventoryEventListener();
							Thread.sleep(200);
						} catch (radioBusyException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} while (waitOperEnd);
					if (prgDlg != null) {
						prgDlg.dismiss();
						} 
					prgDlg = null;
					hMsg.sendEmptyMessage(MSG_EXIT_APP);
					// myRadio.clearInventoryEventListener();
					sp.unload(1);
				}
			}.start();
//			unregisterReceiver(mBatInfoReceiver);
		} else if (exitcount == 1) {
			Toast.makeText(this, getText(R.string.app_exit), Toast.LENGTH_SHORT)
					.show();
			exitcount++;
		} else {
			// 其他状态表示收尾工作已经处理完成，可以正常退出程序
			super.finish();
		}
	}

	/**
	 * 显示搜索得到的标签信息
	 * 
	 * @param activity
	 * @param flagID
	 */
	public static void ShowEPC(Activity activity, String flagID) {


		flagID = flagID.toLowerCase();

		if (!tagInfoList.contains(flagID)) {
			tagCount++;

			Log.e("GetFlagID",flagID);

			User user=new User(null,flagID); //他需要一个id，
			// 这个需要id，通过这个id来标识你的数据的，你可以自己穿，
			// 如果传null，它会用它默认的id
			mUserDao.insert(user);//这是增加


			tagInfoList.add(flagID);
			objFragment.addItem(flagID);

			try {
				txtCount.setText(String.format("%d", tagCount));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		tagTimes++;
		try {
			txtTimes.setText(String.format("%d", tagTimes));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 开启盘点操作
	 */
	public void Inv() {
		try {
			tagInfoList.clear();
			tagCount = 0;
			tagTimes = 0;
			objFragment.clearItem();

			try {
				txtCount.setText(String.format("%d", tagCount));
			} catch (Exception e) {
				e.printStackTrace();
			}

			
				
			try {
				txtTimes.setText(String.format("%d", tagTimes));
			} catch (Exception e) {
				e.printStackTrace();
			}

			// 对于手持机只能使用周期性盘点，不然cpu资源将被耗尽
			if (myRadio.StartInventory(0, 500) == ctrlOperateResult.OK) {
				buttonConnect.setClickable(false);
				Toast.makeText(MainActivity.this,
						getText(R.string.info_inv_success), Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(MainActivity.this,
						getText(R.string.info_inv_fail), Toast.LENGTH_SHORT)
						.show();
			}
		} catch (radioBusyException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		prgDlg=null;

		if (buttonInv.isChecked()) {

			buttonInv.setClickable(false);

				stop();


			buttonInv.setClickable(true);


		}


		if (buttonConnect.isChecked()) {

			buttonConnect.setClickable(false);


				Message closemsg = new Message();
				ctrlOperateResult i = ctrlOperateResult.ERROR;
				try {
					i = myRadio.DisconnectRadio();
				} catch (radioBusyException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (i == ctrlOperateResult.OK) {
					buttonInv.setClickable(false);
				}
				closemsg.obj = (Object) i;
				closemsg.what = MSG_DISCONNECT_WAIT_SHOW;
				hMsg.sendMessage(closemsg);

			powerScroll.setEnabled(true);
			buttonConnect.setClickable(true);





		}

	}

	/**
	 * 停止盘点操作
	 */
	public void stop() {
		buttonConnect.setClickable(true);
		if (myRadio.StopInventory() == ctrlOperateResult.OK) {
			flag = 1;
			Toast.makeText(MainActivity.this,
					getText(R.string.info_stopinv_success), Toast.LENGTH_SHORT)
					.show();
		} else {
			Toast.makeText(MainActivity.this,
					getText(R.string.info_stopinv_fail), Toast.LENGTH_SHORT)
					.show();
		}
	}

}
