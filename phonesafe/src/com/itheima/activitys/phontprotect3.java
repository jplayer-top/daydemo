package com.itheima.activitys;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.phonesafe.R;
import com.itheima.ui.onBackPress;

public class phontprotect3 extends basePhoneProtect {
	private EditText et_phone_phoneprotect3;
	private Button bt_phontprotect3_getNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phoneprotect3);
		et_phone_phoneprotect3 = (EditText) findViewById(R.id.et_phone_phoneprotect3);
		bt_phontprotect3_getNum = (Button) findViewById(R.id.bt_phontprotect3_getNum);
		bt_phontprotect3_getNum.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(phontprotect3.this,
						phoneContacts.class);
				startActivityForResult(intent, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data.getStringExtra("phonenum") != null) {

			String phonenum = data.getStringExtra("phonenum");
			et_phone_phoneprotect3.setText(phonenum);
		}
	}

	public void setting_to_02(View v) {
		openNewActivity(phontprotect2.class);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
	}

	public void setting_to_04(View v) {
		String phonenum = et_phone_phoneprotect3.getText().toString().trim();
		if (phonenum == null || phonenum.equals("")) {
			Toast.makeText(this, "小主，手机号不是这样的", 0).show();
			return;
		}
		sp = getSharedPreferences("config", 0);
		Editor editor = sp.edit();
		editor.putString("phonenum", phonenum);
		editor.commit();
		openNewActivity(phontprotect4.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		onBackPress.setOnbackpress(phontprotect3.this);
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub
		String phonenum = et_phone_phoneprotect3.getText().toString().trim();
		if (phonenum == null || phonenum.equals("")) {
			Toast.makeText(this, "小主，手机号不是这样的", 0).show();
			return;
		}
		sp = getSharedPreferences("config", 0);
		Editor editor = sp.edit();
		editor.putString("phonenum", phonenum);
		editor.commit();
		openNewActivity(phontprotect4.class);
		overridePendingTransition(R.anim.anim_in, R.anim.anim_out);
	}

	@Override
	public void pre() {
		// TODO Auto-generated method stub
		openNewActivity(phontprotect2.class);
		overridePendingTransition(R.anim.anim_left_in, R.anim.anim_left_out);
	}
}
