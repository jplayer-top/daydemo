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
	// handler 线程中发回数据更新UI
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
	 * 将资源文件中的文件导入到手机储存中
	 * 
	 * @param path
	 *            文件的地址 存在以assets中只需写文件名就可以，比如 path = "address.db"
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
		// 耗时的逻辑放在子线程中，不过好像没必要放在这样的子线程中
		//开启病毒库自动更新的service
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
			// 获取包信息，用来获取包的版本号和版本信息等等
			packageinfo = getPackageManager().getPackageInfo(getPackageName(),
					0);
			versionName = packageinfo.versionName;
			mTextView.setText("版本号：" + versionName);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		// 在这里判断是否在初始界面检查更新程序
		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
		boolean flag = sp.getBoolean("flag", true);
		if (flag) {
			new Thread(new myRunable()).start();
		} else {
			//选择不更新的话就会直接等两秒钟进入主界面
			new Thread() {
				public void run() {
					SystemClock.sleep(2000);
					loadHomeActivity();
				};
			}.start();
		}

	}

	/**
	 * 版本不一致选择更新新版本
	 * 
	 * @param urlpath
	 *            json文件解析到的文件地址
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
								// 上层源码中的安装已应用程序的隐士意图
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
				// 获取当前设定的URL
				URL url = new URL(getResources().getString(R.string.url));
				// 打开链接
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				// 设定时长和GET方法
				conn.setConnectTimeout(3000);
				conn.setRequestMethod("GET");
				// 和获取状态码
				int code = conn.getResponseCode();
				if (code == 200) {
					// 通过网页源码流工具类获取json数据并且将数据解析
					InputStream is = conn.getInputStream();
					String result = StreamOutput.getJsonString(is);
					JSONObject jsonObject = new JSONObject(result);
					String serviceVersion = jsonObject.getString("version");
					String urlpath = jsonObject.getString("load");
					Log.i(TAG, urlpath);
					Log.i(TAG, serviceVersion);
					if (versionName.equals(serviceVersion)) {
						Log.i(TAG, "版本一致");
						//选择自动更新后，如果版本一致直接睡3000秒进入主界面
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
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();

				msg.what = ERROR;

			} catch (NotFoundException e) {
				e.printStackTrace();

			} catch (JSONException e) {
				e.printStackTrace();
			} finally {
				// 在首界面停留够时间
				endTime = System.currentTimeMillis();
				long time = endTime - startTime;
				if (time < 3000) {
					//版本不一致让他睡够三秒
					SystemClock.sleep(3000 - time);
				} else {
				}
				//最后选择发送消息，这样能够确保信息发送时间等达到所需要等待的时间
				handler.sendMessage(msg);
			}

		}
	}

	/**
	 * 进入home界面
	 */
	public void loadHomeActivity() {
		Intent intent = new Intent();
		intent.setClass(splaceActivity.this, homeActivity.class);
		startActivity(intent);
		finish();
	}
}
