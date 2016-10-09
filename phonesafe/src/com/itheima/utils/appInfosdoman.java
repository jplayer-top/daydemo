package com.itheima.utils;

import android.graphics.drawable.Drawable;

public class appInfosdoman {
	private long size;
	private String appname;
	private String packagename;
	private Drawable appicon;

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return "appInfosdoman [size=" + size + ", appname=" + appname
				+ ", packagename=" + packagename + "]";
	}

	public String getAppname() {
		return appname;
	}

	public void setAppname(String appname) {
		this.appname = appname;
	}

	public String getPackagename() {
		return packagename;
	}

	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}

	public Drawable getAppicon() {
		return appicon;
	}

	public void setAppicon(Drawable appicon) {
		this.appicon = appicon;
	}
}
