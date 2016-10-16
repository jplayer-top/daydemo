package com.itheima.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.phonesafe.R;

public class enterPassword extends Activity {
	private EditText et_enterpassword;
	private ImageView iv_appicon;
	private TextView tv_appname;
	private PackageManager mpm;
	private ApplicationInfo applicationInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.enterpasword);
		iv_appicon = (ImageView) findViewById(R.id.iv_appicon);
		tv_appname = (TextView) findViewById(R.id.tv_appname);
		Intent intent = getIntent();
		String packagename = intent.getStringExtra("packagename");
		mpm = getPackageManager();
		try {
			applicationInfo = mpm.getApplicationInfo(
					packagename, 0);
			iv_appicon.setImageDrawable(applicationInfo.loadIcon(mpm));
			tv_appname.setText(applicationInfo.loadLabel(mpm).toString());
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		et_enterpassword = (EditText) findViewById(R.id.et_password);
		et_enterpassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				String password = s.toString();
				if ("123".equals(password)) {
					System.out.println("ok");
					finish();
					Intent intent = new Intent();
					intent.setAction("com.android.watchdog.recriver");
					intent.putExtra("packagename", applicationInfo.packageName);
					sendBroadcast(intent);
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		Intent launchIntetn = new Intent();
		launchIntetn.setAction("android.intent.action.MAIN");
		launchIntetn.addCategory("android.intent.category.HOME");
		launchIntetn.addCategory("android.intent.category.DEFAULT");
		startActivity(launchIntetn);
		finish();
	}
}
