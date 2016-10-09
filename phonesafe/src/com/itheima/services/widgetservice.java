package com.itheima.services;

import java.util.Timer;
import java.util.TimerTask;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.format.Formatter;
import android.widget.RemoteViews;

import com.example.phonesafe.R;
import com.itheima.broadcastReceiver.killprogress;
import com.itheima.broadcastReceiver.phonesafewidget;

public class widgetservice extends Service {
	private Timer timer;
	private TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				AppWidgetManager awm = AppWidgetManager
						.getInstance(getApplicationContext());
				RemoteViews views = new RemoteViews(getPackageName(),
						R.layout.process_widget);
				views.setTextViewText(R.id.process_count, "正在运行的进程数"
						+ getRunningProgress(getApplicationContext()));
				views.setTextViewText(R.id.process_memory, "手机运行剩余内存"
						+ getMemoSize(getApplicationContext()));
				Intent intent = new Intent(getApplicationContext(),
						killprogress.class);
				intent.setAction("com.itheima.broadcastReceiver.killprogress");
				PendingIntent pendingIntent = PendingIntent.getBroadcast(
						getApplicationContext(), 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				views.setOnClickPendingIntent(R.id.btn_clear, pendingIntent);
				ComponentName provider = new ComponentName(
						getApplicationContext(), phonesafewidget.class);
				awm.updateAppWidget(provider, views);
			}
		};
		timer.schedule(task, 0, 2000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		timer.cancel();
		task.cancel();
		timer = null;
		task = null;
	}

	public static String getRunningProgress(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
		int size = am.getRunningAppProcesses().size();
		return size + "";
	}

	public static String getMemoSize(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		String size = Formatter.formatFileSize(context, outInfo.availMem);
		return size;
	}
}
