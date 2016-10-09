package com.itheima.broadcastReceiver;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class killprogress extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);

		List<RunningAppProcessInfo> appProcressInfos = am
				.getRunningAppProcesses();
		int oldSize = appProcressInfos.size();
		for (RunningAppProcessInfo Info : appProcressInfos) {
			am.killBackgroundProcesses(Info.processName);
		}
		int newSize = am.getRunningAppProcesses().size();
		Toast.makeText(context, "清理了" + (oldSize - newSize)+"个进程", 0).show();
	}
}
