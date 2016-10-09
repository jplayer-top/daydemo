package com.itheima.activitys;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.widget.Toast;

import com.example.phonesafe.R;

public class smoketoast extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.smoketoast);
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> appProcressInfos = am
				.getRunningAppProcesses();
		int oldSize = appProcressInfos.size();
		for (RunningAppProcessInfo Info : appProcressInfos) {
			am.killBackgroundProcesses(Info.processName);
		}
		int newSize = am.getRunningAppProcesses().size();
		Toast.makeText(this, "清理了" + (oldSize - newSize) + "个进程", 0).show();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
