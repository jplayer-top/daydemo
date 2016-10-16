package com.itheima.activitys;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.example.phonesafe.R;
import com.itheima.services.callintercept;
import com.itheima.services.toastlocation;
import com.itheima.services.watchdog;
import com.itheima.ui.onOffImageButton;
import com.itheima.utils.ServicesNum;

public class settingActivity extends Activity {
	protected static final String TAG = "settingActivity";
	private onOffImageButton ib_setting_onoff;
	private SharedPreferences mSharedPreferences;
	private onOffImageButton ib_setting_callphone;
	private onOffImageButton ib_setting_location;
	private RelativeLayout rl_bg_location;
	private String[] items;
	private onOffImageButton ib_loack_app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_home);
		items = new String[] { "blue", "gray", "green", "orange" };
		final int[] background = new int[] { R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green,
				R.drawable.call_locate_orange };
		rl_bg_location = (RelativeLayout) findViewById(R.id.rl_bg_location);
		mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		rl_bg_location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(settingActivity.this);
				builder.setTitle("归属地风格");
				int checked = mSharedPreferences.getInt("bgwhich", 0);
				int bgitem = 0;
				for (int i = 0; i < background.length; i++) {
					if (background[i] == checked) {
						bgitem = i;
					}
				}
				Log.i("settingActivity", checked + "");
				builder.setSingleChoiceItems(items, bgitem,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								Editor editor = mSharedPreferences.edit();
								editor.putInt("bgwhich", background[which]);
								editor.commit();
								dialog.dismiss();
							}
						});
				builder.setNegativeButton("异常测试无需点击", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String s = null;
						System.out.println(s.equals(""));
					}
				});
				builder.show();
			}
		});
		ib_setting_location = (onOffImageButton) findViewById(R.id.ib_setting_location);
		ib_setting_onoff = (onOffImageButton) findViewById(R.id.ib_setting_onoff);
		ib_setting_callphone = (onOffImageButton) findViewById(R.id.ib_setting_callphone);
		mSharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
		ib_setting_onoff.setFlag(mSharedPreferences.getBoolean("flag", true));
		ib_setting_onoff.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean flag = ib_setting_onoff.changeFlag();
				Log.i(TAG, flag + "");
				Editor editor = mSharedPreferences.edit();
				editor.putBoolean("flag", flag);
				editor.commit();
			}
		});

		final Intent intent = new Intent(settingActivity.this,
				callintercept.class);
		boolean stateflag = ServicesNum.getServicesNum(settingActivity.this,
				"com.itheima.services.callintercept");
		ib_setting_callphone.setFlag(stateflag);
		Log.i("stateflag", stateflag + "");
		ib_setting_callphone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean flag = ib_setting_callphone.changeFlag();
				Log.i("flag", flag + "");
				if (flag) {
					startService(intent);
				} else {
					stopService(intent);
				}

			}
		});
		final Intent locationIntent = new Intent(settingActivity.this,
				toastlocation.class);
		boolean locationFlag = ServicesNum.getServicesNum(settingActivity.this,
				"com.itheima.services.toastlocation");
		ib_setting_location.setFlag(locationFlag);
		ib_setting_location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				ib_setting_location.changeFlag();
				boolean flag = ib_setting_location.getFlag();
				Log.i("flag", flag + "");
				if (flag) {
					startService(locationIntent);
				} else {
					stopService(locationIntent);
				}
			}
		});
		ib_loack_app = (onOffImageButton) findViewById(R.id.ib_loack_app);
		Intent bootIntent = new Intent();
		bootIntent.setAction(Intent.ACTION_BOOT_COMPLETED);
		// 注册开机广播
		ib_loack_app.setFlag(ServicesNum.getServicesNum(
				getApplicationContext(), "com.itheima.services.watchdog"));
		final Intent watchdogIntent = new Intent(getApplicationContext(),
				watchdog.class);
		ib_loack_app.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ib_loack_app.changeFlag();
				if (ib_loack_app.getFlag()) {
					startService(watchdogIntent);
				} else {
					stopService(watchdogIntent);
				}
			}
		});
	}

}
