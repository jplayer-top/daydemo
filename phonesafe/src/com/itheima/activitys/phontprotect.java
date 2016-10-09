package com.itheima.activitys;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.phonesafe.R;

public class phontprotect extends Activity {
	private TextView tv_phoneprotect_phonenum;
	private TextView tv_phoneprotect_result;
	private ImageView iv_foundLock;
	private SharedPreferences sp;
	private RelativeLayout rl_setEndChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneprotect);
		iv_foundLock = (ImageView) findViewById(R.id.iv_foundLock);
		sp = getSharedPreferences("config", 0);
		iv_foundLock.setBackgroundResource(R.drawable.lock);
		boolean isChecked = sp.getBoolean("isChecked", true);
		if (isChecked) {
			iv_foundLock.setBackgroundResource(R.drawable.lock);
		} else {
			iv_foundLock.setBackgroundResource(R.drawable.unlock);
		}
		rl_setEndChecked = (RelativeLayout) findViewById(R.id.rl_setEndChecked);
		rl_setEndChecked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isChecked = sp.getBoolean("isChecked", true);
				Editor editor = sp.edit();
				editor.putBoolean("isChecked", !isChecked);
				editor.commit();
				Log.i("isChecked", !isChecked+"");
				if (!isChecked) {
					iv_foundLock.setBackgroundResource(R.drawable.lock);
				} else {
					iv_foundLock.setBackgroundResource(R.drawable.unlock);
				}
			}
		});
		tv_phoneprotect_phonenum = (TextView) findViewById(R.id.tv_phoneprotect_phonenum);
		String phonenum = sp.getString("phonenum", "          ‘›Œ¥…Ë÷√");
		tv_phoneprotect_phonenum.setText("∞≤»´∫≈¬Î:        " + phonenum);
		tv_phoneprotect_result = (TextView) findViewById(R.id.tv_phoneprotect_result);
		tv_phoneprotect_result.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(phontprotect.this,
						phontprotect1.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
