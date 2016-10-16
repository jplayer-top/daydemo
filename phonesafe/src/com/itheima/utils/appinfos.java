package com.itheima.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.SystemClock;
import android.text.format.Formatter;

public class appinfos {
	public static List<appInfosdoman> getAppInfos(Context context) {
		PackageManager packagemanager = context.getPackageManager();
		List<ApplicationInfo> applicationsInfos = packagemanager
				.getInstalledApplications(0);
		List<appInfosdoman> appInfos = new ArrayList<appInfosdoman>();
		for (ApplicationInfo applicationInfo : applicationsInfos) {
			long size = new File(applicationInfo.sourceDir).length();
			String pacakgename = applicationInfo.packageName;
			String appname = applicationInfo.loadLabel(packagemanager)
					.toString();
			Drawable appicon = applicationInfo.loadIcon(packagemanager);
			int flag = applicationInfo.flags;
			appInfosdoman appInfo = new appInfosdoman();
			if ((flag & ApplicationInfo.FLAG_SYSTEM) != 0) {
				appInfo.setSystemApp(true);
			} else {
				appInfo.setSystemApp(false);
			}
			if ((flag & ApplicationInfo.FLAG_EXTERNAL_STORAGE) != 0) {
				appInfo.setEXetary(true);
			} else {
				appInfo.setEXetary(false);
			}
			appInfo.setAppicon(appicon);
			appInfo.setAppname(appname);
			appInfo.setPackagename(pacakgename);
			appInfo.setSize(size);
			appInfos.add(appInfo);
		}
		SystemClock.sleep(2000);
		return appInfos;
	}

	public static String getRam(Context context) {
		long ram = Environment.getDataDirectory().getFreeSpace();
		return "机身内部存储:" + Formatter.formatFileSize(context, ram);
	}

	public static String getRom(Context context) {
		long rom = Environment.getExternalStorageDirectory().getFreeSpace();
		return "机身外部存储:" + Formatter.formatFileSize(context, rom);
	}
}
