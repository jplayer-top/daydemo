package com.itheima.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;

public class getPhoneNum {
	public static List<phones> phoneContacts(Context context) {
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
		Uri datauri = Uri.parse("content://com.android.contacts/data");
		Cursor cursor = resolver.query(uri, new String[] { "contact_id" },
				null, null, null);
		List<phones> list = new ArrayList<phones>();
		while (cursor.moveToNext()) {
			String id = cursor.getString(0);
			Cursor datacursor = resolver
					.query(datauri, new String[] { "data1", "mimetype" },
							"raw_contact_id=?", new String[] { id }, null);
			Log.i("getPhoneNum", id);
			if (id != null) {
				phones phone = new phones();
				while (datacursor.moveToNext()) {
					String data1 = datacursor.getString(0);
					String mimetype = datacursor.getString(1);
					if (mimetype.equals(
							"vnd.android.cursor.item/phone_v2")) {
						phone.setPhone(data1);
					} else if (datacursor.getString(1).equals(
							"vnd.android.cursor.item/name")) {
						phone.setName(data1);
					}
				}
				datacursor.close();
				list.add(phone);
			}
		}
		Log.i("getPhoneNum", list.toString());
		cursor.close();
		SystemClock.sleep(10000);
		return list;
	}
}
