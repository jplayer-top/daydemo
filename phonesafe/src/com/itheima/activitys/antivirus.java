package com.itheima.activitys;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.db.dao.antivirusDao;

public class antivirus extends Activity {
	// private TextView tv_text;
	private ImageView iv_roc;
	private PackageManager mPackageManager;
	private List<PackageInfo> mPackageInfo;
	private ProgressBar pb_findAntivirus;
	private LinearLayout ll_addView;
	private TextView tv_findsafe;
	private Handler handler = new Handler();
	private RotateAnimation ra;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.antivirus);
		pb_findAntivirus = (ProgressBar) findViewById(R.id.pb_findAntivirus);
		tv_findsafe = (TextView) findViewById(R.id.tv_findsafe);
		ll_addView = (LinearLayout) findViewById(R.id.ll_parent);
		iv_roc = (ImageView) findViewById(R.id.iv_rocl);
		ra = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(300);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		iv_roc.startAnimation(ra);
		mPackageManager = getPackageManager();
		new Thread() {
			public void run() {
				mPackageInfo = mPackageManager.getInstalledPackages(0);
				pb_findAntivirus.setMax(mPackageInfo.size());
				int progress = 0;
				for (final PackageInfo Info : mPackageInfo) {
					String path = Info.applicationInfo.sourceDir;
					String pathMd5 = getMD5(path);
					final String findAntivirus = antivirusDao.getAntivirusDao(
							getApplicationContext(), pathMd5);

					Log.i("antivirus", findAntivirus + "text");
					Log.i("antivirus", pathMd5);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String appName = Info.applicationInfo.loadLabel(
									mPackageManager).toString();
							Log.i("antivirus", appName);
							TextView tv = new TextView(getApplicationContext());
							tv.setText("扫描安全"
									+ Info.applicationInfo
											.loadLabel(mPackageManager));
							if ("".equals(findAntivirus)) {
								tv.setTextColor(Color.BLACK);
							} else {
								tv.setTextColor(Color.RED);
							}
							ll_addView.addView(tv, 0);
						}
					});
					progress++;
					pb_findAntivirus.setProgress(progress);
					SystemClock.sleep(50);
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_findsafe.setText("扫描完毕");
						ra.cancel();
						ra = null;
					}
				});
			};
		}.start();
	}
	public  void bt_fingantirious(View v){
		if(ra != null){
			Toast.makeText(getApplicationContext(), "正在扫描中...", 0).show();
			return;
		}
		ll_addView.removeAllViews();
		ra = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(300);
		ra.setRepeatCount(Animation.INFINITE);
		ra.setRepeatMode(Animation.RESTART);
		iv_roc.startAnimation(ra);
		tv_findsafe.setText("正在扫描");
		new Thread() {
			public void run() {
				mPackageInfo = mPackageManager.getInstalledPackages(0);
				pb_findAntivirus.setMax(mPackageInfo.size());
				int progress = 0;
				for (final PackageInfo Info : mPackageInfo) {
					String path = Info.applicationInfo.sourceDir;
					String pathMd5 = getMD5(path);
					final String findAntivirus = antivirusDao.getAntivirusDao(
							getApplicationContext(), pathMd5);

					Log.i("antivirus", findAntivirus + "text");
					Log.i("antivirus", pathMd5);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							String appName = Info.applicationInfo.loadLabel(
									mPackageManager).toString();
							Log.i("antivirus", appName);
							TextView tv = new TextView(getApplicationContext());
							if ("".equals(findAntivirus)) {
								tv.setText("扫描安全"
										+ Info.applicationInfo
										.loadLabel(mPackageManager));
								tv.setTextColor(Color.BLACK);
							} else {
								tv.setText("发现病毒"
										+ Info.applicationInfo
										.loadLabel(mPackageManager));
								tv.setTextColor(Color.RED);
							}
							tv.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Log.i("click", Info.applicationInfo.loadLabel(mPackageManager)+"");
								}
							});
							ll_addView.addView(tv, 0);
						}
					});
					progress++;
					pb_findAntivirus.setProgress(progress);
					SystemClock.sleep(50);
				}
				handler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						tv_findsafe.setText("扫描完毕");
						ra.cancel();
						ra = null;
					}
				});
			};
		}.start();
	}
	private static String getMD5(String path) {
		try {
			File file = new File(path);
			InputStream is = new FileInputStream(file);
			MessageDigest digest = MessageDigest.getInstance("MD5");
			int len;
			byte[] buffer = new byte[1024];

			while ((len = is.read(buffer)) != -1) {
				digest.update(buffer, 0, len);
			}
			byte[] result = digest.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : result) {
				int num = b & 0xff;
				String mdNum = Integer.toHexString(num);
				if (mdNum.length() <= 1) {
					sb.append("0");
				}
				sb.append(mdNum);
			}
			is.close();
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
