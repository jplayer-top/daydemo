package com.itheima.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mySQLOpenHelper extends SQLiteOpenHelper {

	public mySQLOpenHelper(Context context) {
		super(context, "blackphone.db", null, 1);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create  table blackphone (_id integer primary key autoincrement,phone varchar(20),mode varchar(10))");
		// db.execSQL("create  table packagename (_id integer primary key autoincrement,phone varchar(20),mode varchar(10))");
		db.execSQL("create table allpackagename (_id integer primary key autoincrement, packagename varchar(40) )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
