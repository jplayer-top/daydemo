package com.itheima.utils;

import android.graphics.drawable.Drawable;

public class runProgressInfo {
	private String appName;
	private String packageName;
	private Drawable appIcon;
	private long appUseSize;
	private boolean isChecked;
	private boolean isSystem;
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	@Override
	public String toString() {
		return "runProgressInfo [appName=" + appName + ", packageName="
				+ packageName + ", appUseSize=" + appUseSize + ", isChecked="
				+ isChecked + ", isSystem=" + isSystem + "]";
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public long getAppUseSize() {
		return appUseSize;
	}
	public void setAppUseSize(long appUseSize) {
		this.appUseSize = appUseSize;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public boolean isSystem() {
		return isSystem;
	}
	public void setSystem(boolean isSystem) {
		this.isSystem = isSystem;
	}

}
