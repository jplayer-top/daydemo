package com.itheima.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.SystemClock;
import android.util.Log;
import android.util.Xml;

public class smsbackuptool {
	public interface callBackUp {
		public void setMaxProgress(int max);

		public void setCurrentProgress(int progress);
	}

	public static void setSmsInfo(Context context, callBackUp backUp) {
		Uri uri = Uri.parse("content://sms/");
		ContentResolver resolver = context.getContentResolver();
		resolver.delete(uri, null, null);
		XmlPullParser pullparser = Xml.newPullParser();
		File file = new File(context.getFilesDir(), "smsbackup.xml");
		try {
			if (file.exists()) {
				InputStream is = new FileInputStream(file);
				pullparser.setInput(is, "utf-8");
				int type = pullparser.getEventType();
				ContentValues values = null;
				int progress = 0;
				while (type != XmlPullParser.END_DOCUMENT) {
					switch (type) {
					case XmlPullParser.START_TAG:
						if (pullparser.getName().equals("items")) {
							int max = Integer.parseInt(pullparser.nextText());
							Log.i("smsbackuptool", max + "");
							backUp.setMaxProgress(max);
						}
						if (pullparser.getName().equals("info")) {
							values = new ContentValues();
						}
						if (pullparser.getName().equals("address")) {
							values.put("address", pullparser.nextText());
						} else if (pullparser.getName().equals("body")) {
							values.put("body", pullparser.nextText());
						} else if (pullparser.getName().equals("type")) {
							values.put("type", pullparser.nextText());
						} else if (pullparser.getName().equals("date")) {
							values.put("date", pullparser.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						if (pullparser.getName().equals("info")) {
							resolver.insert(uri, values);
							progress++;
							backUp.setCurrentProgress(progress);
							SystemClock.sleep(1000);
						}
						break;
					}
					type = pullparser.next();
				}
				is.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void getSmsInfos(final Context context,
			final callBackUp backUp) {

		Uri uri = Uri.parse("content://sms/");
		ContentResolver resolver = context.getContentResolver();
		try {
			Cursor cursor = resolver.query(uri, new String[] { "address",
					"body", "type", "date" }, null, null, null);
			XmlSerializer serializer = Xml.newSerializer();
			File file = new File(context.getFilesDir(), "smsbackup.xml");
			OutputStream os = new FileOutputStream(file);
			serializer.setFeature(
					"http://xmlpull.org/v1/doc/features.html#indent-output",
					true);
			serializer.setOutput(os, "utf-8");
			serializer.startDocument("utf-8", true);
			serializer.startTag(null, "infos");
			serializer.startTag(null, "items");
			serializer.text(cursor.getCount() + "");
			serializer.endTag(null, "items");
			backUp.setMaxProgress(cursor.getCount());
			int progress = 0;
			while (cursor.moveToNext()) {
				serializer.startTag(null, "info");
				String address = cursor.getString(0);
				serializer.startTag(null, "address");
				serializer.text(address);
				serializer.endTag(null, "address");
				String body = cursor.getString(1);
				Log.i("body", body);
				serializer.startTag(null, "body");
				serializer.text(body);
				serializer.endTag(null, "body");
				String type = cursor.getString(2);
				serializer.startTag(null, "type");
				serializer.text(type);
				serializer.endTag(null, "type");
				String date = cursor.getString(3);
				serializer.startTag(null, "date");
				serializer.text(date);
				serializer.endTag(null, "date");
				serializer.endTag(null, "info");
				backUp.setCurrentProgress(progress);
				SystemClock.sleep(1000);
				progress++;
			}
			serializer.endTag(null, "infos");
			serializer.endDocument();
			os.close();
			cursor.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
