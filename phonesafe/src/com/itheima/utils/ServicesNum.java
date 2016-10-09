package com.itheima.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServicesNum {
	public static boolean getServicesNum(Context context, String serviceinfo) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> infos = am.getRunningServices(200);
		for (RunningServiceInfo info : infos) {
			if (serviceinfo.equals(info.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
