package com.itheima.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;

import com.itheima.db.mySQLOpenHelper;
import com.itheima.utils.phonemode;

public class mySQL {
	private mySQLOpenHelper helper;

	public mySQL(Context context) {
		helper = new mySQLOpenHelper(context);
	}

	public boolean insert(String phone, String mode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("mode", mode);
		long raws = db.insert("blackphone", null, values);
		db.close();
		return raws != -1;
	}

	public void delete(String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		db.delete("blackphone", "phone=?", new String[] { phone });
		db.close();
	}

	public void update(String phone, String newmode) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("mode", newmode);
		db.update("blackphone", values, "phone=?", new String[] { phone });
		db.close();
	}

	public String select(String phone) {
		SQLiteDatabase db = helper.getWritableDatabase();
		String mode = null;
		Cursor cursor = db.query("blackphone", new String[] { "mode" },
				"phone=?", new String[] { phone }, null, null, null);
		while (cursor.moveToNext()) {
			mode = cursor.getString(0);
		}
		cursor.close();
		db.close();
		return mode;
	}

	public List<phonemode> find() {
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blackphone",
				new String[] { "phone", "mode" }, null, null, null, null, null);
		List<phonemode> list = new ArrayList<phonemode>();
		while (cursor.moveToNext()) {
			phonemode pm = new phonemode();
			String phone = cursor.getString(0);
			pm.setPhone(phone);
			String mode = cursor.getString(1);
			pm.setMode(mode);
			list.add(pm);
		}
		cursor.close();
		db.close();
		SystemClock.sleep(1000);
		return list;
	}
}
