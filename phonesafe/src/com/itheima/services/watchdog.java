package com.itheima.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.itheima.activitys.enterPassword;
import com.itheima.db.dao.savepackagename;
import com.itheima.db.dao.savepackagename.appInfo;

public class watchdog extends Service {
	private boolean flag = true;
	private ActivityManager am;
	private List<RunningTaskInfo> taskInfos;
	private savepackagename lockSQL;
	private packagenameRecriver receiver;
	private String okpackagename = "";
	private myContextObserver observer;
	private List<appInfo> appInfos;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("服务开启");
		receiver = new packagenameRecriver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.android.watchdog.recriver");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);
		observer = new myContextObserver(new Handler());
		Uri uri = Uri
				.parse("content://com.itheima.db.dao.savepackagename.observer");
		getContentResolver().registerContentObserver(uri, true, observer);
		startWatchDog();
	}

	/**
	 * 抽取方法，方便屏幕关闭开启后无法正常运行服务
	 */
	private void startWatchDog() {
		new Thread() {
			public void run() {
				am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
				lockSQL = new savepackagename(getApplicationContext());
				Intent intent = new Intent(getApplicationContext(),
						enterPassword.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				appInfos = lockSQL.allFind();
				System.out.println(appInfos.get(0).toString() + "----");
				while (flag) {
					taskInfos = am.getRunningTasks(1);
					String packagename = taskInfos.get(0).topActivity
							.getPackageName().trim();
					SystemClock.sleep(1000);
					System.out.println(packagename);
					if (okpackagename.equals(packagename)) {
						continue;
					}
					for (appInfo appInfo : appInfos) {
						if (appInfo.toString().equals(packagename)) {
							System.out.println("匹配");
							intent.putExtra("packagename", packagename);
							startActivity(intent);
						}

					}

				}
			};
		}.start();
	}

	private class myContextObserver extends ContentObserver {

		public myContextObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			super.onChange(selfChange);
			appInfos = lockSQL.allFind();
		}

	}

	/**
	 * 创建一个广播接受者，用来接收广播，判断是否程序在进行
	 * 
	 * @author oblivion
	 * 
	 */
	class packagenameRecriver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 如果不判断发送数据的广播，手机屏幕关闭后会出现空指针异常。所以三个广播需要同时验证
			String action = intent.getAction();
			if ("com.android.watchdog.recriver".equals(action)) {
				okpackagename = intent.getStringExtra("packagename");
				System.out.println(okpackagename + "123123123");

			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				Log.i("eee", "屏幕激活了，开启看门狗子线程");
				if (flag == false) {
					flag = true;
					startWatchDog();
				}
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				Log.i("eee", "屏幕锁屏了，关闭看门狗子线程");
				flag = false;
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("服务取消");
		flag = false;
		// 不能放在子线程中，因为会重新创建一个广播接收者，会出现异常
		unregisterReceiver(receiver);
		receiver = null;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
	}
}
