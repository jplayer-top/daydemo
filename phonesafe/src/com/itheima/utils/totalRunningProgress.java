package com.itheima.utils;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.text.format.Formatter;

public class totalRunningProgress {
	public static List<runProgressInfo> getRunProgress(Context context) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runAppInfos = am.getRunningAppProcesses();
		PackageManager pm = context.getPackageManager();
		List<runProgressInfo> Infos = new ArrayList<runProgressInfo>();
		for (RunningAppProcessInfo runAppInfo : runAppInfos) {
			runProgressInfo runpInfo = new runProgressInfo();
			String progressName = runAppInfo.processName;
			try {
				ApplicationInfo appInfo = pm.getApplicationInfo(progressName, 0);
				String appName = appInfo.loadLabel(pm).toString();
				
				Drawable appIcon = appInfo.loadIcon(pm);
				boolean systemApp = false;
				if((appInfo.flags&ApplicationInfo.FLAG_SYSTEM)!= 0){
					systemApp = true;
				}
				int pid = runAppInfo.pid;
				long memoryInfo = am.getProcessMemoryInfo(new int[]{pid})[0].getTotalPrivateDirty()*1024;
				String appSize = Formatter.formatFileSize(context, memoryInfo);
				runpInfo.setAppIcon(appIcon);
				runpInfo.setAppName(appName);
				runpInfo.setAppUseSize(memoryInfo);
				runpInfo.setSystem(systemApp);
				runpInfo.setPackageName(progressName);
				runpInfo.setChecked(false);
				Infos.add(runpInfo);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return Infos;
	}
}
