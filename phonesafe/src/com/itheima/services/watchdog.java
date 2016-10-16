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
		System.out.println("������");
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
	 * ��ȡ������������Ļ�رտ������޷��������з���
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
							System.out.println("ƥ��");
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
	 * ����һ���㲥�����ߣ��������չ㲥���ж��Ƿ�����ڽ���
	 * 
	 * @author oblivion
	 * 
	 */
	class packagenameRecriver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// ������жϷ������ݵĹ㲥���ֻ���Ļ�رպ����ֿ�ָ���쳣�����������㲥��Ҫͬʱ��֤
			String action = intent.getAction();
			if ("com.android.watchdog.recriver".equals(action)) {
				okpackagename = intent.getStringExtra("packagename");
				System.out.println(okpackagename + "123123123");

			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				Log.i("eee", "��Ļ�����ˣ��������Ź����߳�");
				if (flag == false) {
					flag = true;
					startWatchDog();
				}
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				Log.i("eee", "��Ļ�����ˣ��رտ��Ź����߳�");
				flag = false;
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("����ȡ��");
		flag = false;
		// ���ܷ������߳��У���Ϊ�����´���һ���㲥�����ߣ�������쳣
		unregisterReceiver(receiver);
		receiver = null;
		getContentResolver().unregisterContentObserver(observer);
		observer = null;
	}
}
