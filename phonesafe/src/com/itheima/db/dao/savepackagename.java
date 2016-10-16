package com.itheima.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.itheima.db.mySQLOpenHelper;

public class savepackagename {
	private mySQLOpenHelper helper;
	private Context context;
	public savepackagename(Context context) {
		super();
		helper = new mySQLOpenHelper(context);
		this.context = context;
	}

	public void add(String packagename) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packagename", packagename);
		db.insert("allpackagename", null, values);
		Uri uri = Uri.parse("content://com.itheima.db.dao.savepackagename.observer");
		context.getContentResolver().notifyChange(uri, null);
	}

	public void delete(String packagename) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("allpackagename", "packagename = ?", new String[] { packagename });
	}

	public boolean find(String packagename) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("allpackagename", null, "packagename = ?",
				new String[] { packagename }, null, null, null);
		if (cursor.moveToNext()) {
			cursor.close();
			return true;
		}
		cursor.close();
		return false;
	}

	public List<appInfo> allFind() {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.query("allpackagename", null, null, null, null, null,
				null);
		List<appInfo> Infos = new ArrayList<appInfo>();
		while (cursor.moveToNext()) {
			appInfo Info = new appInfo();
			String packagename = cursor.getString(1).trim();
			Info.packagename = packagename;
			Infos.add(Info);
		}
		
		return Infos;
	}

	public class appInfo {
		String packagename;

		@Override
		public String toString() {
			return packagename;
		}
		
	}
}
