package com.itheima.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class addressDao {
	public static String whereAddress(Context context, String number) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase((context.getFilesDir()
				+ "/" + "address.db"), null, SQLiteDatabase.OPEN_READWRITE);
		String sql = "select location from data2 where id =(select outkey from data1 where id =?)";
		String location = "";
		String getNumber = "^1[3578]\\d{9}$";
		if (number.matches(getNumber)) {
			Cursor cursor = db.rawQuery(sql,
					new String[] { number.substring(0, 7) });
			if (cursor.moveToNext()) {
				location = cursor.getString(0);
			}
			cursor.close();
			return location;
		} else {
			return "²éÎÞ´ËºÅ";
		}
	}
}
