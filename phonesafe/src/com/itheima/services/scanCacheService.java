package com.itheima.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.os.IBinder;
import android.os.RemoteException;

public class scanCacheService extends Service {
	private SharedPreferences sp;
	private Timer timer;
	private TimerTask timertask;
	private PackageManager mpm;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		sp = getSharedPreferences("config", 0);
		mpm = getPackageManager();
		timer = new Timer();
		timertask = new TimerTask() {
			@Override
			public void run() {
				String packagename = sp.getString("packagename", "");
				Method[] methods = PackageManager.class.getDeclaredMethods();
				for (Method method : methods) {
					if ("getPackageSizeInfo".equals(method.getName())) {
						try {
							method.invoke(mpm, packagename,
									new IPackageStatsObserver.Stub() {

										@Override
										public void onGetStatsCompleted(
												PackageStats pStats,
												boolean succeeded)
												throws RemoteException {
											long cacheSize = pStats.cacheSize;
											System.out.println(cacheSize);
											if(cacheSize==0){
												Intent intent = new Intent();
												intent.setAction("com.android.findcachesizeiszero");
												intent.putExtra("position",sp.getInt("position", 0) );
												sendBroadcast(intent);
												stopSelf();
											}
										}
									});
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						return;
					}
				}

			}
		};
		timer.schedule(timertask, 0, 1000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		timertask.cancel();
		timer = null;
		timertask = null;
	}
}
