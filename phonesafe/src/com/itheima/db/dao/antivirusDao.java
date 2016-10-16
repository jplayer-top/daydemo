package com.itheima.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class antivirusDao {
	public static String getAntivirusDao(Context context, String md5) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(context.getFilesDir()
				+ "/antivirus.db", null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = db.rawQuery("select desc from datable where md5 = ?",
				new String[] { md5 });
		String desc = "";
		if (cursor.moveToNext()) {
			desc = cursor.getString(0);
		}
		return desc;
	}
}
