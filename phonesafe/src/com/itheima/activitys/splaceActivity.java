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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.phonesafe.R;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_activity);
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					File file = new File(getFilesDir(), "address.db");
					if (!file.exists()) {

						InputStream is = getAssets().open("address.db");
						int len;
						byte[] buffer = new byte[1024];
						OutputStream os = new FileOutputStream(file);
						while ((len = is.read(buffer)) != -1) {
							os.write(buffer, 0, len);
						}
						os.close();
						is.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
			}
		});
		mTextView = (TextView) findViewById(R.id.tv_splash_version);
		PackageInfo packageinfo;
		try {
			packageinfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
			versionName = packageinfo.versionName;
			mTextView.setText("版本号：" + versionName);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean flag = sp.getBoolean("flag", true);
		if (flag) {
			new Thread(new myRunable()).start();
		} else {
			new Thread() {
				public void run() {
					SystemClock.sleep(2000);
					loadHomeActivity();
				};
			}.start();
		}

	}

	private void downloadVwesion(final String urlpath) {
		// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				Log.i(TAG, "ok");
				HttpUtils http = new HttpUtils();
				final File file = new File(Environment
						.getExternalStorageDirectory(), System
						.currentTimeMillis() + ".apk");
				http.download(urlpath, file.getAbsolutePath(),
						new RequestCallBack<File>() {

							@Override
							public void onSuccess(ResponseInfo<File> arg0) {
								// TODO Auto-generated method stub
								Log.i(TAG, "success");
								Intent intent = new Intent();
								intent.setAction("android.intent.action.VIEW");
								intent.addCategory("android.intent.category.DEFAULT");
								intent.setDataAndType(Uri.fromFile(file),
										"application/vnd.android.package-archive");
								startActivity(intent);
								pd.dismiss();
							}

							@Override
							public void onLoading(long total, long current,
									boolean isUploading) {
								// TODO Auto-generated method stub
								super.onLoading(total, current, isUploading);
								pd.setMax(100);
								int progress = (int) ((current / total) * 100);
								pd.setProgress(progress);
							}

							@Override
							public void onFailure(HttpException arg0,
									String arg1) {
								// TODO Auto-generated method stub
								Log.i(TAG, "error");
								pd.dismiss();
							}
						});
			}
		});
		builder.setNegativeButton("cancle", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
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
				URL url = new URL(getResources().getString(R.string.url));
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				int code = conn.getResponseCode();
				if (code == 200) {
					InputStream is = conn.getInputStream();
					String result = StreamOutput.getJsonString(is);
					JSONObject jsonObject = new JSONObject(result);
					String serviceVersion = jsonObject.getString("version");
					String urlpath = jsonObject.getString("load");
					Log.i(TAG, urlpath);
					Log.i(TAG, serviceVersion);
					if (versionName.equals(serviceVersion)) {
						Log.i(TAG, "版本一致");
						SystemClock.sleep(3000);
						loadHomeActivity();
					} else {
						Log.i(TAG, "版本不一致请更新");

						msg.what = UNEQUAL;
						msg.obj = urlpath;

					}
				} else {
					Log.i(TAG, "读取失败");

					msg.what = ERROR;
				}

			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

				msg.what = ERROR;

			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				if (time < 3000) {
					SystemClock.sleep(3000 - time);
				} else {
				}
				handler.sendMessage(msg);
			}

		}
	}

	public void loadHomeActivity() {
		Intent intent = new Intent();
		intent.setClass(splaceActivity.this, homeActivity.class);
		startActivity(intent);
		finish();
	}
}
