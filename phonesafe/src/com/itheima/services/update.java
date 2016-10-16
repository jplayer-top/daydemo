package com.itheima.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;

public class update extends Service {
	private Timer timer;
	private TimerTask task;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		// 10-13 12:50:23.688: E/AndroidRuntime(19138): Caused by:
		// android.os.NetworkOnMainThreadException 耗时的逻辑需要放在子线程中
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					URL url = new URL(
							"http://192.168.16.34:8080/updateantivious.json");
					HttpURLConnection connection = (HttpURLConnection) url
							.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					int code = connection.getResponseCode();
					if (code != 200) {
						System.out.println("链接失败");
						return;
					}
					InputStream is = connection.getInputStream();
					int len;
					byte[] buffer = new byte[1024];
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					while ((len = is.read(buffer)) != -1) {
						bos.write(buffer, 0, len);
					}
					String obj = new String(buffer);
					JSONObject json = new JSONObject(obj);
					String jsonmd5 = json.getString("md5");
					String jsonname = json.getString("name");
					int jsonversion = json.getInt("subcnt");
					String jsondesc = json.getString("desc");
					int jsontype = json.getInt("type");
					SQLiteDatabase db = SQLiteDatabase.openDatabase(
							update.this.getFilesDir() + "/antivirus.db", null,
							SQLiteDatabase.OPEN_READWRITE);
					Cursor cursor = db.rawQuery("select subcnt from version",
							null);
					if (cursor.moveToNext()) {
						int currentVersion = cursor.getInt(0);
						System.out.println(currentVersion);
						if (currentVersion == jsonversion) {
							System.out.println("版本一致");
							return;
						} else {
							ContentValues versionValues = new ContentValues();
							versionValues.put("subcnt", jsonversion);
							db.update("version", versionValues, null, null);

							ContentValues values = new ContentValues();
							values.put("type", jsontype);
							values.put("md5", jsonmd5);
							values.put("name", jsonname);
							values.put("desc", jsondesc);
							db.insert("datable", null, values);
							System.out.println("已经更新病毒库");
						}
					}
					cursor.close();
					System.out.println(jsonmd5);
					bos.close();
					is.close();

				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("mal");
				} catch (ProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("pro");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("IO");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("json");
				}

			}
		};
		timer.schedule(task, 0, 300000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
