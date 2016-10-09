package com.itheima.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.test.suitebuilder.annotation.Smoke;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.activitys.smoketoast;

public class rockettosat extends Service {
	private WindowManager wm;
	private WindowManager.LayoutParams params;
	private ImageView imageView;
	private Handler handler = new Handler();

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("rockettoast", "服务被开启");
		imageView = new ImageView(this);
		imageView.setBackgroundResource(R.drawable.rocket);
		AnimationDrawable rocketAnimation = (AnimationDrawable) imageView.getBackground();
		rocketAnimation.start();
		imageView.setOnTouchListener(new OnTouchListener() {
			int startX;
			int startY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					if (params.x > 150 && params.x < 250 && params.y > 300) {
						Intent intent = new Intent(
								getApplicationContext(),
								smoketoast.class);
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);

						new Thread() {
							public void run() {
								for (int i = 0; i < 15; i++) {
									params.y -= 20;
									SystemClock.sleep(50);
									handler.post(new Runnable() {
										@Override
										public void run() {
											// TODO Auto-generated method stub
											wm.updateViewLayout(imageView,
													params);
										}
									});
									if (i == 14) {
										ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
										List<RunningAppProcessInfo> appProcressInfos = am
												.getRunningAppProcesses();
										for (RunningAppProcessInfo Info : appProcressInfos) {
											am.killBackgroundProcesses(Info.processName);
											am.killBackgroundProcesses(smoketoast.class
													.getPackage().getName());
										}
									}
								}
							}
						}.start();
					}
					break;
				case MotionEvent.ACTION_MOVE:
					int newX = (int) event.getRawX();
					int newY = (int) event.getRawY();
					params.x += (newX - startX);
					params.y += (newY - startY);
					wm.updateViewLayout(imageView, params);
					startX = newX;
					startY = newY;
					if (params.x > 150 && params.x < 250 && params.y > 300) {
						Toast.makeText(rockettosat.this, "我要射了", 0).show();
					}
					break;
				}
				return true;
			}
		});
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		params = new LayoutParams();
		params.gravity = Gravity.TOP + Gravity.LEFT;
		params.x = 20;
		params.y = 20;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;
		wm.addView(imageView, params);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("rockettoast", "服务被关闭");
		wm.removeView(imageView);
		imageView = null;
	}
}
