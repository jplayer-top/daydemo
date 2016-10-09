package com.itheima.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Environment;
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
			appInfosdoman appInfo = new appInfosdoman();
			appInfo.setAppicon(appicon);
			appInfo.setAppname(appname);
			appInfo.setPackagename(pacakgename);
			appInfo.setSize(size);
			appInfos.add(appInfo);
			System.out.println(appInfo.toString());
		}
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
