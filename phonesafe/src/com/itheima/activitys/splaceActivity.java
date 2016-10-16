package com.itheima.activitys;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.widget.TextView;

import com.example.phonesafe.R;
import com.itheima.services.update;
import com.itheima.utils.StreamOutput;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

public class splaceActivity extends Activity {
	public static final String TAG = "splaceActivity";
	public static final int UNEQUAL = 2;
	public static final int ERROR = 3;
	private TextView mTextView;
	private String versionName;
	private long startTime;
	private long endTime;
	// handler �߳��з������ݸ���UI
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UNEQUAL:
				String urlpath = (String) msg.obj;
				downloadVwesion(urlpath);
				break;
			case ERROR:
				loadHomeActivity();
				break;
			}
		}

	};

	/**
	 * ����Դ�ļ��е��ļ����뵽�ֻ�������
	 * 
	 * @param path
	 *            �ļ��ĵ�ַ ������assets��ֻ��д�ļ����Ϳ��ԣ����� path = "address.db"
	 */
	private void createFile(String path) {
		try {
			File file = new File(getFilesDir(), path);
			if (!file.exists()) {
				InputStream is = getAssets().open(path);
				OutputStream os = new FileOutputStream(file);
				int len;
				byte[] buffer = new byte[1024];
				while ((len = is.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				os.close();
				is.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		// ��ʱ���߼��������߳��У���������û��Ҫ�������������߳���
		//�����������Զ����µ�service
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				createFile("address.db");
				createFile("antivirus.db");
				Intent intent = new Intent();
				intent.setClass(getApplicationContext(), update.class);
				startService(intent);
			}
		});
		mTextView = (TextView) findViewById(R.id.tv_splash_version);
		PackageInfo packageinfo;
		try {
			// ��ȡ����Ϣ��������ȡ���İ汾�źͰ汾��Ϣ�ȵ�
			packageinfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
			versionName = packageinfo.versionName;
			mTextView.setText("�汾�ţ�" + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// �������ж��Ƿ��ڳ�ʼ��������³���
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean flag = sp.getBoolean("flag", true);
		if (flag) {
			new Thread(new myRunable()).start();
		} else {
			//ѡ�񲻸��µĻ��ͻ�ֱ�ӵ������ӽ���������
			new Thread() {
				public void run() {
					SystemClock.sleep(2000);
					loadHomeActivity();
				};
			}.start();
		}

	}

	/**
	 * �汾��һ��ѡ������°汾
	 * 
	 * @param urlpath
	 *            json�ļ����������ļ���ַ
	 */
	private void downloadVwesion(final String urlpath) {
		AlertDialog.Builder builder = new Builder(splaceActivity.this);
		builder.setCancelable(false);
		builder.setTitle("update");
		builder.setMessage("update version");
		final ProgressDialog pd = new ProgressDialog(splaceActivity.this);
		pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		pd.setCancelable(false);
		pd.show();
		builder.setPositiveButton("ok", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "ok");
				HttpUtils http = new HttpUtils();
				final File file = new File(Environment
						.getExternalStorageDirectory(), System
						.currentTimeMillis() + ".apk");
				http.download(urlpath, file.getAbsolutePath(),
						new RequestCallBack<File>() {

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								Log.i(TAG, "success");
								/*
								 * <intent-filter> <action
								 * android:name="android.intent.action.VIEW" />
								 * <category
								 * android:name="android.intent.category.DEFAULT"
								 * /> <data android:scheme="content" /> <data
								 * android:scheme="file" /> <data
								 * android:mimeType
								 * ="application/vnd.android.package-archive" />
								 * </intent-filter>
								 */
								// �ϲ�Դ���еİ�װ��Ӧ�ó������ʿ��ͼ
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW")
										.addCategory(
												"android.intent.category.DEFAULT")
										.setDataAndType(Uri.fromFile(file),
												"application/vnd.android.package-archive");
								startActivity(intent);
								pd.dismiss();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								super.onLoading(total, current, isUploading);
								pd.setMax(100);
								int progress = (int) ((current / total) * 100);
								pd.setProgress(progress);
							}

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								Log.i(TAG, "error");
								pd.dismiss();
							}
						});
			}
		});
		builder.setNegativeButton("cancle", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Log.i(TAG, "no");
				loadHomeActivity();
			}
		});
		builder.show();
	};

	public class myRunable implements Runnable {

		public void run() {
			Message msg = Message.obtain();
			try {
				startTime = System.currentTimeMillis();
				// ��ȡ��ǰ�趨��URL
				URL url = new URL(getResources().getString(R.string.url));
				// ������
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// �趨ʱ����GET����
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				// �ͻ�ȡ״̬��
				int code = conn.getResponseCode();
				if (code == 200) {
					// ͨ����ҳԴ�����������ȡjson���ݲ��ҽ����ݽ���
					InputStream is = conn.getInputStream();
					String result = StreamOutput.getJsonString(is);
					JSONObject jsonObject = new JSONObject(result);
					String serviceVersion = jsonObject.getString("version");
					String urlpath = jsonObject.getString("load");
					Log.i(TAG, urlpath);
					Log.i(TAG, serviceVersion);
					if (versionName.equals(serviceVersion)) {
						Log.i(TAG, "�汾һ��");
						//ѡ���Զ����º�����汾һ��ֱ��˯3000�����������
						SystemClock.sleep(3000);
						loadHomeActivity();
					} else {
						Log.i(TAG, "�汾��һ�������");

						msg.what = UNEQUAL;
						msg.obj = urlpath;

					}
				} else {
					Log.i(TAG, "��ȡʧ��");

					msg.what = ERROR;
				}

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

				msg.what = ERROR;

			} catch (NotFoundException e) {
				e.printStackTrace();

			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				// ���׽���ͣ����ʱ��
				endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				if (time < 3000) {
					//�汾��һ������˯������
					SystemClock.sleep(3000 - time);
				} else {
				}
				//���ѡ������Ϣ�������ܹ�ȷ����Ϣ����ʱ��ȴﵽ����Ҫ�ȴ���ʱ��
				handler.sendMessage(msg);
			}

		}
	}

	/**
	 * ����home����
	 */
	public void loadHomeActivity() {
		Intent intent = new Intent();
		intent.setClass(splaceActivity.this, homeActivity.class);
		startActivity(intent);
		finish();
	}
}
